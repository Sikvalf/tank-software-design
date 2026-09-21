package ru.mipt.bit.platformer.view;

import com.badlogic.gdx.graphics.g2d.Batch;
import com.badlogic.gdx.graphics.g2d.TextureRegion;
import com.badlogic.gdx.maps.tiled.TiledMapTileLayer;
import com.badlogic.gdx.math.GridPoint2;
import com.badlogic.gdx.math.Rectangle;
import ru.mipt.bit.platformer.model.Tree;
import static ru.mipt.bit.platformer.util.GdxGameUtils.*;

public class TreeView {
    private final TextureRegion graphics;
    private final Rectangle rectangle;
    private final TiledMapTileLayer groundLayer;
    private final GridPoint2 coordinates;

    public TreeView(TextureRegion graphics, TiledMapTileLayer groundLayer, GridPoint2 coordinates) {
        this.graphics = graphics;
        this.groundLayer = groundLayer;
        this.coordinates = coordinates;
        this.rectangle = createBoundingRectangle(graphics);
        moveRectangleAtTileCenter(groundLayer, rectangle, coordinates);
    }

    public void render(Batch batch, Tree tree) {
        drawTextureRegionUnscaled(batch, graphics, rectangle, 0f);
    }
}