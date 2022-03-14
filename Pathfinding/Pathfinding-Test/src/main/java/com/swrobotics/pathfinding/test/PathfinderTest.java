package com.swrobotics.pathfinding.test;

import com.swrobotics.pathfinding.library.Grid;
import com.swrobotics.pathfinding.library.PathOptimizer;
import com.swrobotics.pathfinding.library.Pathfinder;
import com.swrobotics.pathfinding.library.Point;
import com.swrobotics.pathfinding.library.collider.CircleCollider;
import com.swrobotics.pathfinding.library.collider.Collider;
import com.swrobotics.pathfinding.library.collider.RectangleCollider;
import com.swrobotics.pathfinding.library.collider.Scene;
import processing.core.PApplet;

import java.io.File;
import java.util.List;

public final class PathfinderTest extends PApplet {
    private static final int WIDTH = 54 * 12 / 6;
    private static final int HEIGHT = 27 * 12 / 6;
    private static final int CELL_SIZE = 13;

    private Grid grid;
    private Scene scene;
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

        grid = new Grid(WIDTH, HEIGHT);
        scene = Scene.loadFromFile(new File("scene.txt"));
        grid.buildEnvironmentFromScene(scene, new CircleCollider(0, 0, 3));

        pathfinder = new Pathfinder();
        pathfinder.setGrid(grid);

        start = new Point(1, 1);
        end = new Point(10, 10);
    }

    @Override
    public void draw() {
        stroke(0);
        for (int x = 0; x < WIDTH; x++) {
            for (int y = 0; y < HEIGHT; y++) {
                if (grid.get(x, y))
                    fill(255, 0, 0);
                else
                    fill(255);

                rect(x * CELL_SIZE, y * CELL_SIZE, CELL_SIZE, CELL_SIZE);
            }
        }

        strokeWeight(2);
        for (Collider c : scene.getColliders()) {
            if (c instanceof CircleCollider) {
                stroke(255, 128, 0);
                noFill();

                CircleCollider circle = (CircleCollider) c;
                ellipse((float) circle.getX() * CELL_SIZE, (float) circle.getY() * CELL_SIZE, (float) circle.getRadius() * 2 * CELL_SIZE, (float) circle.getRadius() * 2 * CELL_SIZE);
            } else if (c instanceof RectangleCollider) {
                stroke(255, 128, 0);
                noFill();

                RectangleCollider rect = (RectangleCollider) c;
                pushMatrix();
                translate((float) rect.getX() * CELL_SIZE, (float) rect.getY() * CELL_SIZE);
                rotate((float) -rect.getRotation());
                rect((float) -rect.getWidth() / 2 * CELL_SIZE, (float) -rect.getHeight() / 2 * CELL_SIZE, (float) rect.getWidth() * CELL_SIZE, (float) rect.getHeight() * CELL_SIZE);
                popMatrix();
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

            List<Point> optimized = PathOptimizer.optimize(grid, path);

            stroke(128, 64, 255);
            beginShape(LINE_STRIP);
            for (Point point : optimized) {
                vertex((point.getX() + 0.5f) * CELL_SIZE, (point.getY() + 0.5f) * CELL_SIZE);
            }
            endShape();
        }

        strokeWeight(1);

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
            grid.setOverlay(x, y, dragMode = !grid.getOverlay(x, y));
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
            grid.setOverlay(x, y, dragMode);
        }
    }

    public static void main(String[] args) {
        PApplet.main(PathfinderTest.class);
    }
}
