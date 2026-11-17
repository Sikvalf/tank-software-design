package ru.mipt.bit.platformer.control;

import com.badlogic.gdx.math.GridPoint2;
import org.junit.jupiter.api.Test;
import ru.mipt.bit.platformer.model.Direction;
import ru.mipt.bit.platformer.model.Level;
import ru.mipt.bit.platformer.model.Tank;

import java.util.ArrayDeque;
import java.util.Deque;
import java.util.List;
import java.util.Optional;

import static org.junit.jupiter.api.Assertions.*;

class PlayerControlStrategyTest {

    private static class FakeInput implements DirectionInput {
        private final Deque<Direction> directions = new ArrayDeque<>();

        void press(Direction direction) {
            directions.add(direction);
        }

        @Override
        public Optional<Direction> pollDirection() {
            return Optional.ofNullable(directions.poll());
        }
    }

    @Test
    void noInputMeansNoCommand() {
        Tank tank = new Tank(new GridPoint2(1, 1), 0.4f);
        Level level = new Level(3, 3, tank, List.of());

        assertTrue(new PlayerControlStrategy(new FakeInput()).nextCommand(tank, level).isEmpty());
    }

    @Test
    void pressedDirectionBecomesMoveCommand() {
        Tank tank = new Tank(new GridPoint2(1, 1), 0.4f);
        Level level = new Level(3, 3, tank, List.of());
        FakeInput input = new FakeInput();
        input.press(Direction.LEFT);

        Optional<Command> command = new PlayerControlStrategy(input).nextCommand(tank, level);

        assertEquals(Direction.LEFT, ((MoveCommand) command.orElseThrow()).getDirection());
        command.get().execute();
        assertEquals(new GridPoint2(0, 1), tank.getDestinationCoordinates());
    }
}
