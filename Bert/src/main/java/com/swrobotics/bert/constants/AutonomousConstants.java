package com.swrobotics.bert.constants;

import com.swrobotics.bert.shuffle.ShuffleBoard;
import com.swrobotics.bert.shuffle.TunableDouble;
import com.swrobotics.bert.shuffle.TuneGroup;

public final class AutonomousConstants {
    private static final TuneGroup PID = new TuneGroup("PID", ShuffleBoard.autoTab);
        public static final TunableDouble PATH_KP = PID.getDouble("kP", 0);
        public static final TunableDouble PATH_KI = PID.getDouble("kI", 0);
        public static final TunableDouble PATH_KD = PID.getDouble("kD", 0);

    private static final TuneGroup THRESHOLDS = new TuneGroup("PID", ShuffleBoard.autoTab);
        public static final TunableDouble TARGET_THRESHOLD_DIST = THRESHOLDS.getDouble("Target Threshold Dist (m)", 0.1);
        public static final TunableDouble TURN_THRESHOLD = THRESHOLDS.getDouble("Turn Threshold (deg)", 10);

    public AutonomousConstants() {
        throw new AssertionError();
    }
}
