package ru.mipt.bit.platformer.model;

import com.badlogic.gdx.math.GridPoint2;
import static com.badlogic.gdx.math.MathUtils.isEqual;

public class Tank {
    private GridPoint2 coordinates;
    private GridPoint2 destinationCoordinates;
    private float movementProgress = 1f;
    private float rotation = 0f;
    private final float speed = 0.4f;

    public Tank(GridPoint2 startPosition) {
        this.coordinates = new GridPoint2(startPosition);
        this.destinationCoordinates = new GridPoint2(startPosition);
    }

    public void tryMove(Direction direction, GridPoint2 obstacleCoordinates) {
        if (!isEqual(movementProgress, 1f)) return;

        GridPoint2 nextCoord = direction.getNextCoordinate(coordinates);
        if (!nextCoord.equals(obstacleCoordinates)) {
            destinationCoordinates.set(nextCoord);
            movementProgress = 0f;
        }
        rotation = direction.getRotation();
    }

    public void update(float deltaTime) {
        movementProgress = Math.min(1f, movementProgress + deltaTime / speed);
        if (isEqual(movementProgress, 1f)) {
            coordinates.set(destinationCoordinates);
        }
    }

    public GridPoint2 getCoordinates() { return coordinates; }
    public GridPoint2 getDestinationCoordinates() { return destinationCoordinates; }
    public float getMovementProgress() { return movementProgress; }
    public float getRotation() { return rotation; }
}