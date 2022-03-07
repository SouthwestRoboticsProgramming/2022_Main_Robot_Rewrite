package com.swrobotics.taskmanager.server;

import com.swrobotics.messenger.client.MessengerClient;

import java.io.File;
import java.io.IOException;
import java.util.Collection;
import java.util.Map;
import java.util.concurrent.ConcurrentHashMap;

public final class TaskManagerServer {
    private static final TaskManagerServer INSTANCE = new TaskManagerServer();
    public static TaskManagerServer get() { return INSTANCE; }

    private final TaskManagerConfiguration config;
    private final TaskManagerServerAPI api;
    private final Map<String, Task> tasks;

    private TaskManagerServer() {
        config = TaskManagerConfiguration.loadFromFile(new File("config.properties"));

        MessengerClient msg = null;
        try {
            System.out.println("Connecting to " + config.getMessengerHost() + ":" + config.getMessengerPort() + " with prefix " + config.getMessengerPrefix());
            msg = new MessengerClient(config.getMessengerHost(), config.getMessengerPort(), config.getMessengerPrefix());
            System.out.println("Connected");
        } catch (IOException e) {
            System.err.println("Messenger connection failed:");
            e.printStackTrace();
            System.exit(1);
        }
        api = new TaskManagerServerAPI(msg, config);

        tasks = new ConcurrentHashMap<>();
        try {
            loadTasks();
        } catch (IOException e) {
            System.err.println("Failed to load tasks:");
            e.printStackTrace();
            System.exit(1);
        }
    }

    public Task getTask(String name) {
        return tasks.get(name);
    }

    public Collection<Task> getTasks() {
        return tasks.values();
    }

    public TaskManagerConfiguration getConfig() {
        return config;
    }

    public TaskManagerServerAPI getAPI() {
        return api;
    }

    public void addTask(Task task) {
        tasks.put(task.getName(), task);
    }

    public void removeTask(Task task) {
        tasks.remove(task.getName());
    }

    private void loadTasks() throws IOException {
        File tasksFolder = new File("tasks/");
        if (!tasksFolder.exists()) {
            tasksFolder.mkdir();
        }

        for (File file : tasksFolder.listFiles()) {
            if (!file.isDirectory()) {
                System.err.println("Unexpected regular file '" + file.getName() + "' in tasks directory");
                continue;
            }

            String name = file.getName();
            tasks.put(name, new Task(name, file));
        }
    }
}
