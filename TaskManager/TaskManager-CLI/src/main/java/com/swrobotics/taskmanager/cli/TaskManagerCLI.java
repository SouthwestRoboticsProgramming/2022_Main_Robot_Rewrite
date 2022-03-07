package com.swrobotics.taskmanager.cli;

import com.swrobotics.messenger.client.MessengerClient;
import com.swrobotics.taskmanager.api.TaskManagerAPI;
import com.swrobotics.taskmanager.api.UploadResult;

import java.io.File;
import java.io.IOException;
import java.util.Arrays;
import java.util.Scanner;
import java.util.Set;
import java.util.concurrent.Executors;
import java.util.concurrent.ScheduledExecutorService;
import java.util.concurrent.ScheduledFuture;
import java.util.concurrent.TimeUnit;

public final class TaskManagerCLI {
    private final MessengerClient msg;
    private final TaskManagerAPI api;

    private final ScheduledExecutorService executor;
    private final ScheduledFuture<?> reader;

    private TaskManagerCLI(MessengerClient msg, TaskManagerAPI api) {
        this.msg = msg;
        this.api = api;

        executor = Executors.newSingleThreadScheduledExecutor();
        reader = executor.scheduleAtFixedRate(msg::readMessages, 0, 1000/50, TimeUnit.MILLISECONDS);
    }

    private void runCommand(String[] tokens) {
        switch (tokens[0]) {
            case "list": {
                Set<String> tasks = api.getTasks().join();
                System.out.print("Tasks (");
                System.out.print(tasks.size());
                System.out.print("): ");

                boolean comma = false;
                for (String task : tasks) {
                    if (comma)
                        System.out.print(", ");
                    comma = true;

                    System.out.print(task);
                }

                System.out.println();
                break;
            }
            case "start": {
                if (tokens.length < 2) {
                    System.err.println("Usage: start <task>");
                    break;
                }
                api.startTask(tokens[1]);

                break;
            }
            case "stop": {
                if (tokens.length < 2) {
                    System.err.println("Usage: stop <task>");
                    break;
                }
                api.stopTask(tokens[1]);

                break;
            }
            case "status": {
                if (tokens.length < 2) {
                    System.err.println("Usage: status <task>");
                    break;
                }

                boolean running = api.isTaskRunning(tokens[1]).join();
                System.out.println("Task '" + tokens[1] + "' is " + (running ? "RUNNING" : "IDLE"));

                break;
            }
            case "exists": {
                if (tokens.length < 2) {
                    System.err.println("Usage: exists <task>");
                    break;
                }

                boolean exists = api.taskExists(tokens[1]).join();
                System.out.println("Task '" + tokens[1] + "' " + (exists ? "exists" : "does not exist"));

                break;
            }
            case "delete": {
                if (tokens.length < 2) {
                    System.err.println("Usage: delete <task>");
                    break;
                }
                api.deleteTask(tokens[1]);

                break;
            }
            case "upload": {
                if (tokens.length < 3) {
                    System.err.println("Usage: upload <name> <file>");
                    break;
                }

                UploadResult result = api.uploadTask(tokens[1], new File(tokens[2])).join();
                System.out.println("Output status: " + result);

                break;
            }
        }
    }

    private void runInteractive() {
        Scanner scanner = new Scanner(System.in);

        while (true) {
            System.out.print("> ");
            String line = scanner.nextLine();
            String[] tokens = line.split(" ");
            if (tokens.length == 0) continue;

            if (tokens[0].equals("exit"))
                break;

            runCommand(tokens);
        }

        shutdown();
    }

    private void runOneCommand(String[] cmdArgs) {
        System.out.print("> ");
        for (String cmd : cmdArgs) {
            System.out.print(cmd);
            System.out.print(" ");
        }
        System.out.println();

        runCommand(cmdArgs);
        shutdown();
    }

    private void shutdown() {
        reader.cancel(false);
        executor.shutdown();

        msg.disconnect();
    }

    public static void main(String[] args) {
        if (args.length < 3) {
            System.err.println("Usage: taskman-cli <host> <port> <prefix> [command...]");
            System.exit(1);
        }

        String host = args[0];
        int port = -1;
        String prefix = args[2];

        try {
            port = Integer.parseInt(args[1]);
        } catch (NumberFormatException e) {
            System.err.println("Not a number: " + args[1]);
            System.exit(1);
        }
        if (port < 0 || port >= 65536) {
            System.err.println("Port out of range (0 - 65535): " + port);
        }

        System.out.println("Connecting to " + host + ":" + port + " with prefix " + prefix);
        MessengerClient msg = null;
        try {
            msg = new MessengerClient(host, port, "TaskManager-CLI");
        } catch (IOException e) {
            System.err.println("Failed to connect to Messenger server:");
            e.printStackTrace();
            System.exit(1);
        }

        TaskManagerAPI api = new TaskManagerAPI(msg, prefix);

        TaskManagerCLI cli = new TaskManagerCLI(msg, api);

        System.out.println("Connected");

        if (args.length == 3) {
            cli.runInteractive();
        } else {
            cli.runOneCommand(Arrays.copyOfRange(args, 3, args.length));
        }
    }
}
