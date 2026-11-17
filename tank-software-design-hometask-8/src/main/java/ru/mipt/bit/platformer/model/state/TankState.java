package ru.mipt.bit.platformer.model.state;

public interface TankState {
    float movementDurationMultiplier();
    boolean canShoot();
}
