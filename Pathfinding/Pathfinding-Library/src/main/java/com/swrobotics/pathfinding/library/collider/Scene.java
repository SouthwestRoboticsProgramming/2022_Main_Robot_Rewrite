package com.swrobotics.pathfinding.library.collider;

import java.io.BufferedReader;
import java.io.File;
import java.io.FileReader;
import java.io.IOException;
import java.util.HashSet;
import java.util.Set;

public final class Scene {
    private final Set<Collider> colliders;

    public static Scene loadFromFile(File file) {
        Set<Collider> colliders = new HashSet<>();
        try {
            BufferedReader br = new BufferedReader(new FileReader(file));

            String line;
            while ((line = br.readLine()) != null) {
                if ("".equals(line)) continue;
                if (line.startsWith("#")) continue;

                String[] tokens = line.split(" ");
                switch (tokens[0]) {
                    case "circle": {
                        float x = 0, y = 0, radius = 0;
                        for (int i = 1; i < tokens.length; i++) {
                            String[] parts = tokens[i].split("=");
                            float value = Float.parseFloat(parts[1]);
                            switch (parts[0]) {
                                case "x": x = value; break;
                                case "y": y = value; break;
                                case "radius": radius = value; break;
                            }
                        }
                        System.out.println("Circle collider at (" + x + ", " + y + ") with radius " + radius);
                        colliders.add(new CircleCollider(x, y, radius));
                    }
                    case "rectangle": {
                        float x = 0, y = 0, width = 0, height = 0, rotation = 0;
                        for (int i = 1; i < tokens.length; i++) {
                            String[] parts = tokens[i].split("=");
                            float value = Float.parseFloat(parts[1]);
                            switch (parts[0]) {
                                case "x": x = value; break;
                                case "y": y = value; break;
                                case "width": width = value; break;
                                case "height": height = value; break;
                                case "rotation": rotation = (float) Math.toRadians(value); break;
                            }
                        }
                        System.out.println("Rectangle collider at (" + x + ", " + y + ") with size (" + width + ", " + height + ") with rotation " + rotation);
                        colliders.add(new RectangleCollider(x, y, width, height, rotation));
                    }
                }
            }
        } catch (IOException e) {
            System.err.println("Failed to load scene!");
            e.printStackTrace();
            System.exit(1);
        }

        return new Scene(colliders);
    }

    public Scene(Set<Collider> colliders) {
        this.colliders = colliders;
    }

    public Set<Collider> getColliders() {
        return colliders;
    }

    public boolean testCollision(Collider agent, double offsetX, double offsetY) {
        for (Collider collider : colliders) {
            if (agent.collidesWith(collider, offsetX, offsetY)) {
                return true;
            }
        }

        return false;
    }
}
