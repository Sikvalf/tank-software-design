package ru.mipt.bit.platformer.level;

import com.badlogic.gdx.math.GridPoint2;
import org.junit.jupiter.api.Test;
import ru.mipt.bit.platformer.model.Level;
import ru.mipt.bit.platformer.model.Tree;

import java.util.HashSet;
import java.util.Random;
import java.util.Set;

import static org.junit.jupiter.api.Assertions.*;

class RandomLevelGeneratorTest {

    @Test
    void generatesRequestedNumberOfTreesInsideLevel() {
        Level level = new RandomLevelGenerator(10, 8, 15, 0.4f, new Random(42)).generate();

        assertEquals(10, level.getWidth());
        assertEquals(8, level.getHeight());
        assertEquals(15, level.getTrees().size());
        for (Tree tree : level.getTrees()) {
            assertTrue(level.isInside(tree.getCoordinates()));
        }
        assertTrue(level.isInside(level.getPlayerTank().getCoordinates()));
    }

    @Test
    void treesDoNotOverlapEachOtherOrPlayer() {
        for (long seed = 0; seed < 100; seed++) {
            Level level = new RandomLevelGenerator(10, 8, 40, 0.4f, new Random(seed)).generate();
            Set<GridPoint2> cells = new HashSet<>();
            for (Tree tree : level.getTrees()) {
                assertTrue(cells.add(tree.getCoordinates()), "duplicate tree, seed " + seed);
            }
            assertFalse(cells.contains(level.getPlayerTank().getCoordinates()), "tank on tree, seed " + seed);
        }
    }

    @Test
    void playerPositionIsRandom() {
        Set<GridPoint2> positions = new HashSet<>();
        for (long seed = 0; seed < 50; seed++) {
            positions.add(new RandomLevelGenerator(10, 8, 15, 0.4f, new Random(seed))
                    .generate().getPlayerTank().getCoordinates());
        }
        assertTrue(positions.size() > 1);
    }

    @Test
    void sameSeedGivesSameLevel() {
        Level a = new RandomLevelGenerator(10, 8, 15, 0.4f, new Random(7)).generate();
        Level b = new RandomLevelGenerator(10, 8, 15, 0.4f, new Random(7)).generate();
        assertEquals(a.getPlayerTank().getCoordinates(), b.getPlayerTank().getCoordinates());
        for (int i = 0; i < a.getTrees().size(); i++) {
            assertEquals(a.getTrees().get(i).getCoordinates(), b.getTrees().get(i).getCoordinates());
        }
    }

    @Test
    void rejectsTooManyTrees() {
        assertThrows(IllegalArgumentException.class,
                () -> new RandomLevelGenerator(2, 2, 4, 0.4f, new Random()));
    }
}