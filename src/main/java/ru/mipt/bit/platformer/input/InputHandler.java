package ru.mipt.bit.platformer.input;

import com.badlogic.gdx.Input;
import com.badlogic.gdx.InputAdapter;
import ru.mipt.bit.platformer.control.DirectionInput;
import ru.mipt.bit.platformer.model.Direction;

import java.util.HashMap;
import java.util.Map;
import java.util.Optional;

/**
 * Запоминает последнее нажатое направление; стратегия игрока забирает его на следующем кадре.
 */
public class InputHandler extends InputAdapter implements DirectionInput {
    private final Map<Integer, Direction> keyBindings = new HashMap<>();
    private Direction pendingDirection;

    public InputHandler() {
        bind(Direction.UP, Input.Keys.UP, Input.Keys.W);
        bind(Direction.DOWN, Input.Keys.DOWN, Input.Keys.S);
        bind(Direction.LEFT, Input.Keys.LEFT, Input.Keys.A);
        bind(Direction.RIGHT, Input.Keys.RIGHT, Input.Keys.D);
    }

    private void bind(Direction direction, int... keys) {
        for (int key : keys) {
            keyBindings.put(key, direction);
        }
    }

    @Override
    public boolean keyDown(int keycode) {
        Direction direction = keyBindings.get(keycode);
        if (direction != null) {
            pendingDirection = direction;
            return true;
        }
        return false;
    }

    @Override
    public Optional<Direction> pollDirection() {
        Optional<Direction> result = Optional.ofNullable(pendingDirection);
        pendingDirection = null;
        return result;
    }
}
