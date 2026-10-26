package ru.mipt.bit.platformer.control;

import com.badlogic.gdx.math.GridPoint2;
import org.junit.jupiter.api.Test;
import ru.mipt.bit.platformer.model.Direction;
import ru.mipt.bit.platformer.model.Level;
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
    void commandIsNotExecutedOnCreation() {
        Tank tank = new Tank(new GridPoint2(1, 1), 0.4f);
        Level level = new Level(3, 3, tank, List.of());

        MoveCommand command = new MoveCommand(tank, Direction.DOWN, level);

        assertFalse(tank.isMoving());
        assertEquals(Direction.DOWN, command.getDirection());
    }
}
