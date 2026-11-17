package ru.mipt.bit.platformer.model;

import com.badlogic.gdx.math.GridPoint2;

public enum Direction {
    UP(0, 1, 90f),
    DOWN(0, -1, -90f),
    LEFT(-1, 0, -180f),
    RIGHT(1, 0, 0f);

    private final int dx;
    private final int dy;
    private final float rotation;

    Direction(int dx, int dy, float rotation) {
        this.dx = dx;
        this.dy = dy;
        this.rotation = rotation;
    }

    public GridPoint2 getNextCoordinate(GridPoint2 current) {
        return new GridPoint2(current.x + dx, current.y + dy);
    }

    public float getRotation() {
        return rotation;
    }
}