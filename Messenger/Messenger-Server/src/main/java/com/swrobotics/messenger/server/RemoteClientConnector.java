package com.swrobotics.messenger.server;

import java.io.IOException;
import java.net.ServerSocket;
import java.net.Socket;

public final class RemoteClientConnector implements Runnable {
    private final int port;

    public RemoteClientConnector(int port) {
        this.port = port;
    }

    @Override
    public void run() {
        System.out.println("Opening port " + port + " for Messenger");

        ServerSocket socket = null;
        try {
            socket = new ServerSocket(port);
        } catch (IOException e) {
            System.err.println("Failed to open port " + port);
            e.printStackTrace();
            System.exit(1);
        }

        while (true) {
            try {
                Socket clientSocket = socket.accept();
                System.out.println("Client has connected");

                RemoteClient client = new RemoteClient(clientSocket);
                new Thread(client).start();
            } catch (IOException e) {
                System.err.println("Exception whilst accepting a client connection:");
                e.printStackTrace();
            }
        }
    }
}
