package com.swrobotics.pathfinding.library;

import com.swrobotics.pathfinding.library.grid.Grid;

import java.util.ArrayList;
import java.util.List;

public final class PathOptimizer {
    private static boolean testLineOfSight(Grid grid, Point start, Point end) {
        // TODO: Use a proper algorithm.
        //     So far, this has not been an issue but it could cause a path
        //     to be incorrect if the line is long enough.

        double precision = 0.01;
        for (int i = 0; i <= 100; i++) {
            double x = start.getX() + (end.getX() - start.getX()) * precision * i + 0.5;
            double y = start.getY() + (end.getY() - start.getY()) * precision * i + 0.5;

            if (grid.get((int) x, (int) y)) {
                return false;
            }
        }

        return true;
    }

    // Removes steps in the path which aren't neccesary and allows for perfect diagonal lines
    public static List<Point> optimize(Grid grid, List<Point> path) {
        List<Point> output = new ArrayList<>();

        for (int i = 0; i < path.size(); i++) {
            Point point = path.get(i);
            output.add(point);

            while (i < path.size() - 2) {
                if (!testLineOfSight(grid, point, path.get(i + 2))) {
                    break;
                }
                i++;
            }
        }

        return output;
    }
}
