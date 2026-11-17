package ru.mipt.bit.platformer.model;

import com.badlogic.gdx.math.GridPoint2;
import org.junit.jupiter.api.Test;
import ru.mipt.bit.platformer.model.state.HeavyTankState;
import ru.mipt.bit.platformer.model.state.LightTankState;
import ru.mipt.bit.platformer.model.state.MediumTankState;

import java.util.List;
import java.util.concurrent.atomic.AtomicInteger;

import static org.junit.jupiter.api.Assertions.*;

class TankStateTest {
    @Test
    void transitionsAtExactThresholds() {
        Tank tank = new Tank(new GridPoint2(1, 1), 0.4f);
        assertInstanceOf(LightTankState.class, tank.getState());
        for (int i = 0; i < 29; i++) {
            tank.takeDamage();
        }
        assertInstanceOf(LightTankState.class, tank.getState());
        tank.takeDamage();
        assertInstanceOf(MediumTankState.class, tank.getState());
        for (int i = 0; i < 55; i++) {
            tank.takeDamage();
        }
        assertInstanceOf(MediumTankState.class, tank.getState());
        tank.takeDamage();
        assertInstanceOf(HeavyTankState.class, tank.getState());
    }

    @Test
    void movementSlowsAsHealthDrops() {
        Tank tank = new Tank(new GridPoint2(1, 1), 0.4f);
        Level level = new Level(10, 10, tank, List.of());
        for (int i = 0; i < 30; i++) {
            tank.takeDamage();
        }
        tank.tryMove(Direction.UP, level);
        tank.update(0.4f);
        assertEquals(0.5f, tank.getMovementProgress(), 0.0001f);
        tank.update(0.4f);
        assertFalse(tank.isMoving());
        for (int i = 0; i < 56; i++) {
            tank.takeDamage();
        }
        tank.tryMove(Direction.UP, level);
        tank.update(0.4f);
        assertEquals(1f / 3f, tank.getMovementProgress(), 0.0001f);
    }

    @Test
    void heavyTankCannotShoot() {
        Tank tank = new Tank(new GridPoint2(1, 1), 0.4f);
        AtomicInteger shots = new AtomicInteger();
        tank.setBulletSpawner(bullet -> shots.incrementAndGet());
        tank.shoot();
        assertEquals(1, shots.get());
        for (int i = 0; i < 86; i++) {
            tank.takeDamage();
        }
        tank.shoot();
        assertEquals(1, shots.get());
    }

    @Test
    void coordinatesCannotBeChangedExternally() {
        Tank tank = new Tank(new GridPoint2(1, 1), 0.4f);
        tank.getCoordinates().set(8, 8);
        tank.getDestinationCoordinates().set(9, 9);
        assertEquals(new GridPoint2(1, 1), tank.getCoordinates());
        assertEquals(new GridPoint2(1, 1), tank.getDestinationCoordinates());
        Tree tree = new Tree(new GridPoint2(2, 2));
        tree.getCoordinates().set(3, 3);
        assertEquals(new GridPoint2(2, 2), tree.getCoordinates());
    }
}
