package ru.mipt.bit.platformer.control;

import ru.mipt.bit.platformer.model.Movable;
import ru.mipt.bit.platformer.model.ObstacleMap;
import java.util.Optional;


public class TankController {
    private final Movable tank;
    private final ControlStrategy strategy;

    public TankController(Movable tank, ControlStrategy strategy) {
        this.tank = tank;
        this.strategy = strategy;
    }

    public Optional<Command> selectCommand(ObstacleMap obstacles) {
        return strategy.nextCommand(tank, obstacles);
    }

    public void act(ObstacleMap obstacles) {
        selectCommand(obstacles).ifPresent(Command::execute);
    }

    public Movable getTank() {
        return tank;
    }
}
