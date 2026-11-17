package ru.mipt.bit.platformer.view;

import com.badlogic.gdx.math.Rectangle;

public interface BoundedRenderable extends Renderable {
    Rectangle getRectangle();
}
