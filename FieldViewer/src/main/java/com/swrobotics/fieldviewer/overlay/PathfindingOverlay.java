package com.swrobotics.fieldviewer.overlay;

import com.swrobotics.fieldviewer.FieldViewer;
import com.swrobotics.messenger.client.MessengerClient;
import processing.core.PConstants;

import java.io.DataInputStream;
import java.io.IOException;
import java.util.ArrayList;
import java.util.HashSet;
import java.util.List;
import java.util.Set;

public final class PathfindingOverlay implements FieldOverlay {
    private static final String IN_PATH = "Pathfinder:Path";
    private static final String IN_SCENE_DATA = "Pathfinder:SceneData";
    private static final String IN_SET_POSITION = "RoboRIO:Location";
    private static final String IN_SET_TARGET = "Pathfinder:SetTarget";

    private static final String OUT_DUMP_SCENE = "Pathfinder:DumpScene";

    private static final int TYPE_CODE_CIRCLE = 0;
    private static final int TYPE_CODE_RECTANGLE = 1;

    private interface Obstacle {
        void draw(FieldViewer viewer);
    }

    private static class CircleObstacle implements Obstacle {
        private final float x, y;
        private final float radius;

        public CircleObstacle(float x, float y, float radius) {
            this.x = x;
            this.y = y;
            this.radius = radius;
        }

        @Override
        public void draw(FieldViewer viewer) {
            viewer.ellipse(x, y, radius * 2, radius * 2);
        }
    }

    private static class RectangleObstacle implements Obstacle {
        private final float x, y;
        private final float width, height;
        private final float rotation;

        public RectangleObstacle(float x, float y, float width, float height, float rotation) {
            this.x = x;
            this.y = y;
            this.width = width;
            this.height = height;
            this.rotation = rotation;
        }

        @Override
        public void draw(FieldViewer viewer) {
            viewer.pushMatrix();

            viewer.translate(x, y);
            viewer.rotate(-rotation);
            viewer.rect(0, 0, width, height);

            viewer.popMatrix();
        }
    }

    private static class Point {
        final float x;
        final float y;

        public Point(float x, float y) {
            this.x = x;
            this.y = y;
        }
    }

    private final MessengerClient msg;
    private final Set<Obstacle> obstacles;
    private final List<Point> path;

    private Point position, target;
    private boolean hasReceivedScene = false;

    public PathfindingOverlay(MessengerClient msg) {
        this.msg = msg;
        obstacles = new HashSet<>();
        path = new ArrayList<>();

        position = new Point(0, 0);
        target = new Point(0, 0);

        msg.makeHandler()
                .listen(IN_SET_POSITION)
                .listen(IN_SET_TARGET)
                .listen(IN_SCENE_DATA)
                .listen(IN_PATH)
                .setHandler(this::onMessage);
    }

    private void readPath(DataInputStream in) throws IOException {
        boolean pathValid = in.readBoolean();
        path.clear();

        if (pathValid) {
            int count = in.readInt();
            for (int i = 0; i < count; i++) {
                double x = in.readDouble();
                double y = in.readDouble();

                path.add(new Point((float) -y, (float) x));
            }
        }
    }

    private void readPosition(DataInputStream in) throws IOException {
        double x = in.readDouble();
        double y = in.readDouble();

        position = new Point((float) x, (float) y);
    }

    private void readTarget(DataInputStream in) throws IOException {
        double x = in.readDouble();
        double y = in.readDouble();

        target = new Point((float) x, (float) y);
    }

    private void readSceneData(DataInputStream in) throws IOException {
        obstacles.clear();

        int count = in.readInt();
        for (int i = 0; i < count; i++) {
            int typeCode = in.readInt();
            switch (typeCode) {
                case TYPE_CODE_CIRCLE: {
                    double x = in.readDouble();
                    double y = in.readDouble();
                    double radius = in.readDouble();

                    obstacles.add(new CircleObstacle((float) -y, (float) x, (float) radius));
                }
                case TYPE_CODE_RECTANGLE: {
                    double x = in.readDouble();
                    double y = in.readDouble();
                    double width = in.readDouble();
                    double height = in.readDouble();
                    double rotation = in.readDouble();

                    obstacles.add(new RectangleObstacle((float) -y, (float) x, (float) width, (float) height, (float) (rotation + Math.PI / 2)));
                }
            }
        }

        hasReceivedScene = true;
    }

    private void onMessage(String type, DataInputStream in) throws IOException {
        switch (type) {
            case IN_PATH:
                readPath(in);
                break;
            case IN_SET_POSITION:
                readPosition(in);
                break;
            case IN_SET_TARGET:
                readTarget(in);
                break;
            case IN_SCENE_DATA:
                readSceneData(in);
                break;
        }
    }

    @Override
    public void draw(FieldViewer applet) {
        if (!hasReceivedScene)
            msg.sendMessage(OUT_DUMP_SCENE);

        applet.strokeWeight(2);
        applet.stroke(255, 255, 0);
        applet.noFill();

        for (Obstacle obstacle : obstacles) {
            obstacle.draw(applet);
        }

        applet.stroke(128, 128, 255);
        applet.beginShape(PConstants.LINE_STRIP);
        for (Point point : path) {
            applet.vertex(point.x, point.y);
        }
        applet.endShape();

        applet.strokeWeight(6);
        applet.stroke(255, 255, 0);
        applet.point(position.x, position.y);

        applet.stroke(0, 255, 0);
        applet.point(target.x, target.y);
    }
}
