package com.swrobotics.bert.constants;

import com.swrobotics.bert.shuffle.ShuffleBoard;
import com.swrobotics.bert.shuffle.TunableDouble;
import com.swrobotics.bert.shuffle.TunableDoubleArray;
import com.swrobotics.bert.shuffle.TuneGroup;

public final class ClimberConstants {
    private static final TuneGroup TELESCOPING = new TuneGroup("Telescoping", ShuffleBoard.climberTab);
        public static final TunableDouble TELESCOPING_MIN_TICKS = TELESCOPING.getDouble("Min Ticks", 0);
        public static final TunableDouble TELESCOPING_MAX_TICKS = TELESCOPING.getDouble("Max Ticks", 60.0);
        public static final TunableDouble TELESCOPING_TOLERANCE = TELESCOPING.getDouble("Tele Tolerance", 0.05);
        public static final TunableDouble TELESCOPING_MAX_LOADED_PERCENT = TELESCOPING.getDouble("Tele Max Loaded Output", 0.8);
        public static final TunableDouble TELESCOPING_MAX_UNLOADED_PERCENT = TELESCOPING.getDouble("Tele Max Unloaded Output", 0.5);
        public static final TunableDouble TELESCOPING_LOADED_PERCENT_OUT = TELESCOPING.getDouble("Tele Loaded Percent Out", -0.65);
        public static final TunableDouble TELESCOPING_LOADED_PID_ENGAGE_PERC = TELESCOPING.getDouble("Tele Loaded Engague Percent", 0.05);

        public static final TunableDouble TELESCOPING_PID_KP = TELESCOPING.getDouble("kP", 0.2);
        public static final TunableDouble TELESCOPING_PID_KI = TELESCOPING.getDouble("kI", 0.001);
        public static final TunableDouble TELESCOPING_PID_KD = TELESCOPING.getDouble("kD", 0.0001);
        public static final TunableDouble TELESCOPING_PID_KF = TELESCOPING.getDouble("kF", 0);

        public static final TunableDouble TELESCOPING_PID_LOADED_KP = TELESCOPING.getDouble("Loaded kP", 0.2);
        public static final TunableDouble TELESCOPING_PID_LOADED_KI = TELESCOPING.getDouble("Loaded kI", 0.0001);
        public static final TunableDouble TELESCOPING_PID_LOADED_KD = TELESCOPING.getDouble("Loaded kD", 0.0001);
        public static final TunableDouble TELESCOPING_PID_LOADED_KF = TELESCOPING.getDouble("Loaded kF", -0.4);

    public static final int TELESCOPING_LEFT_MOTOR_ID_ONE = 21;
    public static final int TELESCOPING_LEFT_MOTOR_ID_TWO = 22;
    public static final int TELESCOPING_RIGHT_MOTOR_ID_ONE = 24;
    public static final int TELESCOPING_RIGHT_MOTOR_ID_TWO = 25;

    private static final TuneGroup ROTATING = new TuneGroup("Rotating", ShuffleBoard.climberTab);
        public static final TunableDouble ROTATING_MAX_PERCENT = ROTATING.getDouble("Rotating Max Output", 0.3);
        public static final TunableDouble ROTATING_TOLERANCE = ROTATING.getDouble("Rot Tolerance", 2.0);

        public static final TunableDouble ROTATING_PID_KP = ROTATING.getDouble("kP", 0.15);
        public static final TunableDouble ROTATING_PID_KI = ROTATING.getDouble("kI", 0.004);
        public static final TunableDouble ROTATING_PID_KD = ROTATING.getDouble("kD", 0.001);

        public static final TunableDouble ROTATING_PID_LOADED_KP = ROTATING.getDouble("Loaded kP", 0.15);
        public static final TunableDouble ROTATING_PID_LOADED_KI = ROTATING.getDouble("Loaded kI", 0.001);
        public static final TunableDouble ROTATING_PID_LOADED_KD = ROTATING.getDouble("Loaded kD", 0.001);

        public static final TunableDouble ROTATING_ARM_ENCODER_DIFF_SHUTOFF_THRESHOLD = ROTATING.getDouble("Encoder Shutoff", 10); // FIXME

    private static final TuneGroup TEST = new TuneGroup("Test", ShuffleBoard.climberTab);
        public static final TunableDoubleArray TEST_ARRAY = TEST.getDoubleArray("Test Array", 1, 2, 3, 4, 5, 6, 7, 8, 9);
    
    public static final int ROTATING_LEFT_MOTOR_ID = 20;
    public static final int ROTATING_RIGHT_MOTOR_ID = 23;

    public static final double ROTATING_ARM_LENGTH = 15.0017;
    public static final double ROTATING_BASE_LENGTH = 6.76;
    public static final double ROTATING_ROTS_PER_INCH = 11.0;
    public static final double ROTATING_STARTING_LENGTH = 12.38;




    private static final TuneGroup STEPS = new TuneGroup("Climber Steps v2", ShuffleBoard.climberTab);
        public static final TunableDouble CLIMB_STEP_1_ROT = STEPS.getDouble("1-Rot: Base Angle", 110);
        public static final TunableDouble CLIMB_STEP_1_TELE = STEPS.getDouble("1-Tel: Base Tele", 0.05);
        public static final TunableDouble CLIMB_STEP_1_5_TELE = STEPS.getDouble("1.5-Tel: Extend Tele", 1.0);
        public static final TunableDouble CLIMB_STEP_2_TELE = STEPS.getDouble("2-Tel: Retract Height", 0.04);
        public static final TunableDouble CLIMB_STEP_3_ROT = STEPS.getDouble("3-Rot: Handoff Angle", 94);
        public static final TunableDouble CLIMB_STEP_4_ROT = STEPS.getDouble("4-Rot: Aim At 3 Angle", 57);
        public static final TunableDouble CLIMB_STEP_4_5_GYRO = STEPS.getDouble("4.5-Gyro: Pressure Angle", -34);
        public static final TunableDouble CLIMB_STEP_4_TELE = STEPS.getDouble("4-Tel: Release Height", 0.4);
        public static final TunableDouble CLIMB_STEP_5_TELE = STEPS.getDouble("5-Tel: Extend to 3", 0.9);
        public static final TunableDouble CLIMB_STEP_6_ROT = STEPS.getDouble("6-Rot: Pressure On 3", 69.0);

    private ClimberConstants() {
        throw new AssertionError();
    }
}
