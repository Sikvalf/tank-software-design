package ru.mipt.bit.platformer.level;

import com.badlogic.gdx.math.GridPoint2;
import ru.mipt.bit.platformer.model.Level;
import ru.mipt.bit.platformer.model.Tank;

import java.util.ArrayList;
import java.util.Collections;
import java.util.List;
import java.util.Random;

/**
 * Декоратор над любым генератором уровня: добавляет AI-танки в случайные свободные клетки.
 */
public class RandomTanksLevelGenerator implements LevelGenerator {
    private final LevelGenerator base;
    private final int tankCount;
    private final float tankSpeed;
    private final Random random;

    public RandomTanksLevelGenerator(LevelGenerator base, int tankCount, float tankSpeed, Random random) {
        if (tankCount < 0) {
            throw new IllegalArgumentException("Tank count must be non-negative, got " + tankCount);
        }
        this.base = base;
        this.tankCount = tankCount;
        this.tankSpeed = tankSpeed;
        this.random = random;
    }

    @Override
    public Level generate() {
        Level level = base.generate();
        List<GridPoint2> freeCells = findFreeCells(level);
        if (freeCells.size() < tankCount) {
            throw new IllegalStateException(String.format(
                    "Not enough free cells for %d tanks: only %d available", tankCount, freeCells.size()));
        }
        Collections.shuffle(freeCells, random);

        List<Tank> aiTanks = new ArrayList<>(level.getAiTanks());
        for (GridPoint2 cell : freeCells.subList(0, tankCount)) {
            aiTanks.add(new Tank(cell, tankSpeed));
        }
        return new Level(level.getWidth(), level.getHeight(), level.getPlayerTank(), aiTanks, level.getTrees());
    }

    private static List<GridPoint2> findFreeCells(Level level) {
        List<GridPoint2> cells = new ArrayList<>();
        for (int x = 0; x < level.getWidth(); x++) {
            for (int y = 0; y < level.getHeight(); y++) {
                GridPoint2 cell = new GridPoint2(x, y);
                if (level.isFree(cell)) {
                    cells.add(cell);
                }
            }
        }
        return cells;
    }
}
