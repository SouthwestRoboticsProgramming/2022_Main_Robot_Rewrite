package com.swrobotics.messenger.test;

import com.swrobotics.messenger.client.MessengerClient;

public final class MessengerTest {
    public static void main(String[] args) throws Exception {
        MessengerClient msg = new MessengerClient("localhost", 5805, "Test");

        msg.makeHandler()
                .listen("Test Message")
                .setHandler((type, data) -> {
                    System.out.println(type);
                });

        msg.sendMessage("Test Message", new byte[0]);

        for (int i = 0; i < 5; i++) {
            msg.readMessages();

            Thread.sleep(1000);
        }

        msg.disconnect();
    }
}
