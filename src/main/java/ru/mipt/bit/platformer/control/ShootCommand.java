package ru.mipt.bit.platformer.control;

import ru.mipt.bit.platformer.model.Shootable;

public class ShootCommand implements Command {
    private final Shootable shootable;

    public ShootCommand(Shootable shootable) {
        this.shootable = shootable;
    }

    @Override
    public void execute() {
        shootable.shoot();
    }
}
