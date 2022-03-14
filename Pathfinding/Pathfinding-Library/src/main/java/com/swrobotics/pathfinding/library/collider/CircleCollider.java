package com.swrobotics.pathfinding.library.collider;

public final class CircleCollider implements Collider {
    private final double x;
    private final double y;
    private final double radius;

    public CircleCollider(double x, double y, double radius) {
        this.x = x;
        this.y = y;
        this.radius = radius;
    }

    public double getX() {
        return x;
    }

    public double getY() {
        return y;
    }

    public double getRadius() {
        return radius;
    }

    @Override
    public boolean collidesWith(Collider other, double offsetX, double offsetY) {
        if (other instanceof CircleCollider) {
            CircleCollider c = (CircleCollider) other;

            return CollisionTests.cvc(x + offsetX, y + offsetY, radius, c.x, c.y, c.radius);
        } else if (other instanceof RectangleCollider) {
            RectangleCollider r = (RectangleCollider) other;

            return CollisionTests.cvr(x + offsetX, y + offsetY, radius, r.getX(), r.getY(), r.getWidth(), r.getHeight(), r.getRotation());
        } else {
            throw new IllegalArgumentException("Unimplemented collision handler with " + other.getClass());
        }
    }
}
