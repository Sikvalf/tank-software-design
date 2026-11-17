package ru.mipt.bit.platformer.control;

import com.badlogic.gdx.math.GridPoint2;
import org.junit.jupiter.api.Test;
import ru.mipt.bit.platformer.model.Direction;
import ru.mipt.bit.platformer.model.Level;
import ru.mipt.bit.platformer.model.Movable;
import ru.mipt.bit.platformer.model.ObstacleMap;
import ru.mipt.bit.platformer.model.Tank;
import ru.mipt.bit.platformer.model.Tree;

import java.util.List;

import static org.junit.jupiter.api.Assertions.*;

class MoveCommandTest {

    @Test
    void executeStartsMovementToFreeCell() {
        Tank tank = new Tank(new GridPoint2(1, 1), 0.4f);
        Level level = new Level(3, 3, tank, List.of());

        new MoveCommand(tank, Direction.UP, level).execute();

        assertTrue(tank.isMoving());
        assertEquals(new GridPoint2(1, 2), tank.getDestinationCoordinates());
    }

    @Test
    void executeDoesNothingWhenCellIsBlocked() {
        Tank tank = new Tank(new GridPoint2(1, 1), 0.4f);
        Level level = new Level(3, 3, tank, List.of(new Tree(new GridPoint2(1, 2))));

        new MoveCommand(tank, Direction.UP, level).execute();

        assertFalse(tank.isMoving());
        assertEquals(new GridPoint2(1, 1), tank.getDestinationCoordinates());
    }

    @Test
    void executeCallsMovableForEveryDirection() {
        RecordingMovable movable = new RecordingMovable();
        ObstacleMap obstacles = cell -> true;

        for (Direction direction : Direction.values()) {
            MoveCommand command = new MoveCommand(movable, direction, obstacles);
            int previousCalls = movable.calls;
            command.execute();
            assertEquals(previousCalls + 1, movable.calls);
            assertSame(direction, movable.direction);
            assertSame(obstacles, movable.obstacles);
        }
    }

    private static class RecordingMovable implements Movable {
        private int calls;
        private Direction direction;
        private ObstacleMap obstacles;

        @Override
        public void tryMove(Direction direction, ObstacleMap obstacles) {
            calls++;
            this.direction = direction;
            this.obstacles = obstacles;
        }

        @Override
        public boolean isMoving() { return false; }

        @Override
        public GridPoint2 getCoordinates() { return new GridPoint2(); }

        @Override
        public GridPoint2 getDestinationCoordinates() { return new GridPoint2(); }

        @Override
        public float getMovementProgress() { return 0; }

        @Override
        public float getRotation() { return 0; }
    }

    @Test
    void commandIsNotExecutedOnCreation() {
        Tank tank = new Tank(new GridPoint2(1, 1), 0.4f);
        Level level = new Level(3, 3, tank, List.of());

        MoveCommand command = new MoveCommand(tank, Direction.DOWN, level);

        assertFalse(tank.isMoving());
        assertEquals(Direction.DOWN, command.getDirection());
    }
}
