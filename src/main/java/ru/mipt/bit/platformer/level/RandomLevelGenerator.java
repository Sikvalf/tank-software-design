package ru.mipt.bit.platformer.level;

import com.badlogic.gdx.math.GridPoint2;
import ru.mipt.bit.platformer.model.Level;
import ru.mipt.bit.platformer.model.Tank;
import ru.mipt.bit.platformer.model.Tree;

import java.util.ArrayList;
import java.util.Collections;
import java.util.List;
import java.util.Random;

public class RandomLevelGenerator implements LevelGenerator {
    private final int width;
    private final int height;
    private final int treeCount;
    private final float tankSpeed;
    private final Random random;

    public RandomLevelGenerator(int width, int height, int treeCount, float tankSpeed, Random random) {
        if (width <= 0 || height <= 0) {
            throw new IllegalArgumentException("Level size must be positive: " + width + "x" + height);
        }
        if (treeCount < 0 || treeCount > width * height - 1) {
            throw new IllegalArgumentException(
                    "Tree count must be between 0 and " + (width * height - 1) + ", got " + treeCount);
        }
        this.width = width;
        this.height = height;
        this.treeCount = treeCount;
        this.tankSpeed = tankSpeed;
        this.random = random;
    }

    @Override
    public Level generate() {
        List<GridPoint2> cells = new ArrayList<>(width * height);
        for (int x = 0; x < width; x++) {
            for (int y = 0; y < height; y++) {
                cells.add(new GridPoint2(x, y));
            }
        }
        Collections.shuffle(cells, random);

        Tank tank = new Tank(cells.get(0), tankSpeed);
        List<Tree> trees = new ArrayList<>(treeCount);
        for (GridPoint2 cell : cells.subList(1, 1 + treeCount)) {
            trees.add(new Tree(cell));
        }
        return new Level(width, height, tank, trees);
    }
}