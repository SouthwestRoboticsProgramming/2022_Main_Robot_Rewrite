package com.swrobotics.bert.constants;

import static com.swrobotics.bert.constants.Constants.PERIODIC_PER_SECOND;

public final class CameraTurretConstants {
    public static final int SERVO_ID = 1;

    public static final double SERVO_FULL_SWEEP_TIME = 0.5; // Time for servo to turn from 0 to 180 in seconds
    public static final double SERVO_TURN_PER_PERIODIC = 180.0 / PERIODIC_PER_SECOND / SERVO_FULL_SWEEP_TIME;

    private CameraTurretConstants() {
        throw new AssertionError();
    }
}
