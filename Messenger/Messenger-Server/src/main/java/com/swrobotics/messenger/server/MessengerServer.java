package com.swrobotics.messenger.server;

import java.io.File;
import java.util.Collections;
import java.util.HashSet;
import java.util.Set;
import java.util.concurrent.Executors;
import java.util.concurrent.ScheduledExecutorService;
import java.util.concurrent.TimeUnit;

public final class MessengerServer {
    private static final MessengerServer INSTANCE = new MessengerServer();
    public static MessengerServer get() { return INSTANCE; }

    private final MessengerConfiguration config;
    private final Set<Client> clients;
    private final MessageLogger log;

    private MessengerServer() {
        config = MessengerConfiguration.loadFromFile(new File("config.properties"));
        clients = Collections.synchronizedSet(new HashSet<>());

        if (config.getLogFile() == null) {
            log = null;
        } else {
            log = new MessageLogger(config.getLogFile());
            ScheduledExecutorService executor = Executors.newSingleThreadScheduledExecutor();
            executor.scheduleAtFixedRate(log::flush, 0, 1, TimeUnit.SECONDS);
        }
    }

    public void onMessage(Message msg) {
        if (log != null)
            log.logMessage(msg);

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

    public MessengerConfiguration getConfig() {
        return config;
    }

    public MessageLogger getLog() {
        return log;
    }
}
