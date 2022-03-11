package com.swrobotics.messenger.test;

import com.swrobotics.messenger.client.MessengerClient;

public final class MessengerTest {
    public static void main(String[] args) throws Exception {
        MessengerClient msg = new MessengerClient("10.21.29.3", 5805, "Test");

        msg.makeHandler()
                .listen("Test Message")
                .setHandler((type, data) -> {
                    System.out.println(type);
                });

        msg.sendMessage("Lidar:Start", new byte[0]);

        for (int i = 0; i < 5; i++) {
            msg.readMessages();

            Thread.sleep(1000);
        }

        msg.sendMessage("Lidar:Stop", new byte[0]);

        msg.disconnect();
    }
}
