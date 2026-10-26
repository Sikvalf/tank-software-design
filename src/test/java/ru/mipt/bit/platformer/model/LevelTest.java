package ru.mipt.bit.platformer.model;

import com.badlogic.gdx.math.GridPoint2;
import org.junit.jupiter.api.Test;

import java.util.List;

import static org.junit.jupiter.api.Assertions.*;

class LevelTest {

    @Test
    void cellsOutsideLevelAreNotFree() {
        Level level = new Level(3, 2, new Tank(new GridPoint2(0, 0), 0.4f), List.of());

        assertFalse(level.isFree(new GridPoint2(-1, 0)));
        assertFalse(level.isFree(new GridPoint2(0, -1)));
        assertFalse(level.isFree(new GridPoint2(3, 0)));
        assertFalse(level.isFree(new GridPoint2(0, 2)));
        assertTrue(level.isFree(new GridPoint2(2, 1)));
    }

    @Test
    void treeCellIsNotFree() {
        Level level = new Level(3, 3, new Tank(new GridPoint2(0, 0), 0.4f),
                List.of(new Tree(new GridPoint2(1, 1))));
        assertFalse(level.isFree(new GridPoint2(1, 1)));
    }

    @Test
    void standingTankOccupiesItsCell() {
        Tank player = new Tank(new GridPoint2(0, 0), 0.4f);
        Tank ai = new Tank(new GridPoint2(2, 2), 0.4f);
        Level level = new Level(5, 5, player, List.of(ai), List.of());

        assertFalse(level.isFree(new GridPoint2(0, 0)));
        assertFalse(level.isFree(new GridPoint2(2, 2)));
        assertTrue(level.isFree(new GridPoint2(1, 1)));
    }

    @Test
    void movingTankOccupiesBothSourceAndDestination() {
        Tank player = new Tank(new GridPoint2(0, 0), 0.4f);
        Tank ai = new Tank(new GridPoint2(2, 2), 0.4f);
        Level level = new Level(5, 5, player, List.of(ai), List.of());

        ai.tryMove(Direction.RIGHT, level);
        level.update(0.2f);

        assertTrue(ai.isMoving());
        assertFalse(level.isFree(new GridPoint2(2, 2)), "source cell must stay occupied");
        assertFalse(level.isFree(new GridPoint2(3, 2)), "destination cell must be occupied");
    }

    @Test
    void sourceCellIsReleasedWhenMovementFinishes() {
        Tank player = new Tank(new GridPoint2(0, 0), 0.4f);
        Level level = new Level(5, 5, player, List.of());

        player.tryMove(Direction.UP, level);
        level.update(0.4f);

        assertFalse(player.isMoving());
        assertTrue(level.isFree(new GridPoint2(0, 0)));
        assertFalse(level.isFree(new GridPoint2(0, 1)));
    }

    @Test
    void tankCannotDriveIntoCellsOfMovingTank() {
        Tank a = new Tank(new GridPoint2(0, 1), 0.4f);
        Tank b = new Tank(new GridPoint2(2, 0), 0.4f);
        Tank c = new Tank(new GridPoint2(2, 2), 0.4f);
        Level level = new Level(3, 3, a, List.of(b, c), List.of());

        a.tryMove(Direction.RIGHT, level);           // a едет (0,1) -> (1,1)
        assertTrue(a.isMoving());

        b.tryMove(Direction.UP, level);              // (2,1) свободна — едет
        assertTrue(b.isMoving());

        c.tryMove(Direction.DOWN, level);            // (2,1) уже занята b
        assertFalse(c.isMoving());
        c.tryMove(Direction.LEFT, level);            // (1,2) свободна
        assertTrue(c.isMoving());
    }

    @Test
    void updateMovesAllTanks() {
        Tank player = new Tank(new GridPoint2(0, 0), 0.4f);
        Tank ai = new Tank(new GridPoint2(3, 3), 0.4f);
        Level level = new Level(5, 5, player, List.of(ai), List.of());

        player.tryMove(Direction.RIGHT, level);
        ai.tryMove(Direction.LEFT, level);
        level.update(0.4f);

        assertEquals(new GridPoint2(1, 0), player.getCoordinates());
        assertEquals(new GridPoint2(2, 3), ai.getCoordinates());
    }

    @Test
    void allTanksStartsWithPlayer() {
        Tank player = new Tank(new GridPoint2(0, 0), 0.4f);
        Tank ai = new Tank(new GridPoint2(1, 0), 0.4f);
        Level level = new Level(2, 1, player, List.of(ai), List.of());

        assertEquals(List.of(player, ai), level.getAllTanks());
        assertEquals(List.of(ai), level.getAiTanks());
    }

    @Test
    void rejectsTankOutsideLevel() {
        assertThrows(IllegalArgumentException.class,
                () -> new Level(2, 2, new Tank(new GridPoint2(2, 0), 0.4f), List.of()));
    }

    @Test
    void rejectsTankOnTree() {
        assertThrows(IllegalArgumentException.class,
                () -> new Level(2, 2, new Tank(new GridPoint2(1, 1), 0.4f),
                        List.of(new Tree(new GridPoint2(1, 1)))));
    }

    @Test
    void rejectsTwoTanksInOneCell() {
        assertThrows(IllegalArgumentException.class,
                () -> new Level(2, 2, new Tank(new GridPoint2(0, 0), 0.4f),
                        List.of(new Tank(new GridPoint2(0, 0), 0.4f)), List.of()));
    }

    @Test
    void rejectsNonPositiveSize() {
        assertThrows(IllegalArgumentException.class,
                () -> new Level(0, 2, new Tank(new GridPoint2(0, 0), 0.4f), List.of()));
    }
}
