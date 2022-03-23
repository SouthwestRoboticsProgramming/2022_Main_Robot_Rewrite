package com.swrobotics.pathfinding.library.grid;

import com.swrobotics.pathfinding.library.Point;
import com.swrobotics.pathfinding.library.collider.CircleCollider;

import java.util.HashSet;
import java.util.Iterator;
import java.util.Set;

public class PointCollisionGrid implements Grid {
    private final double radiusSquared;
    private final Set<Point> points;
    private final int width;
    private final int height;

    public PointCollisionGrid(CircleCollider agent, int width, int height) {
        radiusSquared = agent.getRadius() * agent.getRadius();
        points = new HashSet<>();
        this.width = width;
        this.height = height;
    }

    public void addPoint(Point point) {
        points.add(point);
    }

    public void removePoint(Point point) {
        points.remove(point);
    }

    @Override
    public boolean get(int x, int y) {
        for (Point point : points) {
            double dx = x - point.getX();
            double dy = y - point.getY();

            double distSq = dx * dx + dy * dy;
            if (distSq <= radiusSquared) {
                return true;
            }
        }

        return false;
    }

    @Override
    public int getWidth() {
        return width;
    }

    @Override
    public int getHeight() {
        return height;
    }
}
