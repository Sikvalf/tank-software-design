package ru.mipt.bit.platformer.model;

import com.badlogic.gdx.math.GridPoint2;
import org.junit.jupiter.api.Test;

import java.util.List;

import static org.junit.jupiter.api.Assertions.*;

class BulletTest {
    @Test
    void movementDependsOnElapsedTimeNotUpdateCount() {
        Tank firstPlayer = new Tank(new GridPoint2(0, 0), 0.4f);
        Tank secondPlayer = new Tank(new GridPoint2(0, 0), 0.4f);
        Level first = new Level(20, 3, firstPlayer, List.of());
        Level second = new Level(20, 3, secondPlayer, List.of());
        Bullet firstBullet = new Bullet(new GridPoint2(1, 1), Direction.RIGHT, firstPlayer, 1, 0.125f);
        Bullet secondBullet = new Bullet(new GridPoint2(1, 1), Direction.RIGHT, secondPlayer, 1, 0.125f);
        first.spawn(firstBullet);
        second.spawn(secondBullet);

        first.update(1f);
        for (int i = 0; i < 32; i++) {
            second.update(0.03125f);
        }

        assertEquals(new GridPoint2(9, 1), firstBullet.getCoordinates());
        assertEquals(firstBullet.getCoordinates(), secondBullet.getCoordinates());
    }

    @Test
    void bulletDoesNotHitMovingOwnerAndUsesConfiguredDamage() {
        Tank owner = new Tank(new GridPoint2(0, 0), 1f);
        Tank target = new Tank(new GridPoint2(2, 0), 1f);
        Level level = new Level(5, 2, owner, List.of(target), List.of());
        owner.configureWeapon(17, 0.125f);
        owner.tryMove(Direction.RIGHT, level);
        owner.shoot();

        assertEquals(owner.getMaxHealth(), owner.getHealth());
        assertEquals(1, level.getBullets().size());
        Bullet bullet = level.getBullets().get(0);
        assertSame(owner, bullet.getOwner());
        assertEquals(17, bullet.getDamage());
        level.update(0.125f);

        assertEquals(owner.getMaxHealth(), owner.getHealth());
        assertEquals(target.getMaxHealth() - 17, target.getHealth());
        assertTrue(level.getBullets().isEmpty());
    }

    @Test
    void bulletStopsAtTreeAndAtLevelBoundary() {
        Tank owner = new Tank(new GridPoint2(0, 0), 1f);
        Tree tree = new Tree(new GridPoint2(3, 0));
        Level level = new Level(5, 2, owner, List.of(tree));
        Bullet blocked = new Bullet(new GridPoint2(1, 0), Direction.RIGHT, owner, 1, 0.125f);
        level.spawn(blocked);
        level.update(1f);
        assertTrue(level.getBullets().isEmpty());
        assertEquals(new GridPoint2(3, 0), blocked.getCoordinates());

        Bullet escaping = new Bullet(new GridPoint2(4, 1), Direction.RIGHT, owner, 1, 0.125f);
        level.spawn(escaping);
        level.update(0.125f);
        assertTrue(level.getBullets().isEmpty());
        assertEquals(new GridPoint2(5, 1), escaping.getCoordinates());
    }

    @Test
    void bulletsCollidingOnSpawnAreBothRemoved() {
        Tank owner = new Tank(new GridPoint2(0, 0), 1f);
        Level level = new Level(5, 2, owner, List.of());
        level.spawn(new Bullet(new GridPoint2(2, 1), Direction.RIGHT));
        level.spawn(new Bullet(new GridPoint2(2, 1), Direction.LEFT));
        assertTrue(level.getBullets().isEmpty());
    }

    @Test
    void bulletRejectsInvalidParametersAndTime() {
        GridPoint2 position = new GridPoint2(1, 1);
        assertThrows(IllegalArgumentException.class, () -> new Bullet(position, Direction.RIGHT, null, 0, 1f));
        assertThrows(IllegalArgumentException.class, () -> new Bullet(position, Direction.RIGHT, null, 1, Float.NaN));
        Bullet bullet = new Bullet(position, Direction.RIGHT);
        assertThrows(IllegalArgumentException.class, () -> bullet.advance(-1f));
        assertThrows(IllegalArgumentException.class, () -> bullet.advance(Float.POSITIVE_INFINITY));
    }

    @Test
    void largeUpdateChecksCollisionsAtEveryCell() {
        Tank owner = new Tank(new GridPoint2(0, 0), 1f);
        Tank target = new Tank(new GridPoint2(3, 0), 1f);
        Level level = new Level(10, 2, owner, List.of(target), List.of());
        owner.configureWeapon(7, 0.125f);
        owner.shoot();

        level.update(1f);

        assertEquals(target.getMaxHealth() - 7, target.getHealth());
        assertTrue(level.getBullets().isEmpty());
    }
}
