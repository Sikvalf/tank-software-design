package ru.mipt.bit.platformer.model;

import com.badlogic.gdx.math.GridPoint2;
import static com.badlogic.gdx.math.MathUtils.isEqual;

public class Tank implements Movable {
    private final GridPoint2 coordinates;
    private final GridPoint2 destinationCoordinates;
    private float movementProgress = 1f;
    private float rotation = 0f;
    private final float speed;

    public Tank(GridPoint2 startPosition, float speed) {
        this.coordinates = new GridPoint2(startPosition);
        this.destinationCoordinates = new GridPoint2(startPosition);
        this.speed = speed;
    }

    @Override
    public void tryMove(Direction direction, ObstacleMap obstacles) {
        if (isMoving()) return;

        GridPoint2 nextCoord = direction.getNextCoordinate(coordinates);
        if (obstacles.isFree(nextCoord)) {
            destinationCoordinates.set(nextCoord);
            movementProgress = 0f;
        }
        rotation = direction.getRotation();
    }

    public void update(float deltaTime) {
        movementProgress = Math.min(1f, movementProgress + deltaTime / speed);
        if (isEqual(movementProgress, 1f)) {
            movementProgress = 1f;
            coordinates.set(destinationCoordinates);
        }
    }

    /**
     * Пока танк едет, он занимает обе клетки — исходную и целевую.
     */
    public boolean occupies(GridPoint2 cell) {
        return coordinates.equals(cell) || destinationCoordinates.equals(cell);
    }

    @Override
    public boolean isMoving() { return !isEqual(movementProgress, 1f); }
    @Override
    public GridPoint2 getCoordinates() { return coordinates; }
    @Override
    public GridPoint2 getDestinationCoordinates() { return destinationCoordinates; }
    @Override
    public float getMovementProgress() { return movementProgress; }
    @Override
    public float getRotation() { return rotation; }
}
