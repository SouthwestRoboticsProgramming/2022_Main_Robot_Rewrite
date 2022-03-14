package com.swrobotics.bert.util;

public class Utils {
    public static double map(double value, double min, double max, double newMin, double newMax) {
        return (value - min) / (max - min) * (newMax - newMin) + newMin;
    }


    private Utils() {
        throw new AssertionError();
    }
}
