package ru.mipt.bit.platformer.model;

import com.badlogic.gdx.math.GridPoint2;
import org.junit.jupiter.api.Test;

import java.util.List;

import static org.junit.jupiter.api.Assertions.*;

class TankTest {
    @Test
    void testTankMovesToDestination() {
        Tank tank = new Tank(new GridPoint2(1, 1), 0.4f);
        Level level = new Level(10, 8, tank, List.of(new Tree(new GridPoint2(5, 5))));

        tank.tryMove(Direction.UP, level);
        assertTrue(tank.getMovementProgress() < 1f);

        tank.update(0.4f);
        assertEquals(new GridPoint2(1, 2), tank.getCoordinates());
    }

    @Test
    void testTankCannotMoveIntoObstacle() {
        Tank tank = new Tank(new GridPoint2(1, 1), 0.4f);
        Level level = new Level(10, 8, tank, List.of(new Tree(new GridPoint2(1, 2))));

        tank.tryMove(Direction.UP, level);
        assertEquals(1f, tank.getMovementProgress());
        assertEquals(new GridPoint2(1, 1), tank.getCoordinates());
    }

    @Test
    void testTankCannotLeaveLevel() {
        Tank tank = new Tank(new GridPoint2(0, 0), 0.4f);
        Level level = new Level(10, 8, tank, List.of());

        tank.tryMove(Direction.LEFT, level);
        assertEquals(1f, tank.getMovementProgress());
        tank.tryMove(Direction.DOWN, level);
        assertEquals(1f, tank.getMovementProgress());
        assertEquals(new GridPoint2(0, 0), tank.getCoordinates());
    }

    @Test
    void testTankCannotLeaveLevelThroughAnySide() {
        Tank tank = new Tank(new GridPoint2(1, 1), 0.4f);
        Level level = new Level(2, 2, tank, List.of());

        tank.tryMove(Direction.UP, level);
        assertFalse(tank.isMoving());
        tank.tryMove(Direction.RIGHT, level);
        assertFalse(tank.isMoving());
        assertEquals(new GridPoint2(1, 1), tank.getCoordinates());
    }

    @Test
    void testTankRotatesEvenIfBlocked() {
        Tank tank = new Tank(new GridPoint2(0, 0), 0.4f);
        Level level = new Level(3, 3, tank, List.of());

        tank.tryMove(Direction.LEFT, level);
        assertEquals(Direction.LEFT.getRotation(), tank.getRotation());
    }

    @Test
    void testTankIgnoresCommandsWhileMoving() {
        Tank tank = new Tank(new GridPoint2(1, 1), 0.4f);
        Level level = new Level(5, 5, tank, List.of());

        tank.tryMove(Direction.UP, level);
        tank.update(0.1f);
        tank.tryMove(Direction.RIGHT, level);

        assertEquals(new GridPoint2(1, 2), tank.getDestinationCoordinates());
        assertEquals(Direction.UP.getRotation(), tank.getRotation());
    }

    @Test
    void testTankIsMovingUntilProgressReachesOne() {
        Tank tank = new Tank(new GridPoint2(1, 1), 0.4f);
        Level level = new Level(5, 5, tank, List.of());

        assertFalse(tank.isMoving());
        tank.tryMove(Direction.UP, level);
        assertTrue(tank.isMoving());
        tank.update(0.2f);
        assertTrue(tank.isMoving());
        assertEquals(new GridPoint2(1, 1), tank.getCoordinates());
        tank.update(0.2f);
        assertFalse(tank.isMoving());
        assertEquals(new GridPoint2(1, 2), tank.getCoordinates());
    }

    @Test
    void testOccupiesSourceAndDestination() {
        Tank tank = new Tank(new GridPoint2(1, 1), 0.4f);
        Level level = new Level(5, 5, tank, List.of());

        tank.tryMove(Direction.RIGHT, level);
        assertTrue(tank.occupies(new GridPoint2(1, 1)));
        assertTrue(tank.occupies(new GridPoint2(2, 1)));
        assertFalse(tank.occupies(new GridPoint2(3, 1)));
    }
}
