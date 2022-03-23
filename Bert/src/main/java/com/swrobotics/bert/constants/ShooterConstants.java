package com.swrobotics.bert.constants;

import com.swrobotics.bert.shuffle.ShuffleBoard;
import com.swrobotics.bert.shuffle.TunableDouble;
import com.swrobotics.bert.shuffle.TuneGroup;

public final class ShooterConstants {
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
        public static final TunableDouble FLYWHEEL_KICKBACK_SPEED = FLYWHEEL.getDouble("Kickback Speed", 1200);
        public static final TunableDouble FLYWHEEL_KICKBACK_TIME = FLYWHEEL.getDouble("Kickback Time", 0.25);
        public static final TunableDouble FLYWHEEL_SHOOT_SPEED = FLYWHEEL.getDouble("Shoot Speed", -1200);
        public static final TunableDouble FLYWHEEL_SHOOT_TIME = FLYWHEEL.getDouble("Shoot Time", 1);

        public static final TunableDouble FLYWHEEL_KP = FLYWHEEL.getDouble("kP", 0.017);
        public static final TunableDouble FLYWHEEL_KI = FLYWHEEL.getDouble("kI", 0);
        public static final TunableDouble FLYWHEEL_KD = FLYWHEEL.getDouble("kD", 0);
        public static final TunableDouble FLYWHEEL_KF = FLYWHEEL.getDouble("kF", 0);

    public static final int FLYWHEEL_ID = 30;
    public static final double FLYWHEEL_GEAR_RATIO = 2;

    private ShooterConstants() {
        throw new AssertionError();
    }
}
