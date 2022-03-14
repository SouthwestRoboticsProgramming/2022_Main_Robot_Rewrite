package com.swrobotics.pathfinding.library;

import com.swrobotics.pathfinding.library.collider.Collider;
import com.swrobotics.pathfinding.library.collider.Scene;

public final class Grid {
    private final boolean[][] environment;
    private final boolean[][] overlay;
    private final int width;
    private final int height;

    public Grid(int width, int height) {
        this.width = width;
        this.height = height;
        environment = new boolean[width][height];
        overlay = new boolean[width][height];
    }

    public void buildEnvironmentFromScene(Scene scene, Collider agent) {
        for (int x = 0; x < width; x++) {
            for (int y = 0; y < height; y++) {
                environment[x][y] = scene.testCollision(agent, x + 0.5, y + 0.5);
            }
        }
    }

    public boolean get(int x, int y) {
        if (x < 0 || y < 0 || x >= width || y >= height)
            return false;

        return environment[x][y] || overlay[x][y];
    }

    public boolean getOverlay(int x, int y) {
        return overlay[x][y];
    }

    public void setOverlay(int x, int y, boolean colliding) {
        overlay[x][y] = colliding;
    }

    public int getWidth() {
        return width;
    }

    public int getHeight() {
        return height;
    }
}
