package ru.mipt.bit.platformer.control;

import ru.mipt.bit.platformer.model.Movable;
import ru.mipt.bit.platformer.model.ObstacleMap;
import ru.mipt.bit.platformer.model.Shootable;

import java.util.Optional;

public class PlayerControlStrategy implements ControlStrategy {
    private final DirectionInput input;
    private final ShootInput shootInput;

    public PlayerControlStrategy(DirectionInput input) {
        this(input, input instanceof ShootInput
                ? (ShootInput) input
                : () -> false);
    }

    public PlayerControlStrategy(DirectionInput input, ShootInput shootInput) {
        this.input = java.util.Objects.requireNonNull(input);
        this.shootInput = java.util.Objects.requireNonNull(shootInput);
    }

    @Override
    public Optional<Command> nextCommand(Movable tank, ObstacleMap obstacles) {
        if (shootInput.pollShoot() && tank instanceof Shootable) {
            return Optional.of(new ShootCommand((Shootable) tank));
        }

        return input.pollDirection()
                .map(direction -> new MoveCommand(tank, direction, obstacles));
    }
}
