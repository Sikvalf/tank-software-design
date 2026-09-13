package ru.mipt.bit.platformer;

import com.badlogic.gdx.graphics.g2d.Batch;
import com.badlogic.gdx.graphics.g2d.TextureRegion;
import com.badlogic.gdx.math.GridPoint2;
import com.badlogic.gdx.math.Interpolation;
import com.badlogic.gdx.math.Rectangle;
import ru.mipt.bit.platformer.util.TileMovement;

import static com.badlogic.gdx.math.MathUtils.isEqual;
import static ru.mipt.bit.platformer.util.GdxGameUtils.*;

public class Tank {
    private final TextureRegion graphics;
    private final Rectangle rectangle;
    private final TileMovement tileMovement;

    private GridPoint2 coordinates;
    private GridPoint2 destinationCoordinates;
    private float movementProgress = 1f;
    private float rotation = 0f;

    private final float speed = 0.4f;

    public Tank(TextureRegion graphics, TileMovement tileMovement, GridPoint2 startPosition) {
        this.graphics = graphics;
        this.tileMovement = tileMovement;
        this.rectangle = createBoundingRectangle(graphics);
        this.coordinates = new GridPoint2(startPosition);
        this.destinationCoordinates = new GridPoint2(startPosition);
    }

    public void tryMove(Direction direction, GridPoint2 obstacleCoordinates) {
        if (!isEqual(movementProgress, 1f)) return;

        GridPoint2 nextCoord = direction.getNextCoordinate(coordinates);

        if (!nextCoord.equals(obstacleCoordinates)) {
            destinationCoordinates.set(nextCoord);
            movementProgress = 0f;
        }
        rotation = direction.getRotation();
    }

    public void update(float deltaTime) {
        tileMovement.moveRectangleBetweenTileCenters(rectangle, coordinates, destinationCoordinates, movementProgress);
        movementProgress = continueProgress(movementProgress, deltaTime, speed);
        if (isEqual(movementProgress, 1f)) {
            coordinates.set(destinationCoordinates);
        }
    }

    public void render(Batch batch) {
        drawTextureRegionUnscaled(batch, graphics, rectangle, rotation);
    }

    public GridPoint2 getCoordinates() {
        return coordinates;
    }
}