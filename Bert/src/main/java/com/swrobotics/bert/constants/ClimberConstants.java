package com.swrobotics.bert.constants;

import com.swrobotics.bert.shuffle.ShuffleBoard;
import com.swrobotics.bert.shuffle.TunableDouble;
import com.swrobotics.bert.shuffle.TuneGroup;

public final class ClimberConstants {
    private static final TuneGroup TELESCOPING = new TuneGroup("Telescoping", ShuffleBoard.climberTab);
        public static final TunableDouble TELESCOPING_MIN_TICKS = TELESCOPING.getDouble("Min Ticks", 0);
        public static final TunableDouble TELESCOPING_MAX_TICKS = TELESCOPING.getDouble("Max Ticks", 47.07);

        public static final TunableDouble TELESCOPING_PID_KP = TELESCOPING.getDouble("kP", 0.2);
        public static final TunableDouble TELESCOPING_PID_KI = TELESCOPING.getDouble("kI", 0.001);
        public static final TunableDouble TELESCOPING_PID_KD = TELESCOPING.getDouble("kD", 0.0001);
        public static final TunableDouble TELESCOPING_PID_KF = TELESCOPING.getDouble("kF", 0);

        public static final TunableDouble TELESCOPING_PID_LOADED_KP = TELESCOPING.getDouble("Loaded kP", 2);
        public static final TunableDouble TELESCOPING_PID_LOADED_KI = TELESCOPING.getDouble("Loaded kI", 0.001);
        public static final TunableDouble TELESCOPING_PID_LOADED_KD = TELESCOPING.getDouble("Loaded kD", 0.0001);
        public static final TunableDouble TELESCOPING_PID_LOADED_KF = TELESCOPING.getDouble("Loaded kF", -0.1);

    public static final int TELESCOPING_LEFT_MOTOR_ID_ONE = 21;
    public static final int TELESCOPING_LEFT_MOTOR_ID_TWO = 22;
    public static final int TELESCOPING_RIGHT_MOTOR_ID_ONE = 24;
    public static final int TELESCOPING_RIGHT_MOTOR_ID_TWO = 25;

    private ClimberConstants() {
        throw new AssertionError();
    }
}