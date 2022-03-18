package com.swrobotics.bert.constants;

import com.swrobotics.bert.shuffle.ShuffleBoard;
import com.swrobotics.bert.shuffle.TunableDouble;
import com.swrobotics.bert.shuffle.TunableInt;
import com.swrobotics.bert.shuffle.TuneGroup;
import com.swrobotics.bert.subsystems.drive.SwerveModuleInfo;

public final class DriveConstants {
    private static final TuneGroup MODULES = new TuneGroup("Modules", ShuffleBoard.driveTab);
        public static final TunableInt FRONT_LEFT_MODULE = MODULES.getInt("Front Left", 1);
        public static final TunableInt FRONT_RIGHT_MODULE = MODULES.getInt("Front Right", 2);
        public static final TunableInt BACK_RIGHT_MODULE = MODULES.getInt("Back Right", 3);
        public static final TunableInt BACK_LEFT_MODULE = MODULES.getInt("Back Left", 4);

    private static final TuneGroup CONTROLS = new TuneGroup("Controls", ShuffleBoard.driveTab);
        public static final TunableDouble MAX_VELOCITY = CONTROLS.getDouble("Max Velocity", 0.05); // Meters per Seconda
        public static final TunableDouble MAX_TURN_VELOCITY = CONTROLS.getDouble("Max Turn Velocity", 0.05); // Radians per Second

    private static final TuneGroup PID = new TuneGroup("PID", ShuffleBoard.driveTab);
        public static final TunableDouble DRIVE_KP = PID.getDouble("Drive KP", 0.05);
        public static final TunableDouble DRIVE_KI = PID.getDouble("Drive KI", 0.0);
        public static final TunableDouble DRIVE_KD = PID.getDouble("Drive KD", 0.001);
        public static final TunableDouble DRIVE_KF = PID.getDouble("Drive KF", 0.05);

        public static final TunableDouble TURN_KP = PID.getDouble("Turn KP", 0.009);
        public static final TunableDouble TURN_KI = PID.getDouble("Turn KI", 0.0);
        public static final TunableDouble TURN_KD = PID.getDouble("Turn KD", 0.0001);

    /* Hardware*/
    public static final double WHEEL_DIAMETER = 0.10;  // Meters
    public static final int TALON_FX_NATIVE_SENSOR_UNITS_PER_ROTATION = 2048;
    private static final int TALON_FX_NATIVE_VELOCITY_UNITS_PER_ROTATIONS_PER_SECOND =
        TALON_FX_NATIVE_SENSOR_UNITS_PER_ROTATION / 10;
    public static final double SWERVE_MODULE_DRIVE_GEAR_RATIO = 8.14 / 1;

    public static final double DRIVE_SPEED_TO_NATIVE_VELOCITY = 
        TALON_FX_NATIVE_VELOCITY_UNITS_PER_ROTATIONS_PER_SECOND *
        SWERVE_MODULE_DRIVE_GEAR_RATIO /
        (WHEEL_DIAMETER * Math.PI);  // Converts from sensor position to velocity

    public static final double WHEEL_SPACING = 0.4699; // Meters
    public static final double MAX_ATTAINABLE_WHEEL_SPEED = 3.6; // m/s  with 0.5 m/s of padding

    // Device IDs and offsets
    public static final SwerveModuleInfo[] SWERVE_INFO = {
        new SwerveModuleInfo(1 /*Drive*/, 3 /*Cancoder*/, -134.297 /*Offset*/), // Module 1
        new SwerveModuleInfo(4 /*Drive*/, 6 /*Cancoder*/, -129.375 /*Offset*/), // Module 2
        new SwerveModuleInfo(7 /*Drive*/, 9 /*Cancoder*/, -77.344 /*Offset*/), // Module 3
        new SwerveModuleInfo(10 /*Drive*/, 12 /*Cancoder*/, 89.318 /*Offset*/), // Module 4
        new SwerveModuleInfo(13 /*Drive*/, 15 /*Cancoder*/, 0 /*Offset*/), // Module 5
    };

    public static final int TURN_ID_FRONT_LEFT = 2;
    public static final int TURN_ID_FRONT_RIGHT = 5;
    public static final int TURN_ID_BACK_RIGHT = 8;
    public static final int TURN_ID_BACK_LEFT = 11;

    private DriveConstants() {
        throw new AssertionError();
    }
}
