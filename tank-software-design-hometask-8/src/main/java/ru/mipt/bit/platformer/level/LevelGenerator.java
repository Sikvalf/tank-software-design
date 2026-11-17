package ru.mipt.bit.platformer.level;

import ru.mipt.bit.platformer.model.Level;

public interface LevelGenerator {
    Level generate();
}