package com.swrobotics.bert.constants;

public final class CommunicationConstants {
    // FIXME: Set to the correct values for the robot system
    public static final String MESSENGER_HOST = "localhost";
    public static final int MESSENGER_PORT = 5805;
    public static final String MESSENGER_NAME = "RoboRIO";

    public static final String RASPBERRY_PI_PREFIX = "RPi";
    public static final String JETSON_NANO_PREFIX = "Nano";

    public static final String LIDAR_NAME = "Lidar";
    public static final String PATHFINDING_NAME = "Pathfinding";
    public static final String VISION_NAME = "Vision";

    private CommunicationConstants() {
        throw new AssertionError();
    }
}