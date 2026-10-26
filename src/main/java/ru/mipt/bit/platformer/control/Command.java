package ru.mipt.bit.platformer.control;

/**
 * Шаблон «Команда»: действие над игровым объектом, оформленное в виде объекта.
 */
@FunctionalInterface
public interface Command {
    void execute();
}
