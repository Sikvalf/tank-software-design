package ru.mipt.bit.platformer.control;

import ru.mipt.bit.platformer.model.Movable;
import ru.mipt.bit.platformer.model.ObstacleMap;

import java.util.Optional;

public interface ControlStrategy {
    Optional<Command> nextCommand(Movable tank, ObstacleMap obstacles);
}
