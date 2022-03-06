package com.swrobotics.messenger.server;

import java.util.Collections;
import java.util.HashSet;
import java.util.Set;

public final class MessengerServer {
    private static final MessengerServer INSTANCE = new MessengerServer();
    public static MessengerServer get() { return INSTANCE; }

    private final Set<Client> clients;

    private MessengerServer() {
        clients = Collections.synchronizedSet(new HashSet<>());
    }

    public void onMessage(Message msg) {
        for (Client client : clients) {
            if (client.listensTo(msg.getType())) {
                client.sendMessage(msg);
            }
        }
    }

    public void addClient(Client client) {
        clients.add(client);
    }

    public void removeClient(Client client) {
        clients.remove(client);
    }
}
