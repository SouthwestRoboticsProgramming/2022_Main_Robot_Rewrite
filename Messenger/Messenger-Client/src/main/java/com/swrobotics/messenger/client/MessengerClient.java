package com.swrobotics.messenger.client;

import java.io.*;
import java.net.Socket;
import java.util.concurrent.Executors;
import java.util.concurrent.ScheduledExecutorService;
import java.util.concurrent.ScheduledFuture;
import java.util.concurrent.TimeUnit;
import java.util.function.BiConsumer;

public final class MessengerClient {
    private static final String HEARTBEAT = "_Heartbeat";
    private static final String LISTEN = "_Listen";
    private static final String UNLISTEN = "_Unlisten";
    private static final String DISCONNECT = "_Disconnect";

    private final Socket socket;
    private final DataInputStream in;
    private final DataOutputStream out;
    private final ScheduledExecutorService executor;
    private final ScheduledFuture<?> heartbeatFuture;

    private BiConsumer<String, DataInputStream> messageHandler = (t, d) -> {};

    public MessengerClient(String host, int port, String name) throws IOException {
        socket = new Socket(host, port);

        in = new DataInputStream(socket.getInputStream());
        out = new DataOutputStream(socket.getOutputStream());

        out.writeUTF(name);

        executor = Executors.newSingleThreadScheduledExecutor();
        heartbeatFuture = executor.scheduleAtFixedRate(() -> {
            sendMessage(HEARTBEAT, new byte[0]);
        }, 0, 1, TimeUnit.SECONDS);
    }

    public void sendMessage(String type, byte[] data) {
        synchronized (out) {
            try {
                out.writeUTF(type);
                out.writeInt(data.length);
                out.write(data);
            } catch (IOException e) {
                e.printStackTrace();
            }
        }
    }

    public void listen(String type) {
        sendMessage(LISTEN, encodeStringUTF(type));
    }

    public void unlisten(String type) {
        sendMessage(UNLISTEN, encodeStringUTF(type));
    }

    public void setMessageHandler(BiConsumer<String, DataInputStream> handler) {
        messageHandler = handler;
    }

    public void readMessages() {
        try {
            while (in.available() > 0) {
                String type = in.readUTF();
                byte[] data = new byte[in.readInt()];
                in.readFully(data);

                messageHandler.accept(type, new DataInputStream(new ByteArrayInputStream(data)));
            }
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    public void disconnect() {
        sendMessage(DISCONNECT, new byte[0]);

        heartbeatFuture.cancel(false);
        executor.shutdown();

        try {
            socket.close();
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    private byte[] encodeStringUTF(String str) {
        ByteArrayOutputStream b = new ByteArrayOutputStream();
        DataOutputStream out = new DataOutputStream(b);

        try {
            out.writeUTF(str);
        } catch (IOException e) {
            e.printStackTrace();
        }

        return b.toByteArray();
    }
}
