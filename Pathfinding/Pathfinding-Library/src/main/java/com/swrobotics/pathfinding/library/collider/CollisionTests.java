package com.swrobotics.pathfinding.library.collider;

public final class CollisionTests {
    public static boolean cvc(double ax, double ay, double ar, double bx, double by, double br) {
        double dx = ax - bx;
        double dy = ay - by;
        double r = ar + br;

        return dx * dx + dy * dy <= r * r;
    }

    public static boolean cvr(double cx, double cy, double cr, double rx, double ry, double rw, double rh, double rr) {
        // Do not question the math blob

        double lx = cx - rx;
        double ly = cy - ry;
        double s = Math.sin(-rr + Math.PI / 2);
        double c = Math.cos(-rr + Math.PI / 2);
        double px = lx * s - ly * c;
        double py = lx * c + ly * s;
        double hw = rw / 2.0;
        double hh = rh / 2.0;
        double clx = Math.max(-hw, Math.min(px, hw));
        double cly = Math.max(-hh, Math.min(py, hh));
        double dx = px - clx;
        double dy = py - cly;

        return dx * dx + dy * dy <= cr * cr;
    }

    private CollisionTests() {
        throw new AssertionError();
    }
}
