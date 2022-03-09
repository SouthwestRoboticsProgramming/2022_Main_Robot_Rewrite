package com.swrobotics.bert.constants;

import com.swrobotics.bert.subsystems.drive.SwerveModuleInfo;

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

    /* Hardware */
    public static final double WHEEL_SPACING = 0.4699; // Meters
    public static final double MAX_ATTAINABLE_WHEEL_SPEED = 13.0; // m/s  with 0.5 m/s of padding

    // Device IDs and offsets
    public static final SwerveModuleInfo[] SWERVE_INFO = {
        new SwerveModuleInfo(1 /*Drive*/, 2 /*Cancoder*/, 0 /*Offset*/), // Module 1
        new SwerveModuleInfo(4 /*Drive*/, 5 /*Cancoder*/, 0 /*Offset*/), // Module 2
        new SwerveModuleInfo(7 /*Drive*/, 8 /*Cancoder*/, 0 /*Offset*/), // Module 3
        new SwerveModuleInfo(10 /*Drive*/, 11 /*Cancoder*/, 0 /*Offset*/), // Module 4
        new SwerveModuleInfo(13 /*Drive*/, 14 /*Cancoder*/, 0 /*Offset*/), // Module 5
    };

    public static final int TURN_ID_FRONT_LEFT = 3;
    public static final int TURN_ID_FRONT_RIGHT = 6;
    public static final int TURN_ID_BACK_RIGHT = 9;
    public static final int TURN_ID_BACK_LEFT = 12;

    private DriveConstants() {
        throw new AssertionError();
    }
}
