package ru.mipt.bit.platformer.level;

import com.badlogic.gdx.math.GridPoint2;
import ru.mipt.bit.platformer.model.Level;
import ru.mipt.bit.platformer.model.Tank;
import ru.mipt.bit.platformer.model.Tree;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.Reader;
import java.io.UncheckedIOException;
import java.util.ArrayList;
import java.util.List;
import java.util.function.Supplier;

public class FileLevelGenerator implements LevelGenerator {
    public static final char TREE = 'T';
    public static final char PLAYER = 'X';
    public static final char EMPTY = '_';

    private final Supplier<Reader> source;
    private final float tankSpeed;

    public FileLevelGenerator(Supplier<Reader> source, float tankSpeed) {
        this.source = source;
        this.tankSpeed = tankSpeed;
    }

    @Override
    public Level generate() {
        return parse(readLines());
    }

    private List<String> readLines() {
        List<String> lines = new ArrayList<>();
        try (BufferedReader reader = new BufferedReader(source.get())) {
            String line;
            while ((line = reader.readLine()) != null) {
                line = line.strip();
                if (!line.isEmpty()) {
                    lines.add(line);
                }
            }
        } catch (IOException e) {
            throw new UncheckedIOException("Cannot read level file", e);
        }
        return lines;
    }

    private Level parse(List<String> lines) {
        if (lines.isEmpty()) {
            throw new IllegalArgumentException("Level file is empty");
        }
        int height = lines.size();
        int width = lines.get(0).length();

        Tank tank = null;
        List<Tree> trees = new ArrayList<>();
        for (int row = 0; row < height; row++) {
            String line = lines.get(row);
            if (line.length() != width) {
                throw new IllegalArgumentException(String.format(
                        "Line %d has length %d, expected %d", row + 1, line.length(), width));
            }
            int y = height - 1 - row;
            for (int x = 0; x < width; x++) {
                GridPoint2 cell = new GridPoint2(x, y);
                char symbol = line.charAt(x);
                switch (symbol) {
                    case TREE:
                        trees.add(new Tree(cell));
                        break;
                    case PLAYER:
                        if (tank != null) {
                            throw new IllegalArgumentException("Level must contain exactly one '" + PLAYER + "'");
                        }
                        tank = new Tank(cell, tankSpeed);
                        break;
                    case EMPTY:
                        break;
                    default:
                        throw new IllegalArgumentException(String.format(
                                "Unknown symbol '%c' at line %d, column %d", symbol, row + 1, x + 1));
                }
            }
        }
        if (tank == null) {
            throw new IllegalArgumentException("Level must contain player position '" + PLAYER + "'");
        }
        return new Level(width, height, tank, trees);
    }
}