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
    private final List<Tree> trees;
    private final Set<GridPoint2> obstacleCells = new HashSet<>();

    public Level(int width, int height, Tank playerTank, List<Tree> trees) {
        if (width <= 0 || height <= 0) {
            throw new IllegalArgumentException("Level size must be positive: " + width + "x" + height);
        }
        this.width = width;
        this.height = height;
        this.playerTank = playerTank;
        this.trees = Collections.unmodifiableList(new ArrayList<>(trees));
        for (Tree tree : trees) {
            obstacleCells.add(new GridPoint2(tree.getCoordinates()));
        }
    }

    @Override
    public boolean isFree(GridPoint2 cell) {
        return isInside(cell) && !obstacleCells.contains(cell);
    }

    public boolean isInside(GridPoint2 cell) {
        return cell.x >= 0 && cell.x < width && cell.y >= 0 && cell.y < height;
    }

    public int getWidth() { return width; }
    public int getHeight() { return height; }
    public Tank getPlayerTank() { return playerTank; }
    public List<Tree> getTrees() { return trees; }
}