package ru.mipt.bit.platformer.control;

import ru.mipt.bit.platformer.model.Movable;
import ru.mipt.bit.platformer.model.ObstacleMap;

import java.util.Optional;

/**
 * Стратегия игрока: превращает нажатия клавиш в команды движения.
 */
public class PlayerControlStrategy implements ControlStrategy {
    private final DirectionInput input;

    public PlayerControlStrategy(DirectionInput input) {
        this.input = input;
    }

    @Override
    public Optional<Command> nextCommand(Movable tank, ObstacleMap obstacles) {
        return input.pollDirection()
                .map(direction -> new MoveCommand(tank, direction, obstacles));
    }
}
