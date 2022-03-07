package com.swrobotics.messenger.server.log;

import com.swrobotics.messenger.server.Message;

import java.io.File;
import java.io.IOException;
import java.io.PrintWriter;
import java.util.concurrent.Executors;
import java.util.concurrent.ScheduledExecutorService;
import java.util.concurrent.TimeUnit;

public final class FileLogger implements MessageLogger {
    private final long startTime;
    private final PrintWriter out;

    public FileLogger(File file) {
        try {
            if (!file.exists())
                file.createNewFile();

            out = new PrintWriter(file);
        } catch (IOException e) {
            throw new RuntimeException(e);
        }

        System.out.println("Logging messages to " + file.getAbsolutePath());

        startTime = System.currentTimeMillis();

        ScheduledExecutorService executor = Executors.newSingleThreadScheduledExecutor();
        executor.scheduleAtFixedRate(this::flush, 0, 1, TimeUnit.SECONDS);
    }

    private String getTimestamp() {
        return String.valueOf((System.currentTimeMillis() - startTime) / 1000.0);
    }

    @Override
    public void logEvent(String type, String name) {
        out.println(getTimestamp() + "\t" + type + "\t" + name);
    }

    @Override
    public void logEvent(String type, String name, String descriptor) {
        out.println(getTimestamp() + "\t" + type + "\t" + name + "\t" + descriptor);
    }

    @Override
    public void logMessage(Message msg) {
        out.println(getTimestamp() + "\t" + msg.getType() + "\t" + bytesToHex(msg.getData()));
    }

    private static final char[] HEX_ARRAY = "0123456789ABCDEF".toCharArray();
    private static String bytesToHex(byte[] bytes) {
        char[] hexChars = new char[bytes.length * 2];
        for (int j = 0; j < bytes.length; j++) {
            int v = bytes[j] & 0xFF;
            hexChars[j * 2] = HEX_ARRAY[v >>> 4];
            hexChars[j * 2 + 1] = HEX_ARRAY[v & 0x0F];
        }
        return new String(hexChars);
    }

    private void flush() {
        out.flush();
    }

    public void close() {
        flush();
        out.close();
    }
}
