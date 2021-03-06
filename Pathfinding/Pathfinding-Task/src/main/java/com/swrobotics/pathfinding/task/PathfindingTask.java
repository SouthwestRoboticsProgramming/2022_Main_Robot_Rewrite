package com.swrobotics.pathfinding.task;

import com.swrobotics.messenger.client.MessengerClient;
import com.swrobotics.pathfinding.library.grid.ArrayGrid;
import com.swrobotics.pathfinding.library.grid.Grid;
import com.swrobotics.pathfinding.library.PathOptimizer;
import com.swrobotics.pathfinding.library.Pathfinder;
import com.swrobotics.pathfinding.library.Point;
import com.swrobotics.pathfinding.library.collider.CircleCollider;
import com.swrobotics.pathfinding.library.collider.Collider;
import com.swrobotics.pathfinding.library.collider.RectangleCollider;
import com.swrobotics.pathfinding.library.collider.Scene;
import com.swrobotics.pathfinding.library.grid.GridUnion;

import java.io.*;
import java.util.*;

public final class PathfindingTask {
    private static final String CONFIG_FILE = "config.properties";

    private static final String IN_SET_POSITION = "RoboRIO:Location";
    private static final String IN_SET_TARGET = "Pathfinder:SetTarget";
    private static final String IN_DUMP_SCENE = "Pathfinder:DumpScene";
    private static final String IN_LIDAR_POINT = "Lidar:Scan";

    private static final String OUT_PATH = "Pathfinder:Path";
    private static final String OUT_SCENE_DATA = "Pathfinder:SceneData";

    private static final int CELLS_X = 54 * 12 / 6;
    private static final int CELLS_Y = 27 * 12 / 6;

    private static final float CELLS_PER_METER = 3.281f * 2;
    private static final float METERS_PER_CELL = 1 / CELLS_PER_METER;

    private static final long LIDAR_DECAY_TIME = 1000;

