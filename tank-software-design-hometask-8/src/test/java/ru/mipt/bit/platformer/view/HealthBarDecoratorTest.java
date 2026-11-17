package ru.mipt.bit.platformer.view;

import com.badlogic.gdx.graphics.g2d.Batch;
import com.badlogic.gdx.math.GridPoint2;
import com.badlogic.gdx.math.Rectangle;
import org.junit.jupiter.api.Test;
import ru.mipt.bit.platformer.control.ToggleHealthBarsCommand;
import ru.mipt.bit.platformer.model.Tank;

import static org.junit.jupiter.api.Assertions.*;

class HealthBarDecoratorTest {
    @Test
    void rendersWrappedObjectAndOnlyPaintsBarWhenVisible() {
        Rectangle bounds = new Rectangle(2, 3, 4, 5);
        int[] renders = {0};
        BoundedRenderable wrapped = new BoundedRenderable() {
            @Override
            public Rectangle getRectangle() { return bounds; }

            @Override
            public void render(Batch batch) { renders[0]++; }
        };
        Tank tank = new Tank(new GridPoint2(0, 0), 1f);
        HealthBarVisibilityState visibility = new HealthBarVisibilityState();
        int[] paints = {0};
        float[] percentage = {-1f};
        HealthBarDecorator decorator = new HealthBarDecorator(wrapped, tank, (batch, rectangle, health) -> {
            assertSame(bounds, rectangle);
            paints[0]++;
            percentage[0] = health;
        }, visibility);

        assertSame(bounds, decorator.getRectangle());
        assertSame(tank, decorator.getTank());
        decorator.render(null);
        assertEquals(1, renders[0]);
        assertEquals(0, paints[0]);

        ToggleHealthBarsCommand toggle = new ToggleHealthBarsCommand(visibility);
        toggle.execute();
        tank.takeDamage(35);
        decorator.render(null);
        assertEquals(2, renders[0]);
        assertEquals(1, paints[0]);
        assertEquals(0.65f, percentage[0], 0.0001f);

        toggle.execute();
        decorator.render(null);
        assertEquals(3, renders[0]);
        assertEquals(1, paints[0]);
    }
}
