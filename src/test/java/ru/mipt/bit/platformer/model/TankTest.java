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
}