    public static void main(String[] args) {
        Properties config = new Properties();
        try {
            config.load(new FileReader(CONFIG_FILE));
        } catch (IOException e) {
            System.err.println("Failed to load configuration file, using default config");
            config.setProperty("host", "localhost");
            config.setProperty("port", "5805");
            config.setProperty("name", "Pathfinder");
            config.setProperty("agentRadius", "2.94");
            config.setProperty("collisionPadding", "1");
            try {
                config.store(new FileWriter(CONFIG_FILE), "Configuration for Pathfinder");
            } catch (IOException e2) {
                System.err.println("Failed to save default config file:");
                e2.printStackTrace();
            }
        }

        String host = config.getProperty("host");
        int port = Integer.parseInt(config.getProperty("port"));
        String name = config.getProperty("name");
        float agentRadius = Float.parseFloat(config.getProperty("agentRadius"));
        float collisionPadding = Float.parseFloat(config.getProperty("collisionPadding"));

        System.out.println("Connecting to Messenger server at " + host + ":" + port + " as " + name);
        MessengerClient msg = null;
        try {
            msg = new MessengerClient(host, port, name);
        } catch (IOException e) {
            System.err.println("Messenger connection failed:");
            e.printStackTrace();
            System.exit(1);
        }
        System.out.println("Connected");

        CircleCollider agent = new CircleCollider(0, 0, agentRadius + collisionPadding);

        ArrayGrid sceneGrid = new ArrayGrid(CELLS_X, CELLS_Y);
        Scene scene = Scene.loadFromFile(new File("scene.txt"));
        sceneGrid.buildEnvironmentFromScene(scene, agent);

        LidarPointCollisionGrid lidarGrid = new LidarPointCollisionGrid(agent, CELLS_X, CELLS_Y);
        Set<LidarPoint> lidarPoints = new HashSet<>();

        Grid grid = new GridUnion(sceneGrid, lidarGrid);

        Pathfinder pathfinder = new Pathfinder();
        pathfinder.setGrid(grid);

        // These are arrays so they can be modified in the lambda
        Point[] start = {new Point(28, 17)};
        Point[] goal = {new Point(77, 33)};

        final MessengerClient finalMsg = msg;
        msg.makeHandler()
                .listen(IN_SET_TARGET)
                .listen(IN_SET_POSITION)
                .listen(IN_DUMP_SCENE)
                .listen(IN_LIDAR_POINT)
                .setHandler((type, in) -> {
                    switch (type) {
                        case IN_SET_TARGET: {
                            // Conversion from driver coords to judge coords
                            double y = -in.readDouble();
                            double x = in.readDouble();

                            int cx = (int) (x / METERS_PER_CELL + CELLS_X / 2f);
                            int cy = (int) (y / METERS_PER_CELL + CELLS_Y / 2f);

                            if (cx < 0 || cy < 0 | cx >= grid.getWidth() || cy >= grid.getHeight()) {
                                System.err.println("Ignoring request to set target as it is outside the field");
                                break;
                            }

                            goal[0] = new Point(cx, cy);
                            System.out.println("Now targeting " + x + ", " + y);

                            break;
                        }
                        case IN_SET_POSITION: {
                            // Conversion from driver coords to judge coords
                            double y = -in.readDouble();
                            double x = in.readDouble();
                            double rot = in.readDouble();

                            int cx = (int) (x / METERS_PER_CELL + CELLS_X / 2f);
                            int cy = (int) (y / METERS_PER_CELL + CELLS_Y / 2f);

                            if (cx < 0 || cy < 0 | cx >= grid.getWidth() || cy >= grid.getHeight()) {
                                System.err.println("Ignoring position as it is outside the field");
                                break;
                            }

                            start[0] = new Point(cx, cy);

                            break;
                        }
                        case IN_DUMP_SCENE: {
                            final int TYPE_CODE_NONE = -1;
                            final int TYPE_CODE_CIRCLE = 0;
                            final int TYPE_CODE_RECTANGLE = 1;

                            ByteArrayOutputStream b = new ByteArrayOutputStream();
                            DataOutputStream d = new DataOutputStream(b);
                            try {
                                Set<Collider> colliders = scene.getColliders();
                                d.writeInt(colliders.size());
                                for (Collider c : colliders) {
                                    if (c instanceof CircleCollider) {
                                        d.writeInt(TYPE_CODE_CIRCLE);

                                        CircleCollider circle = (CircleCollider) c;
                                        d.writeDouble((circle.getX() - CELLS_X / 2f) / CELLS_PER_METER);
                                        d.writeDouble((circle.getY() - CELLS_Y / 2f) / CELLS_PER_METER);
                                        d.writeDouble(circle.getRadius() / CELLS_PER_METER);
                                    } else if (c instanceof RectangleCollider) {
                                        d.writeInt(TYPE_CODE_RECTANGLE);

                                        RectangleCollider rect = (RectangleCollider) c;
                                        d.writeDouble((rect.getX() - CELLS_X / 2f) / CELLS_PER_METER);
                                        d.writeDouble((rect.getY() - CELLS_Y / 2f) / CELLS_PER_METER);
                                        d.writeDouble(rect.getWidth() / CELLS_PER_METER);
                                        d.writeDouble(rect.getHeight() / CELLS_PER_METER);
                                        d.writeDouble(rect.getRotation());
                                    } else {
                                        d.writeInt(TYPE_CODE_NONE);
                                    }
                                }
                            } catch (IOException e) {
                                e.printStackTrace();
                            }

                            finalMsg.sendMessage(OUT_SCENE_DATA, b.toByteArray());
                            break;
                        }
                        case IN_LIDAR_POINT: {
                            int quality = in.readInt();
                            double y = -in.readDouble();
                            double x = in.readDouble();

                            int cx = (int) (x / METERS_PER_CELL + CELLS_X / 2f);
                            int cy = (int) (y / METERS_PER_CELL + CELLS_Y / 2f);

                            lidarGrid.addLidarPoint(cx, cy);
                            lidarPoints.add(new LidarPoint(cx, cy, System.currentTimeMillis()));
                            break;
                        }
                    }
                });

        while (true) {
            msg.readMessages();

            List<Point> path = pathfinder.findPath(start[0], goal[0]);

            ByteArrayOutputStream b = new ByteArrayOutputStream();
            DataOutputStream d = new DataOutputStream(b);
            try {
                if (path == null) {
                    d.writeBoolean(false);
                } else {
                    path = PathOptimizer.optimize(grid, path);
                    d.writeBoolean(true);
                    d.writeInt(path.size());
                    for (Point point : path) {
                        double x = (point.getX() - CELLS_X / 2.0) / CELLS_PER_METER;
                        double y = (point.getY() - CELLS_Y / 2.0) / CELLS_PER_METER;
                        d.writeDouble(x);
                        d.writeDouble(y);
                    }
                }
            } catch (IOException e) {
                e.printStackTrace();
            }
            msg.sendMessage(OUT_PATH, b.toByteArray());

            for (Iterator<LidarPoint> iter = lidarPoints.iterator(); iter.hasNext(); ) {
                LidarPoint point = iter.next();
                if (System.currentTimeMillis() - point.getTimestamp() >= LIDAR_DECAY_TIME) {
                    iter.remove();
                    lidarGrid.removeLidarPoint(point.getX(), point.getY());
                }
            }

            try {
                Thread.sleep(50);
            } catch (InterruptedException e) {
                // Ignore
            }
        }
    }
}
