package com.swrobotics.pathfinding;

import java.util.*;

public final class Pathfinder {
    private boolean[][] grid;
    private Node[][] nodes;
    private int width, height;

    public void setGrid(boolean[][] grid, int width, int height) {
        this.grid = grid;
        this.width = width;
        this.height = height;
    }

    private static class Node {
        private int x, y;

        public Node(int x, int y) {
            this.x = x;
            this.y = y;
        }

        public Point toPoint() {
            return new Point(x, y);
        }

        @Override
        public boolean equals(Object o) {
            if (this == o) return true;
            if (o == null || getClass() != o.getClass()) return false;
            Node node = (Node) o;
            return x == node.x &&
                    y == node.y;
        }

        @Override
        public int hashCode() {
            return Objects.hash(x, y);
        }
    }

    private boolean isValidNodeCoord(int x, int y) {
        return x >= 0 && y >= 0 && x < width && y < height;
    }

    private void testNeighbor(Node node, Set<Node> nodeSet, int dx, int dy) {
        int x = node.x + dx;
        int y = node.y + dy;
        if (isValidNodeCoord(x, y) && !grid[x][y])
            nodeSet.add(nodes[x][y]);
    }

    private Set<Node> getNeighbors(Node node) {
        Set<Node> nodes = new HashSet<>();
        testNeighbor(node, nodes, -1, -1);
        testNeighbor(node, nodes,  0, -1);
        testNeighbor(node, nodes,  1, -1);
        testNeighbor(node, nodes, -1,  0);
        testNeighbor(node, nodes,  1,  0);
        testNeighbor(node, nodes, -1,  1);
        testNeighbor(node, nodes,  0,  1);
        testNeighbor(node, nodes,  1,  1);
        return nodes;
    }

    private double cost(Node from, Node to) {
        int dx = from.x - to.x;
        int dy = from.y - to.y;
        return Math.sqrt(dx * dx + dy * dy);
    }

    private double heuristic(Node from, Node dest) {
        int dx = from.x - dest.x;
        int dy = from.y - dest.y;
        return Math.sqrt(dx * dx + dy * dy);
    }

    public List<Point> findPath(Point startPoint, Point goalPoint) {
        nodes = new Node[width][height];
        for (int x = 0; x < width; x++) {
            for (int y = 0; y < height; y++) {
                nodes[x][y] = new Node(x, y);
            }
        }

        Node start = nodes[startPoint.getX()][startPoint.getY()];
        Node goal = nodes[goalPoint.getX()][goalPoint.getY()];

        Map<Node, Node> cameFrom = new HashMap<>();
        cameFrom.put(start, null);
        Map<Node, Double> costSoFar = new HashMap<>();
        costSoFar.put(start, 0.0);
        Queue<Node> frontier = new PriorityQueue<>(Comparator.comparingDouble((node) -> costSoFar.get(node) + heuristic(node, goal)));
        frontier.add(start);

        boolean found = false;
        while (!frontier.isEmpty()) {
            Node current = frontier.remove();

            if (current.equals(goal)) {
                found = true;
                break;
            }

            for (Node next : getNeighbors(current)) {
                double newCost = costSoFar.get(current) + cost(current, next);
                if (!costSoFar.containsKey(next) || newCost < costSoFar.get(next)) {
                    costSoFar.put(next, newCost);
                    frontier.add(next);
                    cameFrom.put(next, current);
                }
            }
        }

        if (!found)
            return null;

        List<Point> path = new ArrayList<>();
        Node current = goal;
        while (!current.equals(start)) {
            path.add(0, current.toPoint());
            current = cameFrom.get(current);
        }
        path.add(0, start.toPoint());

        return path;
    }
}
