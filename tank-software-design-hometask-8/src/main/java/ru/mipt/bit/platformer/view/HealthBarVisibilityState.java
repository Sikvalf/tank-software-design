package ru.mipt.bit.platformer.view;

public final class HealthBarVisibilityState {
    private boolean visible;

    public boolean isVisible() {
        return visible;
    }

    public void toggle() {
        visible = !visible;
    }
}
