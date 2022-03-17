package com.swrobotics.bert.subsystems.climber;

public final class ClimberState {
    private final double telescopingDistance;
    private final double rotatingAngle;
    private final boolean loaded;

    public ClimberState(double telescopingDistance, double rotatingAngle, boolean loaded) {
        this.telescopingDistance = telescopingDistance;
        this.rotatingAngle = rotatingAngle;
        this.loaded = loaded;
    }

    public double getTelescopingDistance() {
        return telescopingDistance;
    }

    public double getRotatingAngle() {
        return rotatingAngle;
    }

    public boolean isLoaded() {
        return loaded;
    }

    @Override
    public String toString() {
        return "ClimberState{"
            + "telescoping=" + telescopingDistance + ","
            + "rotating=" + rotatingAngle + ","
            + "loaded=" + loaded
            + "}";
    }
}
