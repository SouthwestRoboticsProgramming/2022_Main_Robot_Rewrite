package com.swrobotics.bert.util;

public class Utils {
    public static double clamp(double value, double min, double max) {
        if (value < min) return min;
        if (value > max) return max;
        return value;
    }

    public static double map(double value, double minA, double maxA, double minB, double maxB) {
        return (value - minA) / (maxA - minA) * (maxB - minB) + minB;
    }

    public static double normalizeRadians(double angle) {
        return -Math.PI + ((Math.PI * 2 + ((angle + Math.PI) % (Math.PI * 2))) % (Math.PI * 2));
    }
}
