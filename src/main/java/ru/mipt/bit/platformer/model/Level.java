package ru.mipt.bit.platformer.model;

import com.badlogic.gdx.math.GridPoint2;

import java.util.ArrayList;
import java.util.Collections;
import java.util.HashSet;
import java.util.List;
import java.util.Set;

public class Level implements ObstacleMap {
    private final int width;
    private final int height;
    private final Tank playerTank;
    private final List<Tank> aiTanks;
    private final List<Tank> allTanks;
    private final List<Tree> trees;
    private final Set<GridPoint2> treeCells = new HashSet<>();

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
        this.aiTanks = Collections.unmodifiableList(new ArrayList<>(aiTanks));
        this.trees = Collections.unmodifiableList(new ArrayList<>(trees));
        for (Tree tree : trees) {
            treeCells.add(new GridPoint2(tree.getCoordinates()));
        }

        List<Tank> tanks = new ArrayList<>();
        tanks.add(playerTank);
        tanks.addAll(aiTanks);
        this.allTanks = Collections.unmodifiableList(tanks);
        checkObjectsArePlacedCorrectly();
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

    /**
     * Клетка свободна, если она внутри уровня, на ней нет дерева
     * и её не занимает ни один танк (ни стоящий, ни едущий в неё или из неё).
     */
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
    }

    public int getWidth() { return width; }
    public int getHeight() { return height; }
    public Tank getPlayerTank() { return playerTank; }
    public List<Tank> getAiTanks() { return aiTanks; }
    public List<Tank> getAllTanks() { return allTanks; }
    public List<Tree> getTrees() { return trees; }
}
