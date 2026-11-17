package ru.mipt.bit.platformer.model;

import com.badlogic.gdx.math.GridPoint2;

public class Bullet implements GameObject {
    private final GridPoint2 coordinates;
    private final Direction direction;
    private final Tank owner;
    private final int damage;
    private final float stepDuration;
    private float elapsed;

    public Bullet(GridPoint2 coordinates, Direction direction) {
        this(coordinates, direction, null, 1, 0.1f);
    }

    public Bullet(GridPoint2 coordinates, Direction direction, Tank owner, int damage, float stepDuration) {
        if (damage <= 0 || stepDuration <= 0f || !Float.isFinite(stepDuration)) {
            throw new IllegalArgumentException("Invalid bullet parameters");
        }
        this.coordinates = new GridPoint2(coordinates);
        this.direction = direction;
        this.owner = owner;
        this.damage = damage;
        this.stepDuration = stepDuration;
    }

    @Override
    public GridPoint2 getCoordinates() {
        return new GridPoint2(coordinates);
    }

    public Direction getDirection() {
        return direction;
    }

    public Tank getOwner() {
        return owner;
    }

    public int getDamage() {
        return damage;
    }

    public int advance(float deltaTime) {
        if (deltaTime < 0f || !Float.isFinite(deltaTime)) {
            throw new IllegalArgumentException("Invalid deltaTime");
        }
        elapsed += deltaTime;
        int steps = (int) (elapsed / stepDuration);
        elapsed -= steps * stepDuration;
        return steps;
    }

    public void move() {
        coordinates.set(direction.getNextCoordinate(coordinates));
    }
}
