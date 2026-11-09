
package ru.mipt.bit.platformer.model;

import com.badlogic.gdx.math.GridPoint2;

import static com.badlogic.gdx.math.MathUtils.isEqual;

public class Tank implements Movable, Shootable {
    private final GridPoint2 coordinates;
    private final GridPoint2 destinationCoordinates;
    private float movementProgress = 1f;
    private float rotation = 0f;
    private Direction facing = Direction.RIGHT;
    private final float speed;
    private BulletSpawner bulletSpawner;
    private static final int MAX_HEALTH = 100;
    private int health = MAX_HEALTH;

    public int getHealth() {
        return health;
    }

    public int getMaxHealth() {
        return MAX_HEALTH;
    }

    public Tank(GridPoint2 startPosition, float speed) {
        this.coordinates = new GridPoint2(startPosition);
        this.destinationCoordinates = new GridPoint2(startPosition);
        this.speed = speed;
    }

    @Override
    public void tryMove(Direction direction, ObstacleMap obstacles) {
        if (isMoving()) {
            return;
        }

        GridPoint2 nextCoord = direction.getNextCoordinate(coordinates);

        if (obstacles.isFree(nextCoord)) {
            destinationCoordinates.set(nextCoord);
            movementProgress = 0f;
        }

        rotation = direction.getRotation();
        facing = direction;
    }

    public void update(float deltaTime) {
        movementProgress = Math.min(1f, movementProgress + deltaTime / speed);

        if (isEqual(movementProgress, 1f)) {
            movementProgress = 1f;
            coordinates.set(destinationCoordinates);
        }
    }

    public boolean occupies(GridPoint2 cell) {
        return coordinates.equals(cell) || destinationCoordinates.equals(cell);
    }

    @Override
    public boolean isMoving() {
        return !isEqual(movementProgress, 1f);
    }

    @Override
    public GridPoint2 getCoordinates() {
        return coordinates;
    }

    @Override
    public GridPoint2 getDestinationCoordinates() {
        return destinationCoordinates;
    }

    @Override
    public float getMovementProgress() {
        return movementProgress;
    }

    @Override
    public float getRotation() {
        return rotation;
    }

    public Direction getFacing() {
        return facing;
    }

    public void setBulletSpawner(BulletSpawner bulletSpawner) {
        this.bulletSpawner = java.util.Objects.requireNonNull(bulletSpawner);
    }

    @Override
    public void shoot() {
        if (bulletSpawner == null || isDestroyed()) {
            return;
        }

        GridPoint2 bulletPosition = facing.getNextCoordinate(coordinates);
        bulletSpawner.spawn(new Bullet(bulletPosition, facing));
    }

    public void takeDamage() {
        if (health > 0) {
            health--;
        }
    }

    public boolean isDestroyed() {
        return health == 0;
    }
    public BulletSpawner getBulletSpawner() {
        return bulletSpawner;
    }
}
