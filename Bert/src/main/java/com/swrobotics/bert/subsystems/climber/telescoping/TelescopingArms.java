package com.swrobotics.bert.subsystems.climber.telescoping;

import static com.swrobotics.bert.constants.ClimberConstants.*;

public final class TelescopingArms {
    private final TelescopingArm left;
    private final TelescopingArm right;

    public TelescopingArms() {
        left = new TelescopingArm(
            TELESCOPING_LEFT_MOTOR_ID_ONE,
            TELESCOPING_LEFT_MOTOR_ID_TWO,
            true
        );
        right = new TelescopingArm(
            TELESCOPING_RIGHT_MOTOR_ID_ONE,
            TELESCOPING_RIGHT_MOTOR_ID_TWO,
            false
        );
    }

    public void setTargetDistancePercent(double distance) {
        left.setTargetDistancePercent(distance);
        right.setTargetDistancePercent(distance);
    }

    public void setLoaded(boolean loaded) {
        left.setLoaded(loaded);
        right.setLoaded(loaded);
    }

    public void update() {
        left.update();
        right.update();
    }

    @Override
    public String toString() {
        return "TelescopingArms{"
            + "left=" + left + ","
            + "right=" + right
            + "}";
    }
}
