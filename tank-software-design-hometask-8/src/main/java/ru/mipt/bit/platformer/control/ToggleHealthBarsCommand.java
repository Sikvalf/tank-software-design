package ru.mipt.bit.platformer.control;

import ru.mipt.bit.platformer.view.HealthBarVisibilityState;

public class ToggleHealthBarsCommand implements Command {
    private final HealthBarVisibilityState visibility;

    public ToggleHealthBarsCommand(HealthBarVisibilityState visibility) {
        this.visibility = visibility;
    }

    @Override
    public void execute() {
        visibility.toggle();
    }
}
