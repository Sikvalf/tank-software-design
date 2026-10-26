package ru.mipt.bit.platformer.game;

import ru.mipt.bit.platformer.control.ControlStrategy;
import ru.mipt.bit.platformer.control.TankController;
import ru.mipt.bit.platformer.model.Level;
import ru.mipt.bit.platformer.model.Tank;

import java.util.ArrayList;
import java.util.List;
import java.util.function.Supplier;

/**
 * Собирает игру: танк игрока получает стратегию игрока, остальные — AI-стратегию.
 */
public final class GameFactory {
    private GameFactory() {
    }

    public static Game create(Level level, ControlStrategy playerStrategy, Supplier<ControlStrategy> aiStrategies) {
        List<TankController> controllers = new ArrayList<>();
        controllers.add(new TankController(level.getPlayerTank(), playerStrategy));
        for (Tank aiTank : level.getAiTanks()) {
            controllers.add(new TankController(aiTank, aiStrategies.get()));
        }
        return new Game(level, controllers);
    }
}
