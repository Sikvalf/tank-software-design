package ru.mipt.bit.platformer;

import com.badlogic.gdx.graphics.g2d.Batch;
import com.badlogic.gdx.graphics.g2d.TextureRegion;
import com.badlogic.gdx.maps.tiled.TiledMapTileLayer;
import com.badlogic.gdx.math.GridPoint2;
import com.badlogic.gdx.math.Rectangle;

import static ru.mipt.bit.platformer.util.GdxGameUtils.*;

public class Tree {
    private final TextureRegion graphics;
    private final Rectangle rectangle;
    private final GridPoint2 coordinates;

    public Tree(TextureRegion graphics, TiledMapTileLayer groundLayer, GridPoint2 coordinates) {
        this.graphics = graphics;
        this.coordinates = coordinates;
        this.rectangle = createBoundingRectangle(graphics);
        moveRectangleAtTileCenter(groundLayer, rectangle, coordinates);
    }

    public void render(Batch batch) {
        drawTextureRegionUnscaled(batch, graphics, rectangle, 0f);
    }

    public GridPoint2 getCoordinates() {
        return coordinates;
    }
}