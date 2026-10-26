package ru.mipt.bit.platformer.control;

import ru.mipt.bit.platformer.model.Movable;
import ru.mipt.bit.platformer.model.ObstacleMap;

/**
 * Связывает танк со стратегией управления и исполняет выбранные ею команды.
 */
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
