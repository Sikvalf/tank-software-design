package ru.mipt.bit.platformer.control;

import ru.mipt.bit.platformer.model.Direction;
import ru.mipt.bit.platformer.model.Movable;
import ru.mipt.bit.platformer.model.ObstacleMap;

public class MoveCommand implements Command {
    private final Movable movable;
    private final Direction direction;
    private final ObstacleMap obstacles;

    public MoveCommand(Movable movable, Direction direction, ObstacleMap obstacles) {
        this.movable = movable;
        this.direction = direction;
        this.obstacles = obstacles;
    }

    @Override
    public void execute() {
        movable.tryMove(direction, obstacles);
    }

    public Direction getDirection() {
        return direction;
    }
}
