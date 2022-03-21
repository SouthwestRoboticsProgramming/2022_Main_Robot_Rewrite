package com.swrobotics.bert.util;

public class Utils {
    public static double clamp(double value, double min, double max) {
        if (value < min) return min;
        if (value > max) return max;
        return value;
    }

    public static double map(double value, double minOld, double maxOld, double minNew, double maxNew) {
        return (value - minOld) / (maxOld - minOld) * (maxNew - minNew) + minNew;
    }

    public static double normalizeRadians(double angle) {
        return -Math.PI + ((Math.PI * 2 + ((angle + Math.PI) % (Math.PI * 2))) % (Math.PI * 2));
    }
}
