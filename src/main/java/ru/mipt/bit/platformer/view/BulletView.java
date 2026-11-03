package ru.mipt.bit.platformer.view;

import com.badlogic.gdx.graphics.Color;
import com.badlogic.gdx.graphics.Pixmap;
import com.badlogic.gdx.graphics.Texture;
import com.badlogic.gdx.graphics.g2d.Batch;
import com.badlogic.gdx.maps.tiled.TiledMapTileLayer;
import ru.mipt.bit.platformer.model.Bullet;

public class BulletView {
    private final Bullet bullet;
    private final TiledMapTileLayer layer;
    private final Texture texture;

    public BulletView(Bullet bullet, TiledMapTileLayer layer, Texture texture) {
        this.bullet = bullet;
        this.layer = layer;
        this.texture = texture;
    }

    public void render(Batch batch) {
        float tileWidth = layer.getTileWidth();
        float tileHeight = layer.getTileHeight();
        float size = Math.min(tileWidth, tileHeight) * 0.25f;

        float x = bullet.getCoordinates().x * tileWidth + (tileWidth - size) / 2f;
        float y = bullet.getCoordinates().y * tileHeight + (tileHeight - size) / 2f;

        batch.draw(texture, x, y, size, size);
    }

    public static Texture createTexture() {
        Pixmap pixmap = new Pixmap(1, 1, Pixmap.Format.RGBA8888);
        pixmap.setColor(Color.YELLOW);
        pixmap.fill();

        Texture texture = new Texture(pixmap);
        pixmap.dispose();

        return texture;
    }
    public Bullet getBullet() {
        return bullet;
    }
}
