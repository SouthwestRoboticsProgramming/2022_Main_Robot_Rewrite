package com.swrobotics.pathfinding;

import processing.core.PApplet;

import java.util.List;

public final class PathfinderTest extends PApplet {
    private static final int WIDTH = 50;
    private static final int HEIGHT = 40;
    private static final int CELL_SIZE = 20;

    private boolean[][] grid;
    private Pathfinder pathfinder;
    private Point start, end;
    private boolean dragMode;

    @Override
    public void settings() {
        size(WIDTH * CELL_SIZE, HEIGHT * CELL_SIZE, P2D);
    }

    @Override
    public void setup() {
        ellipseMode(CENTER);

        grid = new boolean[WIDTH][HEIGHT];
        pathfinder = new Pathfinder();
        pathfinder.setGrid(grid, WIDTH, HEIGHT);

        start = new Point(1, 1);
        end = new Point(10, 10);
    }

    @Override
    public void draw() {
        stroke(0);
        for (int x = 0; x < WIDTH; x++) {
            for (int y = 0; y < HEIGHT; y++) {
                if (grid[x][y])
                    fill(255, 0, 0);
                else
                    fill(255);

                rect(x * CELL_SIZE, y * CELL_SIZE, CELL_SIZE, CELL_SIZE);
            }
        }

        List<Point> path = pathfinder.findPath(start, end);

        if (path != null) {
            stroke(0, 0, 255);
            beginShape(LINE_STRIP);
            for (Point point : path) {
                vertex((point.getX() + 0.5f) * CELL_SIZE, (point.getY() + 0.5f) * CELL_SIZE);
            }
            endShape();
        }

        stroke(0);
        fill(255, 255, 0);
        ellipse((start.getX() + 0.5f) * CELL_SIZE, (start.getY() + 0.5f) * CELL_SIZE, CELL_SIZE / 2f, CELL_SIZE / 2f);

        fill(0, 255, 0);
        ellipse((end.getX() + 0.5f) * CELL_SIZE, (end.getY() + 0.5f) * CELL_SIZE, CELL_SIZE / 2f, CELL_SIZE / 2f);
    }

    @Override
    public void mousePressed() {
        int x = mouseX / CELL_SIZE;
        int y = mouseY / CELL_SIZE;
        if (mouseButton == LEFT) {
            start = new Point(x, y);
        } else if (mouseButton == RIGHT) {
            end = new Point(x, y);
        } else if (mouseButton == CENTER) {
            grid[x][y] = dragMode = !grid[x][y];
        }
    }

    @Override
    public void mouseDragged() {
        if (mouseX < 0 || mouseY < 0 || mouseX >= width || mouseY >= height)
            return;

        int x = mouseX / CELL_SIZE;
        int y = mouseY / CELL_SIZE;
        if (mouseButton == LEFT) {
            start = new Point(x, y);
        } else if (mouseButton == RIGHT) {
            end = new Point(x, y);
        } else if (mouseButton == CENTER) {
            grid[x][y] = dragMode;
        }
    }

    public static void main(String[] args) {
        PApplet.main(PathfinderTest.class);
    }
}
