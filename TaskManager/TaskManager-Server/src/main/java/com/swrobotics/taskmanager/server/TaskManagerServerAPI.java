package com.swrobotics.taskmanager.server;

import com.swrobotics.messenger.client.MessengerClient;

import java.io.*;
import java.util.Collection;
import java.util.zip.ZipEntry;
import java.util.zip.ZipInputStream;

public final class TaskManagerServerAPI {
    private static final String IN_PING = ":Ping";
    private static final String IN_TASK_START = ":StartTask";
    private static final String IN_TASK_STOP = ":StopTask";
    private static final String IN_TASK_IS_RUNNING = ":IsTaskRunning";
    private static final String IN_TASK_DELETE = ":DeleteTask";
    private static final String IN_TASK_UPLOAD = ":UploadTask";
    private static final String IN_TASK_EXISTS = ":GetTaskExists";
    private static final String IN_GET_TASKS = ":GetTasks";

    private static final String OUT_PONG = ":Pong";
    private static final String OUT_TASK_IS_RUNNING = ":TaskRunning";
    private static final String OUT_TASK_EXISTS = ":TaskExists";
    private static final String OUT_TASKS = ":Tasks";
    private static final String OUT_UPLOAD_STATUS = ":UploadStatus";
    private static final String OUT_TASK_STDOUT = ":StdOut";
    private static final String OUT_TASK_STDERR = ":StdErr";

    private final MessengerClient msg;
    private final String prefix;

    public TaskManagerServerAPI(MessengerClient msg, TaskManagerConfiguration config) {
        this.msg = msg;

        prefix = config.getMessengerPrefix();

        msg.makeHandler()
                .listen(prefix + IN_PING)
                .listen(prefix + IN_TASK_START)
                .listen(prefix + IN_TASK_STOP)
                .listen(prefix + IN_TASK_IS_RUNNING)
                .listen(prefix + IN_TASK_DELETE)
                .listen(prefix + IN_TASK_UPLOAD)
                .listen(prefix + IN_TASK_EXISTS)
                .listen(prefix + IN_GET_TASKS)
                .setHandler(this::messageHandler);
    }

    public void sendTaskOutput(String name, OutputType type, String line) {
        ByteArrayOutputStream b = new ByteArrayOutputStream();
        DataOutputStream out = new DataOutputStream(b);

        try {
            out.writeUTF(name);
            out.writeUTF(line);
        } catch (IOException e) {
            e.printStackTrace();
            return;
        }

        String message;
        switch (type) {
            case STDOUT:
                message = OUT_TASK_STDOUT;
                break;
            case STDERR:
                message = OUT_TASK_STDERR;
                break;
            default:
                throw new IllegalArgumentException(String.valueOf(type));
        }

        msg.sendMessage(message, b.toByteArray());
    }

    // Allows the client to test whether the server is alive
    private void handlePing() {
        msg.sendMessage(prefix + OUT_PONG, new byte[0]);
    }

    private void handleTaskStart(DataInputStream in) throws IOException {
        String name = in.readUTF();
        Task task = TaskManagerServer.get().getTask(name);

        if (task == null) {
            System.err.println("Can't start nonexistent task: " + name);
            return;
        }

        task.start();
    }

    private void handleTaskStop(DataInputStream in) throws IOException {
        String name = in.readUTF();
        Task task = TaskManagerServer.get().getTask(name);

        if (task == null) {
            System.err.println("Can't stop nonexistent task: " + name);
            return;
        }

        task.stop();
    }

    private void handleTaskIsRunning(DataInputStream in) throws IOException {
        String name = in.readUTF();
        Task task = TaskManagerServer.get().getTask(name);

        boolean running = false;
        if (task != null) {
            running = task.isRunning();
        }

        ByteArrayOutputStream b = new ByteArrayOutputStream();
        DataOutputStream out = new DataOutputStream(b);

        out.writeUTF(name);
        out.writeBoolean(running);

        msg.sendMessage(prefix + OUT_TASK_IS_RUNNING, b.toByteArray());
    }

    private void handleTaskDelete(DataInputStream in) throws IOException {
        String name = in.readUTF();
        Task task = TaskManagerServer.get().getTask(name);

        if (task == null) {
            System.err.println("Can't delete nonexistent task: " + name);
            return;
        }

        task.delete();
    }

