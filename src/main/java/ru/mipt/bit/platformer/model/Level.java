package ru.mipt.bit.platformer.model;

import com.badlogic.gdx.math.GridPoint2;

import java.util.ArrayList;
import java.util.Collections;
import java.util.HashSet;
import java.util.List;
import java.util.Set;

public class Level implements ObstacleMap, BulletSpawner {
    private final int width;
    private final int height;
    private final Tank playerTank;
    private final List<Tank> aiTanks;
    private final List<Tank> allTanks;
    private final List<Tree> trees;
    private final Set<GridPoint2> treeCells = new HashSet<>();
    private final List<LevelObserver> observers = new ArrayList<>();
    private final List<Bullet> bullets = new ArrayList<>();

    public Level(int width, int height, Tank playerTank, List<Tree> trees) {
        this(width, height, playerTank, List.of(), trees);
    }

    public Level(int width, int height, Tank playerTank, List<Tank> aiTanks, List<Tree> trees) {
        if (width <= 0 || height <= 0) {
            throw new IllegalArgumentException("Level size must be positive: " + width + "x" + height);
        }

        this.width = width;
        this.height = height;
        this.playerTank = playerTank;
        this.aiTanks = new ArrayList<>(aiTanks);
        this.trees = Collections.unmodifiableList(new ArrayList<>(trees));

        for (Tree tree : trees) {
            treeCells.add(new GridPoint2(tree.getCoordinates()));
        }

        this.allTanks = new ArrayList<>();
        allTanks.add(playerTank);
        allTanks.addAll(aiTanks);

        checkObjectsArePlacedCorrectly();

        for (Tank tank : allTanks) {
            tank.setBulletSpawner(this);
        }
    }

    private void checkObjectsArePlacedCorrectly() {
        Set<GridPoint2> used = new HashSet<>(treeCells);

        for (Tank tank : allTanks) {
            GridPoint2 cell = tank.getCoordinates();

            if (!isInside(cell)) {
                throw new IllegalArgumentException("Tank is outside the level: " + cell);
            }

            if (!used.add(new GridPoint2(cell))) {
                throw new IllegalArgumentException("Cell is already occupied: " + cell);
            }
        }
    }

    @Override
    public boolean isFree(GridPoint2 cell) {
        return isInside(cell) && !treeCells.contains(cell) && !isOccupiedByTank(cell);
    }

    private boolean isOccupiedByTank(GridPoint2 cell) {
        for (Tank tank : allTanks) {
            if (tank.occupies(cell)) {
                return true;
            }
        }

        return false;
    }

    public boolean isInside(GridPoint2 cell) {
        return cell.x >= 0 && cell.x < width && cell.y >= 0 && cell.y < height;
    }

    public void update(float deltaTime) {
        for (Tank tank : allTanks) {
            tank.update(deltaTime);
        }

        List<Bullet> currentBullets = new ArrayList<>(bullets);

        for (Bullet bullet : currentBullets) {
            if (!bullets.contains(bullet)) {
                continue;
            }

            bullet.move();
            resolveBulletCollision(bullet);
        }
    }

    private void resolveBulletCollision(Bullet bullet) {
        GridPoint2 cell = bullet.getCoordinates();

        if (!isInside(cell) || treeCells.contains(cell)) {
            removeBullet(bullet);
            return;
        }

        for (Tank tank : new ArrayList<>(allTanks)) {
            if (tank.occupies(cell)) {
                removeBullet(bullet);
                tank.takeDamage();

                if (tank.isDestroyed()) {
                    removeTank(tank);
                }

                return;
            }
        }

        for (Bullet other : new ArrayList<>(bullets)) {
            if (other != bullet && other.getCoordinates().equals(cell)) {
                removeBullet(bullet);
                removeBullet(other);
                return;
            }
        }
    }

    private void removeBullet(Bullet bullet) {
        if (bullets.remove(bullet)) {
            notifyObjectRemoved(bullet);
        }
    }

    private void removeTank(Tank tank) {
        if (allTanks.remove(tank)) {
            aiTanks.remove(tank);
            notifyObjectRemoved(tank);
        }
    }

    public void addObserver(LevelObserver observer) {
        if (observer == null) {
            throw new IllegalArgumentException("Observer cannot be null");
        }

        if (observers.contains(observer)) {
            return;
        }

        observers.add(observer);

        for (Tree tree : trees) {
            observer.onObjectAdded(tree);
        }

        for (Tank tank : allTanks) {
            observer.onObjectAdded(tank);
        }

        for (Bullet bullet : bullets) {
            observer.onObjectAdded(bullet);
        }
    }

    public void removeObserver(LevelObserver observer) {
        observers.remove(observer);
    }

    private void notifyObjectAdded(GameObject object) {
        for (LevelObserver observer : List.copyOf(observers)) {
            observer.onObjectAdded(object);
        }
    }

    private void notifyObjectRemoved(GameObject object) {
        for (LevelObserver observer : List.copyOf(observers)) {
            observer.onObjectRemoved(object);
        }
    }

    @Override
    public void spawn(Bullet bullet) {
        if (bullet == null) {
            throw new IllegalArgumentException("Bullet cannot be null");
        }

        if (!isInside(bullet.getCoordinates())) {
            return;
        }

        bullets.add(bullet);
        notifyObjectAdded(bullet);
        resolveBulletCollision(bullet);
    }

    public List<Bullet> getBullets() {
        return Collections.unmodifiableList(bullets);
    }

    public int getWidth() {
        return width;
    }

    public int getHeight() {
        return height;
    }

    public Tank getPlayerTank() {
        return playerTank;
    }

    public List<Tank> getAiTanks() {
        return Collections.unmodifiableList(aiTanks);
    }

    public List<Tank> getAllTanks() {
        return Collections.unmodifiableList(allTanks);
    }

    public List<Tree> getTrees() {
        return trees;
    }
}
