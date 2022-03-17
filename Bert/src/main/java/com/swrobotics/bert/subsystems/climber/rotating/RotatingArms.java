package com.swrobotics.bert.subsystems.climber.rotating;

public final class RotatingArms {
    private final RotatingArm left;
    private final RotatingArm right;

    public RotatingArms() {
        left = new RotatingArm();
        right = new RotatingArm();
    }

    public void setTargetAngleDegrees(double angle) {
        left.setTargetAngleDegrees(angle);
        right.setTargetAngleDegrees(angle);
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
        return "RotatingArms{"
            + "left=" + left + ","
            + "right=" + right
            + "}";
    }
}
