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

    private final Map<Integer, Direction> keyBindings = new HashMap<>();
    private Direction pendingDirection;
    private boolean pendingShoot;
    private Command toggleHealthBarsCommand;

    public InputHandler() {
        bind(Direction.UP, Input.Keys.UP, Input.Keys.W);
        bind(Direction.DOWN, Input.Keys.DOWN, Input.Keys.S);
        bind(Direction.LEFT, Input.Keys.LEFT, Input.Keys.A);
        bind(Direction.RIGHT, Input.Keys.RIGHT, Input.Keys.D);
    }

    public void setToggleHealthBarsCommand(Command command) {
        this.toggleHealthBarsCommand = command;
    }

    private void bind(Direction direction, int... keys) {
        for (int key : keys) {
            keyBindings.put(key, direction);
        }
    }

    @Override
    public boolean keyDown(int keycode) {
        if (keycode == Input.Keys.L) {
            if (toggleHealthBarsCommand != null) {
                toggleHealthBarsCommand.execute();
            }
            return true;
        }

        if (keycode == Input.Keys.SPACE) {
            pendingShoot = true;
            return true;
        }

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

    @Override
    public boolean pollShoot() {
        boolean result = pendingShoot;
        pendingShoot = false;
        return result;
    }
}
