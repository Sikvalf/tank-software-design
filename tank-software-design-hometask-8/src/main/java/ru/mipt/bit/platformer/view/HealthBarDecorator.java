package ru.mipt.bit.platformer.view;

import com.badlogic.gdx.graphics.Color;
import com.badlogic.gdx.graphics.g2d.Batch;
import com.badlogic.gdx.graphics.glutils.ShapeRenderer;
import com.badlogic.gdx.math.Rectangle;
import ru.mipt.bit.platformer.model.Tank;

public class HealthBarDecorator implements BoundedRenderable {

    private final BoundedRenderable wrapped;
    private final Tank tank;
    private final HealthBarPainter painter;
    private final HealthBarVisibilityState visibility;

    public HealthBarDecorator(
            BoundedRenderable wrapped,
            Tank tank,
            ShapeRenderer shapeRenderer,
            HealthBarVisibilityState visibility
    ) {
        this(wrapped, tank, (batch, rectangle, healthPercent) ->
                drawHealthBar(shapeRenderer, batch, rectangle, healthPercent), visibility);
    }

    HealthBarDecorator(
            BoundedRenderable wrapped,
            Tank tank,
            HealthBarPainter painter,
            HealthBarVisibilityState visibility
    ) {
        this.wrapped = wrapped;
        this.tank = tank;
        this.painter = painter;
        this.visibility = visibility;
    }

    @FunctionalInterface
    interface HealthBarPainter {
        void render(Batch batch, Rectangle rectangle, float healthPercent);
    }

    @Override
    public Rectangle getRectangle() {
        return wrapped.getRectangle();
    }

    public Tank getTank() {
        return tank;
    }

    @Override
    public void render(Batch batch) {
        wrapped.render(batch);

        if (!visibility.isVisible()) {
            return;
        }

        float healthPercent = (float) tank.getHealth() / tank.getMaxHealth();
        painter.render(batch, wrapped.getRectangle(), healthPercent);
    }

    private static void drawHealthBar(
            ShapeRenderer shapeRenderer, Batch batch, Rectangle rectangle, float healthPercent
    ) {
        float width = rectangle.width;
        float height = 5f;
        float x = rectangle.x;
        float y = rectangle.y + rectangle.height + 5f;

        batch.end();

        shapeRenderer.setProjectionMatrix(batch.getProjectionMatrix());
        shapeRenderer.begin(ShapeRenderer.ShapeType.Filled);

        shapeRenderer.setColor(Color.RED);
        shapeRenderer.rect(x, y, width, height);

        shapeRenderer.setColor(Color.GREEN);
        shapeRenderer.rect(x, y, width * healthPercent, height);

        shapeRenderer.end();

        batch.begin();
    }
}