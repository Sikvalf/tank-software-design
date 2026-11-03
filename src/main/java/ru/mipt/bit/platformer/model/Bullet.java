package ru.mipt.bit.platformer.model;

import com.badlogic.gdx.math.GridPoint2;

public class Bullet implements GameObject {
    private final GridPoint2 coordinates;
    private final Direction direction;

    public Bullet(GridPoint2 coordinates, Direction direction) {
        this.coordinates = new GridPoint2(coordinates);
        this.direction = direction;
    }

    @Override
    public GridPoint2 getCoordinates() {
        return new GridPoint2(coordinates);
    }

    public Direction getDirection() {
        return direction;
    }

    public void move() {
        coordinates.set(direction.getNextCoordinate(coordinates));
    }
}
