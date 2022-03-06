package com.swrobotics.messenger.server;

import java.io.File;
import java.io.IOException;
import java.io.PrintWriter;

public final class MessageLogger {
    private final long startTime;
    private final PrintWriter out;

    public MessageLogger(File file) {
        try {
            if (!file.exists())
                file.createNewFile();

            out = new PrintWriter(file);
        } catch (IOException e) {
            throw new RuntimeException(e);
        }

        System.out.println("Logging messages to " + file.getAbsolutePath());

        startTime = System.currentTimeMillis();
    }

    private String getTimestamp() {
        return String.valueOf((System.currentTimeMillis() - startTime) / 1000.0);
    }

    public void logEvent(String type, String name) {
        out.println(getTimestamp() + "\t" + type + "\t" + name);
    }

    public void logEvent(String type, String name, String descriptor) {
        out.println(getTimestamp() + "\t" + type + "\t" + name + "\t" + descriptor);
    }

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

    public void flush() {
        out.flush();
    }

    public void close() {
        flush();
        out.close();
    }
}
