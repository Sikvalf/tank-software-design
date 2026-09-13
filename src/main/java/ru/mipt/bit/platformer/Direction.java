package ru.mipt.bit.platformer;
import com.badlogic.gdx.Input;
import com.badlogic.gdx.math.GridPoint2;

public enum Direction {
    UP(0, 1, 90f, Input.Keys.UP, Input.Keys.W),
    DOWN(0, -1, -90f, Input.Keys.DOWN, Input.Keys.S),
    LEFT(-1, 0, -180f, Input.Keys.LEFT, Input.Keys.A),
    RIGHT(1, 0, 0f, Input.Keys.RIGHT, Input.Keys.D);

    private final int dx;
    private final int dy;
    private final float rotation;
    private final int[] keys;

    Direction(int dx, int dy, float rotation, int... keys) {
        this.dx = dx;
        this.dy = dy;
        this.rotation = rotation;
        this.keys = keys;
    }

    public GridPoint2 getNextCoordinate(GridPoint2 current) {
        return new GridPoint2(current.x + dx, current.y + dy);
    }

    public float getRotation() {
        return rotation;
    }

    public boolean isPressed() {
        for (int key : keys) {
            if (com.badlogic.gdx.Gdx.input.isKeyPressed(key)) {
                return true;
            }
        }
        return false;
    }
}