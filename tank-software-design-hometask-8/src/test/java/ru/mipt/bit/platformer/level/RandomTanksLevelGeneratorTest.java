package ru.mipt.bit.platformer.level;

import com.badlogic.gdx.math.GridPoint2;
import org.junit.jupiter.api.Test;
import ru.mipt.bit.platformer.model.Level;
import ru.mipt.bit.platformer.model.Tank;
import ru.mipt.bit.platformer.model.Tree;

import java.io.StringReader;
import java.util.HashSet;
import java.util.List;
import java.util.Random;
import java.util.Set;

import static org.junit.jupiter.api.Assertions.*;

class RandomTanksLevelGeneratorTest {

    private static LevelGenerator fileLevel(String text) {
        return new FileLevelGenerator(() -> new StringReader(text), 0.4f);
    }

    @Test
    void addsRequestedNumberOfAiTanks() {
        Level level = new RandomTanksLevelGenerator(
                new RandomLevelGenerator(10, 8, 15, 0.4f, new Random(1)), 5, 0.4f, new Random(2)).generate();

        assertEquals(5, level.getAiTanks().size());
        assertEquals(6, level.getAllTanks().size());
    }

    @Test
    void keepsBaseLevelUntouched() {
        LevelGenerator base = fileLevel("T__\n___\nX__\n");
        Level level = new RandomTanksLevelGenerator(base, 2, 0.4f, new Random(3)).generate();

        assertEquals(3, level.getWidth());
        assertEquals(3, level.getHeight());
        assertEquals(new GridPoint2(0, 0), level.getPlayerTank().getCoordinates());
        assertEquals(1, level.getTrees().size());
    }

    @Test
    void tanksAreInsideAndDoNotOverlapAnything() {
        for (long seed = 0; seed < 100; seed++) {
            Level level = new RandomTanksLevelGenerator(
                    new RandomLevelGenerator(10, 8, 30, 0.4f, new Random(seed)), 20, 0.4f, new Random(seed)).generate();

            Set<GridPoint2> used = new HashSet<>();
            for (Tree tree : level.getTrees()) {
                used.add(tree.getCoordinates());
            }
            for (Tank tank : level.getAllTanks()) {
                assertTrue(level.isInside(tank.getCoordinates()), "seed " + seed);
                assertTrue(used.add(tank.getCoordinates()), "overlap, seed " + seed);
            }
        }
    }

    @Test
    void fillsAllFreeCellsWhenAsked() {
        Level level = new RandomTanksLevelGenerator(fileLevel("XT\n__\n"), 2, 0.4f, new Random()).generate();

        assertEquals(2, level.getAiTanks().size());
        assertFalse(level.isFree(new GridPoint2(0, 0)));
        assertFalse(level.isFree(new GridPoint2(1, 0)));
    }

    @Test
    void positionsAreRandom() {
        Set<List<GridPoint2>> layouts = new HashSet<>();
        for (long seed = 0; seed < 20; seed++) {
            Level level = new RandomTanksLevelGenerator(fileLevel("X____\n_____\n"), 2, 0.4f, new Random(seed)).generate();
            layouts.add(List.of(level.getAiTanks().get(0).getCoordinates(), level.getAiTanks().get(1).getCoordinates()));
        }
        assertTrue(layouts.size() > 1);
    }

    @Test
    void zeroTanksIsAllowed() {
        Level level = new RandomTanksLevelGenerator(fileLevel("X_\n"), 0, 0.4f, new Random()).generate();
        assertTrue(level.getAiTanks().isEmpty());
    }

    @Test
    void failsWhenNotEnoughFreeCells() {
        RandomTanksLevelGenerator generator = new RandomTanksLevelGenerator(fileLevel("XT\n_T\n"), 2, 0.4f, new Random());
        assertThrows(IllegalStateException.class, generator::generate);
    }

    @Test
    void rejectsNegativeCount() {
        assertThrows(IllegalArgumentException.class,
                () -> new RandomTanksLevelGenerator(fileLevel("X\n"), -1, 0.4f, new Random()));
    }
}
