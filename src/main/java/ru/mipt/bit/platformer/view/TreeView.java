package ru.mipt.bit.platformer.view;

import com.badlogic.gdx.graphics.g2d.Batch;
import com.badlogic.gdx.graphics.g2d.TextureRegion;
import com.badlogic.gdx.maps.tiled.TiledMapTileLayer;
import com.badlogic.gdx.math.Rectangle;
import ru.mipt.bit.platformer.model.Tree;
import static ru.mipt.bit.platformer.util.GdxGameUtils.*;

public class TreeView {
    private final TextureRegion graphics;
    private final Rectangle rectangle;

    public TreeView(TextureRegion graphics, TiledMapTileLayer groundLayer, Tree tree) {
        this.graphics = graphics;
        this.rectangle = createBoundingRectangle(graphics);
        moveRectangleAtTileCenter(groundLayer, rectangle, tree.getCoordinates());
    }

    public void render(Batch batch) {
        drawTextureRegionUnscaled(batch, graphics, rectangle, 0f);
    }
}