package com.swrobotics.lidar.task;

import com.swrobotics.lidar.library.Lidar;
import com.swrobotics.lidar.library.LidarHealth;
import com.swrobotics.messenger.client.MessengerClient;

import java.io.DataInputStream;
import java.io.IOException;

public final class LidarTask {
    private static final String IN_START = "Lidar:Start";
    private static final String IN_STOP = "Lidar:Stop";

    private static final String IN_LOCATION = "RoboRIO:Location";

    private static final String OUT_READY = "Lidar:Ready";
    private static final String OUT_SCAN_START = "Lidar:ScanStart";
    private static final String OUT_SCAN = "Lidar:Scan";

    private static final boolean INVERT_ANGLE = false;
    private static final boolean INVERT_LOC_ANGLE = false;
    private static final double ANGLE_OFFSET = 0;

    private static final double IGNORED_RADIUS = 898.024 / 2;

    private final MessengerClient msg;
    private final Lidar lidar;

    private double locX, locY;
    private double locRot;

    private LidarTask(String host, int port) {
        try {
            msg = new MessengerClient(host, port, "Lidar");
        } catch (IOException e) {
            throw new RuntimeException(e);
        }

        lidar = new Lidar();

        msg.makeHandler()
                .listen(IN_START)
                .listen(IN_STOP)
                .listen(IN_LOCATION)
                .setHandler(this::onMessage);

        lidar.setScanStartCallback(() -> {
            msg.sendMessage(OUT_SCAN_START);
        });

        lidar.setScanDataCallback((entry) -> {
            int quality = entry.getQuality();
            double angle = entry.getAngle();
            double distance = entry.getDistance();

            if (INVERT_ANGLE)
                angle = -angle;

            angle += ANGLE_OFFSET;

            // Don't send the sample if it is bad
            if (quality == 0 || distance == 0)
                return;

            // Ignore samples that are too close
            if (distance < IGNORED_RADIUS)
                return;

            // Account for robot rotation
            angle -= locRot;

            // Get field-aligned position of point relative to robot
            double angleRadians = Math.toRadians(angle);
            double distanceMeters = distance / 1000.0;
            double localX = Math.cos(angleRadians) * distanceMeters;
            double localY = Math.sin(angleRadians) * distanceMeters;

            // Find the field position of the point
            double fieldX = localX + locX;
            double fieldY = localY + locY;

            // Send message
            msg.builder(OUT_SCAN)
                    .addInt(entry.getQuality())
                    .addDouble(fieldX)
                    .addDouble(fieldY)
                    .send();
        });

        LidarHealth health = lidar.getHealth().join();
        System.out.println("Lidar health: " + health);

        if (health == LidarHealth.ERROR) {
            System.err.println("Lidar is not able to scan, shutting down");
            System.exit(1);
        }

        msg.sendMessage(OUT_READY);
    }

    private void onMessage(String type, DataInputStream in) throws IOException {
        switch (type) {
            case IN_START: {
                System.out.println("Starting lidar scan");
                lidar.startScanning();
                break;
            }
            case IN_STOP: {
                System.out.println("Stopping scan");
                lidar.stopScanning();
                break;
            }
            case IN_LOCATION: {
                // Note: This is assuming that distances are in meters and rotation is in degrees

                locX = in.readDouble();
                locY = in.readDouble();
                locRot = in.readDouble();

                if (INVERT_LOC_ANGLE)
                    locRot = -locRot;

                break;
            }
        }
    }

    private void run() {
        while (true) {
            msg.readMessages();

            try {
                Thread.sleep(50);
            } catch (InterruptedException e) {
                e.printStackTrace();
            }
        }
    }

    public static void main(String[] args) {
        String host = args[0];
        int port = Integer.parseInt(args[1]);

        new LidarTask(host, port).run();
    }
}
