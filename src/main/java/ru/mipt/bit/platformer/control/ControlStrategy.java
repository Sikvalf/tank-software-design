package ru.mipt.bit.platformer.control;

import ru.mipt.bit.platformer.model.Movable;
import ru.mipt.bit.platformer.model.ObstacleMap;

import java.util.Optional;

/**
 * Шаблон «Стратегия»: способ решить, какую команду танк выполнит на этом шаге.
 * Игрок и AI — это разные стратегии одного и того же интерфейса.
 */
public interface ControlStrategy {
    Optional<Command> nextCommand(Movable tank, ObstacleMap obstacles);
}
