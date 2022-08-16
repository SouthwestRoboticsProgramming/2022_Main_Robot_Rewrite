package com.swrobotics.bert.subsystems.climber.telescoping;

import static com.swrobotics.bert.constants.ClimberConstants.*;

public final class TelescopingArms {
    private final TelescopingArm left;
    private final TelescopingArm right;

    public TelescopingArms() {
        left = new TelescopingArm(
            TELESCOPING_LEFT_MOTOR_ID_ONE,
            TELESCOPING_LEFT_MOTOR_ID_TWO,
            true,
            "Left Tele"
        );
        right = new TelescopingArm(
            TELESCOPING_RIGHT_MOTOR_ID_ONE,
            TELESCOPING_RIGHT_MOTOR_ID_TWO,
            false,
            "Right Tele"
        );
    }

    public void setTargetDistancePercent(double distance) {
        left.setTargetDistancePercent(distance);
        right.setTargetDistancePercent(distance);
    }

    public void manualMove(double percentOutput) {
        left.manualMove(percentOutput);
        right.manualMove(percentOutput);
    }

    public void setLoaded(boolean loaded) {
        left.setLoaded(loaded);
        right.setLoaded(loaded);
    }

    public boolean isInTolarence() {
        return left.isInTolarence() && right.isInTolarence();
    }

    public void zero() {
        left.zero();
        right.zero();
    }

    public void stopMotors() {
        left.stopMotor();
        right.stopMotor();
    }

    public void update() {
        left.update();
        right.update();
    }

    public void stop() {
        left.stop();
        right.stop();
    }

    public void disable() {
        left.disable();
        right.disable();
    }

    @Override
    public String toString() {
        return "TelescopingArms{"
            + "left=" + left + ","
            + "right=" + right
            + "}";
    }
}
