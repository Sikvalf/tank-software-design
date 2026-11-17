package ru.mipt.bit.platformer.input;

import com.badlogic.gdx.Input;
import com.badlogic.gdx.InputAdapter;
import ru.mipt.bit.platformer.control.Command;
import ru.mipt.bit.platformer.control.DirectionInput;
import ru.mipt.bit.platformer.control.ShootInput;
import ru.mipt.bit.platformer.model.Direction;

import java.util.HashMap;
import java.util.Map;
import java.util.Optional;

public class InputHandler extends InputAdapter implements DirectionInput, ShootInput {

    private Direction pendingDirection;
    private boolean pendingShoot;
    private final Map<Integer, Command> actions = new HashMap<>();

    public InputHandler() {
        bind(Direction.UP, Input.Keys.UP, Input.Keys.W);
        bind(Direction.DOWN, Input.Keys.DOWN, Input.Keys.S);
        bind(Direction.LEFT, Input.Keys.LEFT, Input.Keys.A);
        bind(Direction.RIGHT, Input.Keys.RIGHT, Input.Keys.D);
        bindAction(Input.Keys.SPACE, () -> pendingShoot = true);
    }

    public void bindAction(int key, Command command) {
        actions.put(key, java.util.Objects.requireNonNull(command));
    }

    private void bind(Direction direction, int... keys) {
        for (int key : keys) {
            bindAction(key, () -> pendingDirection = direction);
        }
    }

    @Override
    public boolean keyDown(int keycode) {
        Command action = actions.get(keycode);
        if (action == null) {
            return false;
        }
        action.execute();
        return true;
    }

    @Override
    public Optional<Direction> pollDirection() {
        Optional<Direction> result = Optional.ofNullable(pendingDirection);
        pendingDirection = null;
        return result;
    }

    @Override
    public boolean pollShoot() {
        boolean result = pendingShoot;
        pendingShoot = false;
        return result;
    }
}
