package com.swrobotics.messenger.server;

import java.io.File;
import java.io.FileReader;
import java.io.FileWriter;
import java.io.IOException;
import java.util.Properties;

public final class MessengerConfiguration {
    private static final Properties defaultProps = new Properties();
    static {
        defaultProps.put("port", "5805");
        defaultProps.put("logFile", "");
    }

    private final int port;
    private final File logFile;

    private MessengerConfiguration(int port, File logFile) {
        this.port = port;
        this.logFile = logFile;
    }

    public static MessengerConfiguration loadFromFile(File file) {
        if (!file.exists()) {
            try {
                defaultProps.store(new FileWriter(file), "Configuration for Messenger Server");
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

        return new MessengerConfiguration(
                Integer.parseInt(props.getProperty("port")),
                props.getProperty("logFile").equals("") ? null : new File(props.getProperty("logFile"))
        );
    }

    public int getPort() {
        return port;
    }

    public File getLogFile() {
        return logFile;
    }
}
