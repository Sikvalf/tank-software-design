package ru.mipt.bit.platformer.view;

import com.badlogic.gdx.graphics.Color;
import com.badlogic.gdx.graphics.g2d.Batch;
import com.badlogic.gdx.graphics.glutils.ShapeRenderer;
import com.badlogic.gdx.math.Rectangle;
import ru.mipt.bit.platformer.model.Tank;

public class HealthBarDecorator implements Renderable {

    private final TankView wrapped;
    private final Tank tank;
    private final ShapeRenderer shapeRenderer;
    private boolean visible = false;

    public HealthBarDecorator(
            TankView wrapped,
            Tank tank,
            ShapeRenderer shapeRenderer
    ) {
        this.wrapped = wrapped;
        this.tank = tank;
        this.shapeRenderer = shapeRenderer;
    }

    public void toggle() {
        visible = !visible;
    }

    public Tank getTank() {
        return tank;
    }

    @Override
    public void render(Batch batch) {
        wrapped.render(batch);

        if (!visible) {
            return;
        }

        Rectangle rectangle = wrapped.getRectangle();

        float width = rectangle.width;
        float height = 5f;
        float x = rectangle.x;
        float y = rectangle.y + rectangle.height + 5f;

        float healthPercent =
                (float) tank.getHealth() / tank.getMaxHealth();

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