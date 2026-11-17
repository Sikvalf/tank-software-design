package ru.mipt.bit.platformer.model.state;

public final class MediumTankState implements TankState {
    @Override
    public float movementDurationMultiplier() {
        return 2f;
    }

    @Override
    public boolean canShoot() {
        return true;
    }
}
