package ru.mipt.bit.platformer.control;

import ru.mipt.bit.platformer.model.Direction;
import ru.mipt.bit.platformer.model.Movable;
import ru.mipt.bit.platformer.model.ObstacleMap;

import java.util.Optional;
import java.util.Random;

/**
 * Простейший AI: каждый раз, когда танк стоит, выбирает случайное направление.
 */
public class RandomControlStrategy implements ControlStrategy {
    private static final Direction[] DIRECTIONS = Direction.values();

    private final Random random;

    public RandomControlStrategy(Random random) {
        this.random = random;
    }

    @Override
    public Optional<Command> nextCommand(Movable tank, ObstacleMap obstacles) {
        if (tank.isMoving()) {
            return Optional.empty();
        }
        Direction direction = DIRECTIONS[random.nextInt(DIRECTIONS.length)];
        return Optional.of(new MoveCommand(tank, direction, obstacles));
    }
}
