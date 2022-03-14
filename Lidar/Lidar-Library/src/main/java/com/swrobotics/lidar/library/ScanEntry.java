package com.swrobotics.lidar.library;

public final class ScanEntry {
    private final int quality;
    private final double angle;
    private final double distance;

    public ScanEntry(int quality, double angle, double distance) {
        this.quality = quality;
        this.angle = angle;
        this.distance = distance;
    }

    public int getQuality() {
        return quality;
    }

    public double getAngle() {
        return angle;
    }

    public double getDistance() {
        return distance;
    }

    @Override
    public String toString() {
        return "ScanEntry{" +
                "quality=" + quality +
                ", angle=" + angle +
                ", distance=" + distance +
                '}';
    }
}
