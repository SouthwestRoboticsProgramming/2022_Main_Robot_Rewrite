package com.swrobotics.pathfinding.task;

import com.swrobotics.pathfinding.library.Point;
import com.swrobotics.pathfinding.library.collider.CircleCollider;
import com.swrobotics.pathfinding.library.grid.PointCollisionGrid;

public class LidarPointCollisionGrid extends PointCollisionGrid {
    private final int[][] cellCounts;

    public LidarPointCollisionGrid(CircleCollider agent, int width, int height) {
        super(agent, width, height);

        cellCounts = new int[width][height];
    }

    private boolean checkBounds(int x, int y) {
        return x < 0 || y < 0 || x >= getWidth() || y >= getHeight();
    }

    public void addLidarPoint(int x, int y) {
        if (checkBounds(x, y)) return;

        if (cellCounts[x][y]++ == 0) {
            addPoint(new Point(x, y));
        }
    }

    public void removeLidarPoint(int x, int y) {
        if (checkBounds(x, y)) return;

        if (--cellCounts[x][y] == 0) {
            removePoint(new Point(x, y));
        }
    }
}
