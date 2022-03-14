package com.swrobotics.pathfinding.library.collider;

public interface Collider {
    boolean collidesWith(Collider other, double offsetX, double offsetY);
}
