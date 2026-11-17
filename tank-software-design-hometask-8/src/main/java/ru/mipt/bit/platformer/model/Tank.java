
package ru.mipt.bit.platformer.model;

import com.badlogic.gdx.math.GridPoint2;

import static com.badlogic.gdx.math.MathUtils.isEqual;
import ru.mipt.bit.platformer.model.state.HealthBasedTankStateResolver;
import ru.mipt.bit.platformer.model.state.TankState;

public class Tank implements Movable, Shootable {
    private final GridPoint2 coordinates;
    private final GridPoint2 destinationCoordinates;
    private float movementProgress = 1f;
    private float rotation = 0f;
    private Direction facing = Direction.RIGHT;
    private final float speed;
    private BulletSpawner bulletSpawner;
    private int bulletDamage = 1;
    private float bulletStepDuration = 0.1f;
    private static final int MAX_HEALTH = 100;
    private int health = MAX_HEALTH;
    private final HealthBasedTankStateResolver stateResolver = new HealthBasedTankStateResolver();
    private TankState state = stateResolver.resolve(health, MAX_HEALTH);

    public TankState getState() {
        return state;
    }

    public int getHealth() {
        return health;
    }

    public int getMaxHealth() {
        return MAX_HEALTH;
    }

    public Tank(GridPoint2 startPosition, float speed) {
        this.coordinates = new GridPoint2(startPosition);
        this.destinationCoordinates = new GridPoint2(startPosition);
        if (speed <= 0f || !Float.isFinite(speed)) {
            throw new IllegalArgumentException("Speed must be positive and finite");
        }
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
        movementProgress = Math.min(1f, movementProgress + deltaTime / (speed * state.movementDurationMultiplier()));

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
        return new GridPoint2(coordinates);
    }

    @Override
    public GridPoint2 getDestinationCoordinates() {
        return new GridPoint2(destinationCoordinates);
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
        if (bulletSpawner == null || isDestroyed() || !state.canShoot()) {
            return;
        }

        GridPoint2 bulletPosition = facing.getNextCoordinate(coordinates);
        bulletSpawner.spawn(new Bullet(bulletPosition, facing, this, bulletDamage, bulletStepDuration));
    }

    public void takeDamage() {
        takeDamage(1);
    }

    public void takeDamage(int damage) {
        if (damage < 0) {
            throw new IllegalArgumentException("Damage cannot be negative");
        }
        health = Math.max(0, health - damage);
        state = stateResolver.resolve(health, MAX_HEALTH);
    }

    public void configureWeapon(int damage, float stepDuration) {
        if (damage <= 0 || stepDuration <= 0f || !Float.isFinite(stepDuration)) {
            throw new IllegalArgumentException("Invalid weapon parameters");
        }
        bulletDamage = damage;
        bulletStepDuration = stepDuration;
    }

    public boolean isDestroyed() {
        return health == 0;
    }
    public BulletSpawner getBulletSpawner() {
        return bulletSpawner;
    }
}
