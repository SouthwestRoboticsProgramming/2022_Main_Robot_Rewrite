package com.swrobotics.bert.constants;

import com.swrobotics.bert.shuffle.ShuffleBoard;
import com.swrobotics.bert.shuffle.Tunable;
import com.swrobotics.bert.shuffle.TunableBoolean;
import com.swrobotics.bert.shuffle.TunableDouble;
import com.swrobotics.bert.shuffle.TuneGroup;

public final class ShooterConstants {

    // TODO: REMOVE
    private static final TuneGroup SHOOTER_TUNE = new TuneGroup("Tuning (Temp)", ShuffleBoard.shooterTab);
        public static final TunableDouble HOOD_POSITION = SHOOTER_TUNE.getDouble("Hood Position", 0);
        public static final TunableDouble FLYWHEEL_RPM = SHOOTER_TUNE.getDouble("Flywheel RPM", 0);


    private static final TuneGroup POWER = new TuneGroup("Speeds", ShuffleBoard.shooterTab);
        public static final TunableBoolean AIM_HIGH_GOAL = POWER.getBoolean("Aim at high goal", true);
    
    private static final TuneGroup INDEX = new TuneGroup("Index", ShuffleBoard.shooterTab);
        public static final TunableDouble INDEX_IDLE_SPEED = INDEX.getDouble("Idle Speed", -750);
        public static final TunableDouble INDEX_KICKBACK_SPEED = INDEX.getDouble("Kickback Speed", 1200);
        public static final TunableDouble INDEX_KICKBACK_TIME = INDEX.getDouble("Kickback Time", 0.25);
        public static final TunableDouble INDEX_SHOOT_SPEED = INDEX.getDouble("Shoot Speed", -1200);
        public static final TunableDouble INDEX_SHOOT_TIME = INDEX.getDouble("Shoot Time", 1);

        public static final TunableDouble INDEX_KP = INDEX.getDouble("kP", 0.017);
        public static final TunableDouble INDEX_KI = INDEX.getDouble("kI", 0);
        public static final TunableDouble INDEX_KD = INDEX.getDouble("kD", 0);
        public static final TunableDouble INDEX_KF = INDEX.getDouble("kF", 0);

    public static final int BALL_DETECTOR_ID = 9;
    public static final int INDEX_ID = 33;

    private static final TuneGroup FLYWHEEL = new TuneGroup("Flywheel", ShuffleBoard.shooterTab);
        public static final TunableDouble FLYWHEEL_IDLE_SPEED = FLYWHEEL.getDouble("Idle Speed", -750);

        public static final TunableDouble FLYWHEEL_KP = FLYWHEEL.getDouble("kP", 0.017);
        public static final TunableDouble FLYWHEEL_KI = FLYWHEEL.getDouble("kI", 0);
        public static final TunableDouble FLYWHEEL_KD = FLYWHEEL.getDouble("kD", 0);
        public static final TunableDouble FLYWHEEL_KF = FLYWHEEL.getDouble("kF", 0);

    public static final int FLYWHEEL_ID = 30;
    public static final double FLYWHEEL_GEAR_RATIO = 1; // 1:1

    private static final TuneGroup HOOD = new TuneGroup("HOOD", ShuffleBoard.shooterTab);
        public static final TunableDouble HOOD_KP = HOOD.getDouble("kP", 0.017);
        public static final TunableDouble HOOD_KI = HOOD.getDouble("kI", 0);
        public static final TunableDouble HOOD_KD = HOOD.getDouble("kD", 0);
        public static final TunableDouble HOOD_KF = HOOD.getDouble("kF", 0);

        public static final TunableDouble HOOD_LOWEST_TICKS = HOOD.getDouble("Lowest Ticks", 15); // FIXME
        public static final TunableDouble HOOD_HIGHEST_TICKS = HOOD.getDouble("Highest Ticks", 1000); // FIXME

        public static final TunableDouble HOOD_CALIBRATE_SPEED = HOOD.getDouble("Calibration Speed", -0.2);

    public static final int HOOD_ID = 41;
    public static final int HOOD_LIMIT_ID = 0;

    public static final int HOOD_ENCODER_ID_1 = 8;
    public static final int HOOD_ENCODER_ID_2 = 7;

    private ShooterConstants() {
        throw new AssertionError();
    }
}
