package ru.mipt.bit.platformer.control;

import com.badlogic.gdx.math.GridPoint2;
import org.junit.jupiter.api.Test;
import ru.mipt.bit.platformer.model.Direction;
import ru.mipt.bit.platformer.model.Level;
import ru.mipt.bit.platformer.model.Tank;

import java.util.EnumSet;
import java.util.List;
import java.util.Optional;
import java.util.Random;
import java.util.Set;

import static org.junit.jupiter.api.Assertions.*;

class RandomControlStrategyTest {

    @Test
    void returnsMoveCommandWhenTankStands() {
        Tank tank = new Tank(new GridPoint2(2, 2), 0.4f);
        Level level = new Level(5, 5, tank, List.of());

        Optional<Command> command = new RandomControlStrategy(new Random(1)).nextCommand(tank, level);

        assertTrue(command.isPresent());
        assertInstanceOf(MoveCommand.class, command.get());
    }

    @Test
    void returnsNothingWhileTankIsMoving() {
        Tank tank = new Tank(new GridPoint2(2, 2), 0.4f);
        Level level = new Level(5, 5, tank, List.of());
        tank.tryMove(Direction.UP, level);

        assertTrue(new RandomControlStrategy(new Random(1)).nextCommand(tank, level).isEmpty());
    }

    @Test
    void choosesAllDirectionsOverTime() {
        Tank tank = new Tank(new GridPoint2(2, 2), 0.4f);
        Level level = new Level(5, 5, tank, List.of());
        RandomControlStrategy strategy = new RandomControlStrategy(new Random(42));

        Set<Direction> seen = EnumSet.noneOf(Direction.class);
        for (int i = 0; i < 100; i++) {
            MoveCommand command = (MoveCommand) strategy.nextCommand(tank, level).orElseThrow();
            seen.add(command.getDirection());
        }
        assertEquals(EnumSet.allOf(Direction.class), seen);
    }

    @Test
    void sameSeedGivesSameDirections() {
        Tank tank = new Tank(new GridPoint2(2, 2), 0.4f);
        Level level = new Level(5, 5, tank, List.of());
        RandomControlStrategy a = new RandomControlStrategy(new Random(7));
        RandomControlStrategy b = new RandomControlStrategy(new Random(7));

        for (int i = 0; i < 20; i++) {
            assertEquals(((MoveCommand) a.nextCommand(tank, level).orElseThrow()).getDirection(),
                    ((MoveCommand) b.nextCommand(tank, level).orElseThrow()).getDirection());
        }
    }
}
