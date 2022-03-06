package com.swrobotics.messenger.server;

public final class MessengerServerMain {
    public static void main(String[] args) {
        RemoteClientConnector conn = new RemoteClientConnector(5805);
        conn.run();
    }

    private MessengerServerMain() {
        throw new AssertionError();
    }
}
