package com.swrobotics.taskmanager.server;

import org.zeroturnaround.exec.ProcessExecutor;
import org.zeroturnaround.exec.StartedProcess;

import java.io.File;
import java.io.IOException;
import java.nio.file.Files;

public final class Task {
    private static final String[] COMMAND = {"bash", "task.sh"};

    private final String name;
    private final File folder;
    private Process process;

    public Task(String name, File folder) {
        this.name = name;
        this.folder = folder;
    }

    public String getName() {
        return name;
    }

    public void start() {
        if (isRunning())
            stop();

        System.out.println("Starting task: " + name);
        try {
            StartedProcess p = new ProcessExecutor()
                    .command(COMMAND)
                    .directory(folder)
                    .redirectOutput(new OutputLogger(this, OutputType.STDOUT))
                    .redirectError(new OutputLogger(this, OutputType.STDERR))
                    .start();
            process = p.getProcess();
        } catch (IOException e) {
            System.err.println("Exception whilst starting task: " + name);
            e.printStackTrace();
        }
    }

    public void stop() {
        if (!isRunning())
            return;

        System.out.println("Stopping task: " + name);
        process.descendants().forEach((child) -> {
            child.destroyForcibly();
        });
        process.destroyForcibly();
    }

    public void delete() {
        System.out.println("Deleting task: " + name);

        if (isRunning())
            stop();

        deleteFile(folder);

        TaskManagerServer.get().removeTask(this);
    }

    private void deleteFile(File file) {
        File[] contents = file.listFiles();
        if (contents != null) {
            for (File f : contents) {
                if (!Files.isSymbolicLink(f.toPath())) {
                    deleteFile(f);
                }
            }
        }
        file.delete();
    }

    public boolean isRunning() {
        return process != null && process.isAlive();
    }
}
