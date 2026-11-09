package ru.mipt.bit.platformer.control;

import ru.mipt.bit.platformer.model.Movable;
import ru.mipt.bit.platformer.model.ObstacleMap;


public class TankController {
    private final Movable tank;
    private final ControlStrategy strategy;

    public TankController(Movable tank, ControlStrategy strategy) {
        this.tank = tank;
        this.strategy = strategy;
    }

    public void act(ObstacleMap obstacles) {
        strategy.nextCommand(tank, obstacles).ifPresent(Command::execute);
    }

    public Movable getTank() {
        return tank;
    }
}
