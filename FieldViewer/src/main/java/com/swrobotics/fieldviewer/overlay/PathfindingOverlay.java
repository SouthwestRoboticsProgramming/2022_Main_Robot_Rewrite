package com.swrobotics.fieldviewer.overlay;

import com.swrobotics.fieldviewer.FieldViewer;
import com.swrobotics.messenger.client.MessengerClient;

import java.util.HashSet;
import java.util.Set;

public final class PathfindingOverlay implements FieldOverlay {
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

    private final Set<Obstacle> obstacles;

    public PathfindingOverlay(MessengerClient msg) {
        obstacles = new HashSet<>();
    }

    @Override
    public void draw(FieldViewer applet) {
        for (Obstacle obstacle : obstacles) {
            
        }
    }
}
