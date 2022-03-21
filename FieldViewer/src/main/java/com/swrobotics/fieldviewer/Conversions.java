package com.swrobotics.fieldviewer;

public final class Conversions {
    public static float feetToMeters(float feet) {
        return feet * 0.3048f;
    }

    public static float inchesToMeters(float inches) {
        return feetToMeters(inches / 12.0f);
    }

    private Conversions() {
        throw new AssertionError();
    }
}
