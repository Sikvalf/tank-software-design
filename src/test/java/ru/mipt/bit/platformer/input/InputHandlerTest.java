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

        assertFalse(handler.keyDown(Input.Keys.SPACE));
        assertEquals(Optional.empty(), handler.pollDirection());
    }
}
