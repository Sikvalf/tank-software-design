package ru.mipt.bit.platformer.model.state;

public final class HealthBasedTankStateResolver {
    private final TankState light = new LightTankState();
    private final TankState medium = new MediumTankState();
    private final TankState heavy = new HeavyTankState();

    public TankState resolve(int health, int maxHealth) {
        if (maxHealth <= 0 || health < 0 || health > maxHealth) {
            throw new IllegalArgumentException("Invalid health");
        }
        if (health * 100L > maxHealth * 70L) {
            return light;
        }
        if (health * 100L >= maxHealth * 15L) {
            return medium;
        }
        return heavy;
    }
}
