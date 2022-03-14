package com.swrobotics.pathfinding.library.collider;

public final class RectangleCollider implements Collider {
    private final double x;
    private final double y;
    private final double width;
    private final double height;
    private final double rotation;

    public RectangleCollider(double x, double y, double width, double height, double rotation) {
        this.x = x;
        this.y = y;
        this.width = width;
        this.height = height;
        this.rotation = rotation;
    }

    public double getX() {
        return x;
    }

    public double getY() {
        return y;
    }

    public double getWidth() {
        return width;
    }

    public double getHeight() {
        return height;
    }

    public double getRotation() {
        return rotation;
    }

    @Override
    public boolean collidesWith(Collider other, double offsetX, double offsetY) {
        if (other instanceof CircleCollider) {
            CircleCollider c = (CircleCollider) other;

            return CollisionTests.cvr(c.getX(), c.getY(), c.getRadius(), x, y, width, height, rotation);
        } else {
            throw new IllegalArgumentException("Unimplemented collision handler with " + other.getClass());
        }
    }
}
