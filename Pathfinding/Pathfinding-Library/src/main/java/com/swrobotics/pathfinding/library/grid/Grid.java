package com.swrobotics.pathfinding.library.grid;

public interface Grid {
    // Returns whether a point on the grid is not passable
    boolean get(int x, int y);

    int getWidth();
    int getHeight();
}
