package com.swrobotics.bert.constants;

import com.swrobotics.bert.shuffle.ShuffleBoard;
import com.swrobotics.bert.shuffle.TunableDouble;
import com.swrobotics.bert.shuffle.TuneGroup;

public final class IntakeConstants {
    private static final TuneGroup PID = new TuneGroup("PID", ShuffleBoard.intakeTab);
        public static final TunableDouble INTAKE_KP = PID.getDouble("kP", 0.05);
        public static final TunableDouble INTAKE_KI = PID.getDouble("kI", 0);
        public static final TunableDouble INTAKE_KD = PID.getDouble("kD", 0);
        public static final TunableDouble INTAKE_KF = PID.getDouble("kF", 0);

    private static final TuneGroup TUNE = new TuneGroup("Tune", ShuffleBoard.intakeTab);
        public static final TunableDouble INTAKE_SPEED = TUNE.getDouble("Intake Speed", 10); // RPM

    public static final int INTAKE_MOTOR_ID = 40;

    private IntakeConstants() {
        throw new AssertionError();
    }
}
