package ru.mipt.bit.platformer.view;

import com.badlogic.gdx.graphics.Color;
import com.badlogic.gdx.graphics.g2d.Batch;
import com.badlogic.gdx.graphics.g2d.TextureRegion;
import com.badlogic.gdx.math.Rectangle;
import ru.mipt.bit.platformer.model.Movable;
import ru.mipt.bit.platformer.util.TileMovement;
import static ru.mipt.bit.platformer.util.GdxGameUtils.*;

public class TankView {
    private final TextureRegion graphics;
    private final Rectangle rectangle;
    private final TileMovement tileMovement;
    private final Movable tank;
    private final Color tint;

    public TankView(TextureRegion graphics, TileMovement tileMovement, Movable tank, Color tint) {
        this.graphics = graphics;
        this.tileMovement = tileMovement;
        this.tank = tank;
        this.tint = tint;
        this.rectangle = createBoundingRectangle(graphics);
    }

    public void render(Batch batch) {
        tileMovement.moveRectangleBetweenTileCenters(
                rectangle,
                tank.getCoordinates(),
                tank.getDestinationCoordinates(),
                tank.getMovementProgress()
        );
        Color previous = batch.getColor().cpy();
        batch.setColor(tint);
        drawTextureRegionUnscaled(batch, graphics, rectangle, tank.getRotation());
        batch.setColor(previous);
    }
}
