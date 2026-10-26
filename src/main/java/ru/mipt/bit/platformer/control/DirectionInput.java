package ru.mipt.bit.platformer.control;

import ru.mipt.bit.platformer.model.Direction;

import java.util.Optional;

/**
 * Источник направлений от игрока (клавиатура, геймпад, тестовый стаб).
 */
public interface DirectionInput {
    /**
     * Возвращает направление, запрошенное игроком с прошлого вызова, и «забывает» его.
     */
    Optional<Direction> pollDirection();
}
