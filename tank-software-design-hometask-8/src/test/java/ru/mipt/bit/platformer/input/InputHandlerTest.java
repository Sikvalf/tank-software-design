package ru.mipt.bit.platformer.input;

import com.badlogic.gdx.Input;
import org.junit.jupiter.api.Test;
import ru.mipt.bit.platformer.model.Direction;

import java.util.Optional;

import static org.junit.jupiter.api.Assertions.*;

class InputHandlerTest {

    @Test
    void arrowsAndWasdMapToDirections() {
        InputHandler handler = new InputHandler();
        int[][] bindings = {
                {Input.Keys.UP, Input.Keys.W},
                {Input.Keys.DOWN, Input.Keys.S},
                {Input.Keys.LEFT, Input.Keys.A},
                {Input.Keys.RIGHT, Input.Keys.D},
        };
        Direction[] expected = {Direction.UP, Direction.DOWN, Direction.LEFT, Direction.RIGHT};

        for (int i = 0; i < bindings.length; i++) {
            for (int key : bindings[i]) {
                assertTrue(handler.keyDown(key));
                assertEquals(Optional.of(expected[i]), handler.pollDirection());
            }
        }
    }

    @Test
    void directionIsConsumedOnce() {
        InputHandler handler = new InputHandler();
        handler.keyDown(Input.Keys.W);

        assertEquals(Optional.of(Direction.UP), handler.pollDirection());
        assertEquals(Optional.empty(), handler.pollDirection());
    }

    @Test
    void lastPressedKeyWins() {
        InputHandler handler = new InputHandler();
        handler.keyDown(Input.Keys.W);
        handler.keyDown(Input.Keys.D);

        assertEquals(Optional.of(Direction.RIGHT), handler.pollDirection());
    }

    @Test
    void unknownKeyIsIgnored() {
        InputHandler handler = new InputHandler();

        assertFalse(handler.keyDown(Input.Keys.F1));
        assertEquals(Optional.empty(), handler.pollDirection());
    }
    @Test
    void registeredActionExecutesOnlyOnBoundKey() {
        InputHandler handler = new InputHandler();
        int[] calls = {0};
        handler.bindAction(Input.Keys.L, () -> calls[0]++);

        assertFalse(handler.keyDown(Input.Keys.F1));
        assertEquals(0, calls[0]);
        assertTrue(handler.keyDown(Input.Keys.L));
        assertEquals(1, calls[0]);
        assertEquals(Optional.empty(), handler.pollDirection());
    }

    @Test
    void actionCanBeReboundWithoutInvokingPreviousAction() {
        InputHandler handler = new InputHandler();
        int[] first = {0};
        int[] second = {0};
        handler.bindAction(Input.Keys.L, () -> first[0]++);
        handler.bindAction(Input.Keys.L, () -> second[0]++);

        assertTrue(handler.keyDown(Input.Keys.L));
        assertEquals(0, first[0]);
        assertEquals(1, second[0]);
    }

    @Test
    void shootInputIsConsumedOnlyOnce() {
        InputHandler handler = new InputHandler();
        assertTrue(handler.keyDown(Input.Keys.SPACE));
        assertTrue(handler.pollShoot());
        assertFalse(handler.pollShoot());
    }

    @Test
    void repeatedKeyDownEventsAreHandled() {
        InputHandler handler = new InputHandler();
        assertTrue(handler.keyDown(Input.Keys.W));
        assertEquals(Optional.of(Direction.UP), handler.pollDirection());
        assertTrue(handler.keyDown(Input.Keys.W));
        assertEquals(Optional.of(Direction.UP), handler.pollDirection());
    }

    @Test
    void directionInputCanDriveMovableThroughStrategy() {
        InputHandler handler = new InputHandler();
        com.badlogic.gdx.math.GridPoint2 start = new com.badlogic.gdx.math.GridPoint2(1, 1);
        ru.mipt.bit.platformer.model.Tank tank = new ru.mipt.bit.platformer.model.Tank(start, 0.4f);
        ru.mipt.bit.platformer.model.Level level = new ru.mipt.bit.platformer.model.Level(4, 4, tank, java.util.List.of());
        ru.mipt.bit.platformer.control.PlayerControlStrategy strategy = new ru.mipt.bit.platformer.control.PlayerControlStrategy(handler);

        handler.keyDown(Input.Keys.D);
        strategy.nextCommand(tank, level).orElseThrow().execute();
        assertEquals(new com.badlogic.gdx.math.GridPoint2(2, 1), tank.getDestinationCoordinates());
    }

    @Test
    void heldKeyDoesNotGenerateAdditionalInputWithoutKeyDownEvents() {
        InputHandler handler = new InputHandler();

        assertTrue(handler.keyDown(Input.Keys.W));
        assertEquals(Optional.of(Direction.UP), handler.pollDirection());
        assertEquals(Optional.empty(), handler.pollDirection());
        assertEquals(Optional.empty(), handler.pollDirection());
        assertFalse(handler.keyUp(Input.Keys.W));
        assertEquals(Optional.empty(), handler.pollDirection());
        assertTrue(handler.keyDown(Input.Keys.W));
        assertEquals(Optional.of(Direction.UP), handler.pollDirection());
    }

    @Test
    void registeredActionsRunForEachKeyDownEvent() {
        InputHandler handler = new InputHandler();
        int[] calls = {0};
        handler.bindAction(Input.Keys.L, () -> calls[0]++);

        assertTrue(handler.keyDown(Input.Keys.L));
        assertTrue(handler.keyDown(Input.Keys.L));
        assertFalse(handler.keyUp(Input.Keys.L));
        assertTrue(handler.keyDown(Input.Keys.L));
        assertEquals(3, calls[0]);
    }

    @Test
    void customActionCanReplaceDefaultKeyBinding() {
        InputHandler handler = new InputHandler();
        int[] calls = {0};
        handler.bindAction(Input.Keys.W, () -> calls[0]++);

        assertTrue(handler.keyDown(Input.Keys.W));
        assertEquals(1, calls[0]);
        assertEquals(Optional.empty(), handler.pollDirection());
    }

    @Test
    void nullActionIsRejected() {
        InputHandler handler = new InputHandler();
        assertThrows(NullPointerException.class, () -> handler.bindAction(Input.Keys.L, null));
    }
}
