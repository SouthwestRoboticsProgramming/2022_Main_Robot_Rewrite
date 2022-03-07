package com.swrobotics.taskmanager.server;

import java.io.File;
import java.io.FileReader;
import java.io.FileWriter;
import java.io.IOException;
import java.util.Properties;

public final class TaskManagerConfiguration {
    private static final Properties defaultProps = new Properties();
    static {
        defaultProps.put("messengerHost", "localhost");
        defaultProps.put("messengerPort", "5805");
        defaultProps.put("messengerPrefix", "TaskManager");
        defaultProps.put("taskFolder", "tasks");
    }

    private final String messengerHost;
    private final int messengerPort;
    private final String messengerPrefix;
    private final File taskFolder;

    public static TaskManagerConfiguration loadFromFile(File file) {
        if (!file.exists()) {
            try {
                defaultProps.store(new FileWriter(file), "Configuration for TaskManager Server");
            } catch (IOException e) {
                e.printStackTrace();
            }
        }

        Properties props = new Properties(defaultProps);
        try {
            props.load(new FileReader(file));
        } catch (IOException e) {
            e.printStackTrace();
        }

        return new TaskManagerConfiguration(
                props.getProperty("messengerHost"),
                Integer.parseInt(props.getProperty("messengerPort")),
                props.getProperty("messengerPrefix"),
                new File(props.getProperty("taskFolder"))
        );
    }

    private TaskManagerConfiguration(String messengerHost, int messengerPort, String messengerPrefix, File taskFolder) {
        this.messengerHost = messengerHost;
        this.messengerPort = messengerPort;
        this.messengerPrefix = messengerPrefix;
        this.taskFolder = taskFolder;
    }

    public String getMessengerHost() {
        return messengerHost;
    }

    public int getMessengerPort() {
        return messengerPort;
    }

    public String getMessengerPrefix() {
        return messengerPrefix;
    }

    public File getTaskFolder() {
        return taskFolder;
    }
}
