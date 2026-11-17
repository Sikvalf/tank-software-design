package ru.mipt.bit.platformer.model.state;

public final class HeavyTankState implements TankState {
    @Override
    public float movementDurationMultiplier() {
        return 3f;
    }

    @Override
    public boolean canShoot() {
        return false;
    }
}
