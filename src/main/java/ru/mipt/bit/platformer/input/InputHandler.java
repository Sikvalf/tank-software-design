package ru.mipt.bit.platformer.input;

import com.badlogic.gdx.Input;
import com.badlogic.gdx.InputAdapter;
import ru.mipt.bit.platformer.model.Direction;
import ru.mipt.bit.platformer.model.GameObject;
import ru.mipt.bit.platformer.model.Movable;

import java.util.HashMap;
import java.util.Map;

public class InputHandler extends InputAdapter {
    private final Map<Integer, Runnable> keyBindings = new HashMap<>();

    public InputHandler(Movable tank, GameObject obstacle) {
        keyBindings.put(Input.Keys.UP, () -> tank.tryMove(Direction.UP, obstacle.getCoordinates()));
        keyBindings.put(Input.Keys.W, () -> tank.tryMove(Direction.UP, obstacle.getCoordinates()));
        keyBindings.put(Input.Keys.DOWN, () -> tank.tryMove(Direction.DOWN, obstacle.getCoordinates()));
        keyBindings.put(Input.Keys.S, () -> tank.tryMove(Direction.DOWN, obstacle.getCoordinates()));
        keyBindings.put(Input.Keys.LEFT, () -> tank.tryMove(Direction.LEFT, obstacle.getCoordinates()));
        keyBindings.put(Input.Keys.A, () -> tank.tryMove(Direction.LEFT, obstacle.getCoordinates()));
        keyBindings.put(Input.Keys.RIGHT, () -> tank.tryMove(Direction.RIGHT, obstacle.getCoordinates()));
        keyBindings.put(Input.Keys.D, () -> tank.tryMove(Direction.RIGHT, obstacle.getCoordinates()));
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