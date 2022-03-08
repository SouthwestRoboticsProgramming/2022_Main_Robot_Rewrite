package com.swrobotics.bert.constants;

public final class DriveConstants {
    // TODO: Make configurable with ShuffleWood or ShuffleBoard
    public static final double DRIVE_KP = 0;
    public static final double DRIVE_KI = 0;
    public static final double DRIVE_KD = 0;
    public static final double DRIVE_KF = 0;

    public static final double TURN_KP = 0;
    public static final double TURN_KI = 0;
    public static final double TURN_KD = 0;
    public static final double TURN_KF = 0;



    public static final double WHEEL_DIAMETER = 0.10;  // Meters
    public static final int TALON_FX_NATIVE_SENSOR_UNITS_PER_ROTATION = 2048;
    private static final int TALON_FX_NATIVE_VELOCITY_UNITS_PER_ROTATIONS_PER_SECOND =
        TALON_FX_NATIVE_SENSOR_UNITS_PER_ROTATION * 10;
    public static final double SWERVE_MODULE_DRIVE_GEAR_RATIO = 8.14 / 1;

    public static final double DRIVE_SPEED_TO_NATIVE_VELOCITY = 
        TALON_FX_NATIVE_VELOCITY_UNITS_PER_ROTATIONS_PER_SECOND *
        SWERVE_MODULE_DRIVE_GEAR_RATIO *
        WHEEL_DIAMETER * Math.PI;  // Converts from sensor position to velocity

    private DriveConstants() {
        throw new AssertionError();
    }
}
