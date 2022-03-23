package com.swrobotics.pathfinding.task;

public final class LidarPoint {
    private final int x;
    private final int y;
    private final long timestamp;

    public LidarPoint(int x, int y, long timestamp) {
        this.x = x;
        this.y = y;
        this.timestamp = timestamp;
    }

    public int getX() {
        return x;
    }

    public int getY() {
        return y;
    }

    public long getTimestamp() {
        return timestamp;
    }
}
