package ru.mipt.bit.platformer.model.state;

public final class LightTankState implements TankState {
    @Override
    public float movementDurationMultiplier() {
        return 1f;
    }

    @Override
    public boolean canShoot() {
        return true;
    }
}
