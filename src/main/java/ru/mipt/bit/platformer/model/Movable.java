package ru.mipt.bit.platformer.model;

import com.badlogic.gdx.math.GridPoint2;

public interface Movable extends GameObject {
    void tryMove(Direction direction, GridPoint2 obstacleCoordinates);
    GridPoint2 getDestinationCoordinates();
    float getMovementProgress();
    float getRotation();
}