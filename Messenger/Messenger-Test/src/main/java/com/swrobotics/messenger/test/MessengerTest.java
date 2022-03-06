package com.swrobotics.messenger.test;

import com.swrobotics.messenger.client.MessengerClient;

public final class MessengerTest {
    public static void main(String[] args) throws Exception {
        MessengerClient msg = new MessengerClient("localhost", 5805, "Test");

        msg.listen("Test Message");
        msg.setMessageHandler((type, data) -> {
            System.out.println(type);
        });

        for (int i = 0; i < 10; i++) {
            msg.readMessages();

            msg.sendMessage("Test Message", new byte[] { (byte) i });

            Thread.sleep(1000);
        }

        msg.disconnect();
    }
}
