package ru.mipt.bit.platformer.model;

import com.badlogic.gdx.math.GridPoint2;

public interface ObstacleMap {
    boolean isFree(GridPoint2 cell);
}