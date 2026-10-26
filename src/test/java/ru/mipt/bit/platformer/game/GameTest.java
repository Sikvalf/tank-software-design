package ru.mipt.bit.platformer.game;

import com.badlogic.gdx.math.GridPoint2;
import org.junit.jupiter.api.Test;
import ru.mipt.bit.platformer.control.ControlStrategy;
import ru.mipt.bit.platformer.control.MoveCommand;
import ru.mipt.bit.platformer.control.RandomControlStrategy;
import ru.mipt.bit.platformer.control.TankController;
import ru.mipt.bit.platformer.model.Direction;
import ru.mipt.bit.platformer.model.Level;
import ru.mipt.bit.platformer.model.Tank;
import ru.mipt.bit.platformer.model.Tree;

import java.util.ArrayList;
import java.util.HashSet;
import java.util.List;
import java.util.Optional;
import java.util.Random;
import java.util.Set;

import static org.junit.jupiter.api.Assertions.*;

class GameTest {

    private static ControlStrategy always(Direction direction) {
        return (tank, obstacles) -> Optional.of(new MoveCommand(tank, direction, obstacles));
    }

    private static final ControlStrategy IDLE = (tank, obstacles) -> Optional.empty();

    @Test
    void factoryGivesPlayerStrategyToPlayerAndAiToOthers() {
        Tank player = new Tank(new GridPoint2(0, 0), 0.4f);
        Tank ai1 = new Tank(new GridPoint2(2, 0), 0.4f);
        Tank ai2 = new Tank(new GridPoint2(4, 0), 0.4f);
        Level level = new Level(5, 3, player, List.of(ai1, ai2), List.of());
        List<ControlStrategy> created = new ArrayList<>();

        Game game = GameFactory.create(level, always(Direction.UP), () -> {
            ControlStrategy s = always(Direction.UP);
            created.add(s);
            return s;
        });

        assertEquals(3, game.getControllers().size());
        assertSame(player, game.getControllers().get(0).getTank());
        assertEquals(2, created.size(), "each AI tank gets its own strategy");

        game.update(0.4f);
        assertEquals(new GridPoint2(0, 1), player.getCoordinates());
        assertEquals(new GridPoint2(2, 1), ai1.getCoordinates());
        assertEquals(new GridPoint2(4, 1), ai2.getCoordinates());
    }

    @Test
    void twoTanksCannotTakeSameCellInOneFrame() {
        Tank left = new Tank(new GridPoint2(0, 0), 0.4f);
        Tank right = new Tank(new GridPoint2(2, 0), 0.4f);
        Level level = new Level(3, 1, left, List.of(right), List.of());
        Game game = new Game(level, List.of(
                new TankController(left, always(Direction.RIGHT)),
                new TankController(right, always(Direction.LEFT))));

        game.update(0.2f);

        assertTrue(left.isMoving());
        assertFalse(right.isMoving());
        assertEquals(new GridPoint2(1, 0), left.getDestinationCoordinates());
    }

    @Test
    void playerTankIsNotMovedWithoutInput() {
        Tank player = new Tank(new GridPoint2(0, 0), 0.4f);
        Tank ai = new Tank(new GridPoint2(2, 2), 0.4f);
        Level level = new Level(3, 3, player, List.of(ai), List.of());
        Game game = GameFactory.create(level, IDLE, () -> always(Direction.LEFT));

        game.update(0.4f);

        assertEquals(new GridPoint2(0, 0), player.getCoordinates());
        assertEquals(new GridPoint2(1, 2), ai.getCoordinates());
    }

    @Test
    void randomTanksNeverCollideOrLeaveLevel() {
        for (long seed = 0; seed < 20; seed++) {
            Tank player = new Tank(new GridPoint2(0, 0), 0.4f);
            List<Tank> ai = List.of(
                    new Tank(new GridPoint2(1, 1), 0.4f),
                    new Tank(new GridPoint2(2, 2), 0.4f),
                    new Tank(new GridPoint2(3, 0), 0.4f),
                    new Tank(new GridPoint2(3, 3), 0.4f));
            Level level = new Level(4, 4, player, ai, List.of(new Tree(new GridPoint2(1, 2))));
            Random random = new Random(seed);
            Game game = GameFactory.create(level, IDLE, () -> new RandomControlStrategy(random));

            for (int frame = 0; frame < 500; frame++) {
                game.update(0.13f);
                assertNoConflicts(level, seed, frame);
            }
        }
    }

    private static void assertNoConflicts(Level level, long seed, int frame) {
        Set<GridPoint2> occupied = new HashSet<>();
        occupied.add(new GridPoint2(1, 2)); // дерево
        for (Tank tank : level.getAllTanks()) {
            String where = "seed " + seed + ", frame " + frame;
            assertTrue(level.isInside(tank.getCoordinates()), where);
            assertTrue(level.isInside(tank.getDestinationCoordinates()), where);
            assertTrue(occupied.add(new GridPoint2(tank.getCoordinates())), where);
            if (!tank.getDestinationCoordinates().equals(tank.getCoordinates())) {
                assertTrue(occupied.add(new GridPoint2(tank.getDestinationCoordinates())), where);
            }
        }
    }

    @Test
    void randomAiActuallyMoves() {
        Tank player = new Tank(new GridPoint2(0, 0), 0.4f);
        Tank ai = new Tank(new GridPoint2(5, 5), 0.4f);
        Level level = new Level(10, 10, player, List.of(ai), List.of());
        Random random = new Random(5);
        Game game = GameFactory.create(level, IDLE, () -> new RandomControlStrategy(random));

        Set<GridPoint2> visited = new HashSet<>();
        for (int frame = 0; frame < 200; frame++) {
            game.update(0.1f);
            visited.add(new GridPoint2(ai.getCoordinates()));
        }
        assertTrue(visited.size() > 3);
    }
}
