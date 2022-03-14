package com.swrobotics.lidar.task;

import com.swrobotics.lidar.library.Lidar;
import com.swrobotics.messenger.client.MessengerClient;

import java.io.ByteArrayOutputStream;
import java.io.DataOutputStream;
import java.io.IOException;

public final class LidarTask {
    public static void main(String[] args) {
        String host = args[0];
        int port = Integer.parseInt(args[1]);

        final MessengerClient msg;
        try {
            msg = new MessengerClient(host, port, "Lidar");
        } catch (IOException e) {
            throw new RuntimeException(e);
        }

        Lidar lidar = new Lidar();

        msg.makeHandler()
                .listen("Lidar:Start")
                .listen("Lidar:Stop")
                .setHandler((type, data) -> {
                    switch (type) {
                        case "Lidar:Start": {
                            lidar.startScanning();
                            break;
                        }
                        case "Lidar:Stop": {
                            lidar.stopScanning();
                            break;
                        }
                    }
                });

        lidar.setScanStartCallback(() -> {
            msg.sendMessage("Lidar:ScanStart", new byte[0]);
        });

        lidar.setScanDataCallback((entry) -> {
            ByteArrayOutputStream b = new ByteArrayOutputStream();
            DataOutputStream out = new DataOutputStream(b);

            try {
                out.writeInt(entry.getQuality());
                out.writeDouble(entry.getAngle());
                out.writeDouble(entry.getDistance());
            } catch (IOException e) {
                e.printStackTrace();
            }

            msg.sendMessage("Lidar:Scan", b.toByteArray());
        });

        System.out.println("Lidar health: " + lidar.getHealth().join());
        msg.sendMessage("Lidar:Ready", new byte[0]);

        while (true) {
            msg.readMessages();

            try {
                Thread.sleep(50);
            } catch (InterruptedException e) {
                e.printStackTrace();
            }
        }
    }
}
