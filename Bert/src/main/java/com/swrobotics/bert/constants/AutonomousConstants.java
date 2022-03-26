package com.swrobotics.bert.constants;

import com.swrobotics.bert.shuffle.ShuffleBoard;
import com.swrobotics.bert.shuffle.TunableDouble;
import com.swrobotics.bert.shuffle.TuneGroup;

public class AutonomousConstants {
    private static final TuneGroup PID = new TuneGroup("PID", ShuffleBoard.autoTab);
        public static final TunableDouble PATH_KP = PID.getDouble("kP", 0);
        public static final TunableDouble PATH_KI = PID.getDouble("kI", 0);
        public static final TunableDouble PATH_KD = PID.getDouble("kD", 0);

    public AutonomousConstants() {
        throw new AssertionError();
    }
}
