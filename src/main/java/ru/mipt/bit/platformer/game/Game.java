package ru.mipt.bit.platformer.game;

import ru.mipt.bit.platformer.control.TankController;
import ru.mipt.bit.platformer.model.Level;

import java.util.ArrayList;
import java.util.Collections;
import java.util.List;

/**
 * Игровая логика без графики: на каждом кадре контроллеры отдают команды, затем танки двигаются.
 */
public class Game {
    private final Level level;
    private final List<TankController> controllers;

    public Game(Level level, List<TankController> controllers) {
        this.level = level;
        this.controllers = Collections.unmodifiableList(new ArrayList<>(controllers));
    }

    public void update(float deltaTime) {
        // Команды исполняются по очереди, поэтому два танка не могут
        // выбрать одну клетку в одном кадре: второй увидит её уже занятой.
        for (TankController controller : controllers) {
            controller.act(level);
        }
        level.update(deltaTime);
    }

    public Level getLevel() {
        return level;
    }

    public List<TankController> getControllers() {
        return controllers;
    }
}
