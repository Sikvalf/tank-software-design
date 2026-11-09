package ru.mipt.bit.platformer.control;

import ru.mipt.bit.platformer.view.HealthBarDecorator;

import java.util.List;

public class ToggleHealthBarsCommand implements Command {

    private final List<HealthBarDecorator> decorators;

    public ToggleHealthBarsCommand(List<HealthBarDecorator> decorators) {
        this.decorators = decorators;
    }

    @Override
    public void execute() {
        for (HealthBarDecorator decorator : decorators) {
            decorator.toggle();
        }
    }
}

