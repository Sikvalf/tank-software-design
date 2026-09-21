package ru.mipt.bit.platformer.view;

import com.badlogic.gdx.graphics.g2d.Batch;
import com.badlogic.gdx.graphics.g2d.TextureRegion;
import com.badlogic.gdx.math.Rectangle;
import ru.mipt.bit.platformer.model.Tank;
import ru.mipt.bit.platformer.util.TileMovement;
import static ru.mipt.bit.platformer.util.GdxGameUtils.*;

public class TankView {
    private final TextureRegion graphics;
    private final Rectangle rectangle;
    private final TileMovement tileMovement;

    public TankView(TextureRegion graphics, TileMovement tileMovement) {
        this.graphics = graphics;
        this.tileMovement = tileMovement;
        this.rectangle = createBoundingRectangle(graphics);
    }

    public void render(Batch batch, Tank tank) {
        tileMovement.moveRectangleBetweenTileCenters(
            rectangle,
            tank.getCoordinates(),
            tank.getDestinationCoordinates(),
            tank.getMovementProgress()
        );
        drawTextureRegionUnscaled(batch, graphics, rectangle, tank.getRotation());
    }
}