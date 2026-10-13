package ru.mipt.bit.platformer.input;

import com.badlogic.gdx.Input;
import com.badlogic.gdx.InputAdapter;
import ru.mipt.bit.platformer.model.Direction;
import ru.mipt.bit.platformer.model.Movable;
import ru.mipt.bit.platformer.model.ObstacleMap;

import java.util.HashMap;
import java.util.Map;

public class InputHandler extends InputAdapter {
    private final Map<Integer, Runnable> keyBindings = new HashMap<>();

    public InputHandler(Movable tank, ObstacleMap obstacles) {
        bind(tank, obstacles, Direction.UP, Input.Keys.UP, Input.Keys.W);
        bind(tank, obstacles, Direction.DOWN, Input.Keys.DOWN, Input.Keys.S);
        bind(tank, obstacles, Direction.LEFT, Input.Keys.LEFT, Input.Keys.A);
        bind(tank, obstacles, Direction.RIGHT, Input.Keys.RIGHT, Input.Keys.D);
    }

    private void bind(Movable tank, ObstacleMap obstacles, Direction direction, int... keys) {
        for (int key : keys) {
            keyBindings.put(key, () -> tank.tryMove(direction, obstacles));
        }
    }

    @Override
    public boolean keyDown(int keycode) {
        Runnable action = keyBindings.get(keycode);
        if (action != null) {
            action.run();
            return true;
        }
        return false;
    }
}