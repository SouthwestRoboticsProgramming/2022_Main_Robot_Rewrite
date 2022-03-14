package com.swrobotics.bert.constants;

public class InputConstants {
    public static final int DRIVE_CONTROLLER_ID = 0;
    public static final int MANIPULATOR_CONTROLLER_ID = 1;

    public static final double JOYSTICK_DEAD_ZONE = 0.18;

    private InputConstants() {
        throw new AssertionError();
    }
}
