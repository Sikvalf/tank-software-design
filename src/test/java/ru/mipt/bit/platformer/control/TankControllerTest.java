package ru.mipt.bit.platformer.control;

import com.badlogic.gdx.math.GridPoint2;
import org.junit.jupiter.api.Test;
import ru.mipt.bit.platformer.model.Direction;
import ru.mipt.bit.platformer.model.Level;
import ru.mipt.bit.platformer.model.Tank;

import java.util.List;
import java.util.Optional;

import static org.junit.jupiter.api.Assertions.*;

class TankControllerTest {

    @Test
    void executesCommandChosenByStrategy() {
        Tank tank = new Tank(new GridPoint2(1, 1), 0.4f);
        Level level = new Level(3, 3, tank, List.of());
        ControlStrategy alwaysRight = (t, obstacles) -> Optional.of(new MoveCommand(t, Direction.RIGHT, obstacles));

        new TankController(tank, alwaysRight).act(level);

        assertEquals(new GridPoint2(2, 1), tank.getDestinationCoordinates());
    }

    @Test
    void doesNothingWhenStrategyHasNoCommand() {
        Tank tank = new Tank(new GridPoint2(1, 1), 0.4f);
        Level level = new Level(3, 3, tank, List.of());
        TankController controller = new TankController(tank, (t, obstacles) -> Optional.empty());

        controller.act(level);

        assertFalse(tank.isMoving());
        assertSame(tank, controller.getTank());
    }

    @Test
    void passesItsTankAndObstaclesToStrategy() {
        Tank tank = new Tank(new GridPoint2(1, 1), 0.4f);
        Level level = new Level(3, 3, tank, List.of());
        Object[] received = new Object[2];

        new TankController(tank, (t, obstacles) -> {
            received[0] = t;
            received[1] = obstacles;
            return Optional.empty();
        }).act(level);

        assertSame(tank, received[0]);
        assertSame(level, received[1]);
    }
}