    private void handleTaskUpload(DataInputStream in) throws IOException {
        System.out.println("Receiving");

        String name = in.readUTF();
        Task task = TaskManagerServer.get().getTask(name);

        if (task != null) {
            System.out.println("Task already exists, deleting: " + name);
            task.delete();
        }

        byte[] payload = new byte[in.readInt()];
        in.readFully(payload);

        boolean success;
        try {
            File folder = new File(TaskManagerServer.get().getConfig().getTaskFolder(), name);

            ZipInputStream zip = new ZipInputStream(new ByteArrayInputStream(payload));
            ZipEntry entry;
            while ((entry = zip.getNextEntry()) != null) {
                File file = new File(folder, entry.getName());

                System.out.println("Unpacking " + file);

                String destPath = folder.getCanonicalPath();
                String filePath = file.getCanonicalPath();

                if (!filePath.startsWith(destPath + File.separator)) {
                    throw new IOException("ZIP entry outside of target directory: " + entry.getName());
                }

                if (entry.isDirectory()) {
                    if (!file.isDirectory() && !file.mkdirs()) {
                        throw new IOException("Failed to create directory: " + file);
                    }
                } else {
                    File parent = file.getParentFile();
                    if (!parent.isDirectory() && !parent.mkdirs()) {
                        throw new IOException("Failed to create directory: " + parent);
                    }

                    FileOutputStream out = new FileOutputStream(file);
                    int length;
                    byte[] buffer = new byte[1024];
                    while ((length = zip.read(buffer)) > 0) {
                        out.write(buffer, 0, length);
                    }
                    out.close();
                }
            }
            zip.closeEntry();
            zip.close();

            if (!new File(folder, "task.sh").setExecutable(true)) {
                throw new IOException("Failed to make task.sh executable");
            }

            TaskManagerServer.get().addTask(new Task(name, folder));

            System.out.println("Imported task: " + name);
            success = true;
        } catch (IOException e) {
            System.err.println("Exception whilst importing task: " + name);
            e.printStackTrace();
            success = false;
        }

        ByteArrayOutputStream b = new ByteArrayOutputStream();
        DataOutputStream out = new DataOutputStream(b);

        out.writeUTF(name);
        out.writeBoolean(success);

        msg.sendMessage(prefix + OUT_UPLOAD_STATUS, b.toByteArray());
    }

    private void handleTaskExists(DataInputStream in) throws IOException {
        String name = in.readUTF();
        Task task = TaskManagerServer.get().getTask(name);

        ByteArrayOutputStream b = new ByteArrayOutputStream();
        DataOutputStream out = new DataOutputStream(b);

        out.writeUTF(name);
        out.writeBoolean(task != null);

        msg.sendMessage(prefix + OUT_TASK_EXISTS, b.toByteArray());
    }

    private void handleGetTasks(DataInputStream in) throws IOException {
        Collection<Task> tasks = TaskManagerServer.get().getTasks();

        ByteArrayOutputStream b = new ByteArrayOutputStream();
        DataOutputStream out = new DataOutputStream(b);

        out.writeInt(tasks.size());
        for (Task task : tasks) {
            out.writeUTF(task.getName());
        }

        msg.sendMessage(prefix + OUT_TASKS, b.toByteArray());
    }

    private void messageHandler(String type, DataInputStream in) throws IOException {
        switch (type.substring(prefix.length())) {
            case IN_PING:
                handlePing();
                break;
            case IN_TASK_START:
                handleTaskStart(in);
                break;
            case IN_TASK_STOP:
                handleTaskStop(in);
                break;
            case IN_TASK_IS_RUNNING:
                handleTaskIsRunning(in);
                break;
            case IN_TASK_DELETE:
                handleTaskDelete(in);
                break;
            case IN_TASK_UPLOAD:
                handleTaskUpload(in);
                break;
            case IN_TASK_EXISTS:
                handleTaskExists(in);
                break;
            case IN_GET_TASKS:
                handleGetTasks(in);
                break;
            default:
                throw new IllegalArgumentException("Unknown message: " + type.substring(prefix.length()));
        }
    }

    public void run() {
        while (true) {
            msg.readMessages();

            try {
                Thread.sleep(1000 / 50);
            } catch (InterruptedException e) {
                e.printStackTrace();
            }
        }
    }
}
