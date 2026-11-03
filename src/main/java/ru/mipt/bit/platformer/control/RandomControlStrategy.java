package ru.mipt.bit.platformer.control;

import ru.mipt.bit.platformer.model.Direction;
import ru.mipt.bit.platformer.model.Movable;
import ru.mipt.bit.platformer.model.ObstacleMap;
import ru.mipt.bit.platformer.model.Shootable;

import java.util.Optional;
import java.util.Random;

public class RandomControlStrategy implements ControlStrategy {
    private static final Direction[] DIRECTIONS = Direction.values();
    private static final double SHOOT_PROBABILITY = 0.2;

    private final Random random;
    private final boolean shootingEnabled;

    public RandomControlStrategy(Random random) {
        this(random, false);
    }

    public RandomControlStrategy(Random random, boolean shootingEnabled) {
        this.random = random;
        this.shootingEnabled = shootingEnabled;
    }

    @Override
    public Optional<Command> nextCommand(Movable tank, ObstacleMap obstacles) {
        if (tank.isMoving()) {
            return Optional.empty();
        }

        if (shootingEnabled
                && tank instanceof Shootable
                && random.nextDouble() < SHOOT_PROBABILITY) {
            return Optional.of(new ShootCommand((Shootable) tank));
        }

        Direction direction = DIRECTIONS[random.nextInt(DIRECTIONS.length)];
        return Optional.of(new MoveCommand(tank, direction, obstacles));
    }
}
