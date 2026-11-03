package ru.mipt.bit.platformer.model;

public interface LevelObserver {
    void onObjectAdded(GameObject object);

    void onObjectRemoved(GameObject object);
}
