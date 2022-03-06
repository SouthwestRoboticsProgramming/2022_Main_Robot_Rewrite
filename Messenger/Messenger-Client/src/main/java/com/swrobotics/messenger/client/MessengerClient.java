package com.swrobotics.messenger.client;

import java.io.ByteArrayInputStream;
import java.io.DataInputStream;
import java.io.DataOutputStream;
import java.io.IOException;
import java.net.Socket;
import java.util.function.BiConsumer;

public final class MessengerClient {
    private static final String HEARTBEAT = "_Heartbeat";
    private static final String LISTEN = "_Listen";
    private static final String UNLISTEN = "_Unlisten";
    private static final String DISCONNECT = "_Disconnect";

    private final Socket socket;
    private final DataInputStream in;
    private final DataOutputStream out;

    private BiConsumer<String, DataInputStream> messageHandler = (t, d) -> {};

    public MessengerClient(String host, int port) throws IOException {
        socket = new Socket(host, port);

        in = new DataInputStream(socket.getInputStream());
        out = new DataOutputStream(socket.getOutputStream());
    }

    public void sendMessage(String type, byte[] data) {
        try {
            out.writeUTF(type);
            out.writeInt(data.length);
            out.write(data);
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    public void listen(String type) {
        sendMessage(LISTEN, new byte[0]);
    }

    public void unlisten(String type) {
        sendMessage(UNLISTEN, new byte[0]);
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

        try {
            socket.close();
        } catch (IOException e) {
            e.printStackTrace();
        }
    }
}
