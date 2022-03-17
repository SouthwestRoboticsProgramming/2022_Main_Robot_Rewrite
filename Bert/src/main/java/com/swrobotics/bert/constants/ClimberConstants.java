package com.swrobotics.bert.constants;

public final class ClimberConstants {
    public static final double TELESCOPING_MIN_TICKS = 0;
    public static final double TELESCOPING_MAX_TICKS = 47.07;

    public static final double TELESCOPING_PID_KP = 0;
    public static final double TELESCOPING_PID_KI = 0;
    public static final double TELESCOPING_PID_KD = 0;
    public static final double TELESCOPING_PID_KF = 0;

    public static final double TELESCOPING_PID_LOADED_KP = 0;
    public static final double TELESCOPING_PID_LOADED_KI = 0;
    public static final double TELESCOPING_PID_LOADED_KD = 0;
    public static final double TELESCOPING_PID_LOADED_KF = 0;

    public static final int TELESCOPING_LEFT_MOTOR_ID_ONE = 21;
    public static final int TELESCOPING_LEFT_MOTOR_ID_TWO = 22;
    public static final int TELESCOPING_RIGHT_MOTOR_ID_ONE = 24;
    public static final int TELESCOPING_RIGHT_MOTOR_ID_TWO = 25;

    private ClimberConstants() {
        throw new AssertionError();
    }
}
