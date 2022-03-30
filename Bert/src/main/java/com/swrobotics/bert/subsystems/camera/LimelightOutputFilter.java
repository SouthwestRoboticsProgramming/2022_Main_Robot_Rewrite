package com.swrobotics.bert.subsystems.camera;

public final class LimelightOutputFilter {
    private static final double INVALID_MEASUREMENT = 0.0;

    private final int span;
    private final double[] history;

    private int pointer;
    private double savedValue;

    public LimelightOutputFilter(int span) {
        this.span = span;
        history = new double[span];
        pointer = 0;
    }

    private double calcAverage() {
        double sum = 0;
        for (double v : history) {
            sum += v;
        }

        return sum / span;
    }

    public double filter(double measurement) {
        if (measurement == INVALID_MEASUREMENT) {
            measurement = savedValue;
        } else {
            savedValue = measurement;
        }

        history[pointer++] = measurement;
        if (pointer >= span) {
            pointer = 0;
        }

        return calcAverage();
    }
}
