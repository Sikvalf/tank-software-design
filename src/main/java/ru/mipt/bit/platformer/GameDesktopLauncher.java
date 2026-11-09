package ru.mipt.bit.platformer;

import com.badlogic.gdx.ApplicationListener;
import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.backends.lwjgl3.Lwjgl3Application;
import com.badlogic.gdx.backends.lwjgl3.Lwjgl3ApplicationConfiguration;
import com.badlogic.gdx.graphics.Color;
import com.badlogic.gdx.graphics.Texture;
import com.badlogic.gdx.graphics.g2d.Batch;
import com.badlogic.gdx.graphics.g2d.SpriteBatch;
import com.badlogic.gdx.graphics.g2d.TextureRegion;
import com.badlogic.gdx.graphics.glutils.ShapeRenderer;
import com.badlogic.gdx.maps.MapRenderer;
import com.badlogic.gdx.maps.tiled.TiledMap;
import com.badlogic.gdx.maps.tiled.TiledMapTileLayer;
import com.badlogic.gdx.maps.tiled.TmxMapLoader;
import com.badlogic.gdx.math.Interpolation;
import ru.mipt.bit.platformer.control.PlayerControlStrategy;
import ru.mipt.bit.platformer.control.RandomControlStrategy;
import ru.mipt.bit.platformer.game.Game;
import ru.mipt.bit.platformer.game.GameFactory;
import ru.mipt.bit.platformer.input.InputHandler;
import ru.mipt.bit.platformer.level.FileLevelGenerator;
import ru.mipt.bit.platformer.level.LevelGenerator;
import ru.mipt.bit.platformer.level.RandomLevelGenerator;
import ru.mipt.bit.platformer.level.RandomTanksLevelGenerator;
import ru.mipt.bit.platformer.model.Bullet;
import ru.mipt.bit.platformer.model.GameObject;
import ru.mipt.bit.platformer.model.Level;
import ru.mipt.bit.platformer.model.LevelObserver;
import ru.mipt.bit.platformer.model.Tank;
import ru.mipt.bit.platformer.model.Tree;
import ru.mipt.bit.platformer.util.TileMovement;
import ru.mipt.bit.platformer.view.BulletView;
import ru.mipt.bit.platformer.view.HealthBarDecorator;
import ru.mipt.bit.platformer.view.TankView;
import ru.mipt.bit.platformer.view.TreeView;
import ru.mipt.bit.platformer.control.ToggleHealthBarsCommand;
import java.util.ArrayList;
import java.util.List;
import java.util.Random;

import static com.badlogic.gdx.graphics.GL20.GL_COLOR_BUFFER_BIT;
import static ru.mipt.bit.platformer.util.GdxGameUtils.*;

public class GameDesktopLauncher implements ApplicationListener, LevelObserver {

    private static final String DEFAULT_LEVEL_FILE = "levels/level1.txt";
    private static final int LEVEL_WIDTH = 10;
    private static final int LEVEL_HEIGHT = 8;
    private static final int RANDOM_TREE_COUNT = 15;
    private static final int AI_TANK_COUNT = 5;
    private static final float TANK_SPEED = 0.4f;
    private static final Color PLAYER_TINT = Color.WHITE;
    private static final Color AI_TINT = new Color(1f, 0.55f, 0.55f, 1f);

    private final LevelGenerator levelGenerator;

    private Batch batch;
    private ShapeRenderer shapeRenderer;
    private TiledMap map;
    private MapRenderer levelRenderer;
    private TileMovement tileMovement;
    private TiledMapTileLayer groundLayer;

    private Texture blueTankTexture;
    private Texture greenTreeTexture;
    private Texture bulletTexture;

    private TextureRegion tankRegion;
    private TextureRegion treeRegion;

    private Game game;

    private final List<TankView> tankViews = new ArrayList<>();
    private final List<HealthBarDecorator> healthBarDecorators = new ArrayList<>();
    private final List<TreeView> treeViews = new ArrayList<>();
    private final List<BulletView> bulletViews = new ArrayList<>();

    public GameDesktopLauncher(LevelGenerator levelGenerator) {
        this.levelGenerator = levelGenerator;
    }

    @Override
    public void create() {
        batch = new SpriteBatch();
        shapeRenderer = new ShapeRenderer();

        map = new TmxMapLoader().load("level.tmx");
        levelRenderer = createSingleLayerMapRenderer(map, batch);
        groundLayer = getSingleLayer(map);
        tileMovement = new TileMovement(groundLayer, Interpolation.smooth);

        Level level = levelGenerator.generate();
        checkLevelFitsMap(level, groundLayer);

        InputHandler inputHandler = new InputHandler();
        inputHandler.setToggleHealthBarsCommand(
                new ToggleHealthBarsCommand(healthBarDecorators)
        );
        Random aiRandom = new Random();

        game = GameFactory.create(
                level,
                new PlayerControlStrategy(inputHandler),
                () -> new RandomControlStrategy(aiRandom, true)
        );

        blueTankTexture = new Texture("images/tank_blue.png");
        greenTreeTexture = new Texture("images/greenTree.png");
        bulletTexture = BulletView.createTexture();

        tankRegion = new TextureRegion(blueTankTexture);
        treeRegion = new TextureRegion(greenTreeTexture);

        level.addObserver(this);
        Gdx.input.setInputProcessor(inputHandler);
    }

    private static void checkLevelFitsMap(
            Level level,
            TiledMapTileLayer groundLayer
    ) {
        if (level.getWidth() > groundLayer.getWidth()
                || level.getHeight() > groundLayer.getHeight()) {
            throw new IllegalStateException(String.format(
                    "Level %dx%d does not fit map %dx%d",
                    level.getWidth(),
                    level.getHeight(),
                    groundLayer.getWidth(),
                    groundLayer.getHeight()
            ));
        }
    }

    @Override
    public void onObjectAdded(GameObject object) {
        if (object instanceof Tank) {
            Tank tank = (Tank) object;
            Color tint = tank == game.getLevel().getPlayerTank()
                    ? PLAYER_TINT
                    : AI_TINT;

            TankView tankView = new TankView(
                    tankRegion, tileMovement, tank, tint
            );

            tankViews.add(tankView);

            healthBarDecorators.add(
                    new HealthBarDecorator(tankView, tank, shapeRenderer)
            );

        } else if (object instanceof Tree) {
            Tree tree = (Tree) object;

            treeViews.add(
                    new TreeView(treeRegion, groundLayer, tree)
            );

        } else if (object instanceof Bullet) {
            Bullet bullet = (Bullet) object;

            bulletViews.add(
                    new BulletView(bullet, groundLayer, bulletTexture)
            );
        }
    }

    @Override
    public void onObjectRemoved(GameObject object) {
        if (object instanceof Tank) {
            tankViews.removeIf(view -> view.getTank() == object);

            healthBarDecorators.removeIf(
                    decorator -> decorator.getTank() == object
            );

        } else if (object instanceof Bullet) {
            bulletViews.removeIf(view -> view.getBullet() == object);
        }
    }

    @Override
    public void render() {
        Gdx.gl.glClearColor(0f, 0f, 0.2f, 1f);
        Gdx.gl.glClear(GL_COLOR_BUFFER_BIT);

        float deltaTime = Gdx.graphics.getDeltaTime();
        game.update(deltaTime);

        levelRenderer.render();

        batch.begin();

        for (HealthBarDecorator decorator : healthBarDecorators) {
            decorator.render(batch);
        }

        for (TreeView treeView : treeViews) {
            treeView.render(batch);
        }

        for (BulletView bulletView : bulletViews) {
            bulletView.render(batch);
        }

        batch.end();
    }

    @Override
    public void resize(int width, int height) {
    }

    @Override
    public void pause() {
    }

    @Override
    public void resume() {
    }

    @Override
    public void dispose() {
        game.getLevel().removeObserver(this);
        bulletTexture.dispose();
        greenTreeTexture.dispose();
        blueTankTexture.dispose();
        map.dispose();
        batch.dispose();
        shapeRenderer.dispose();
    }

    public static void main(String[] args) {
        Lwjgl3ApplicationConfiguration config =
                new Lwjgl3ApplicationConfiguration();

        config.setWindowedMode(1280, 1024);

        new Lwjgl3Application(
                new GameDesktopLauncher(chooseLevelGenerator(args)),
                config
        );
    }

    private static LevelGenerator chooseLevelGenerator(String[] args) {
        return new RandomTanksLevelGenerator(
                chooseBaseLevelGenerator(args),
                AI_TANK_COUNT,
                TANK_SPEED,
                new Random()
        );
    }

    private static LevelGenerator chooseBaseLevelGenerator(String[] args) {
        if (args.length > 0 && args[0].equals("--random")) {
            return new RandomLevelGenerator(
                    LEVEL_WIDTH,
                    LEVEL_HEIGHT,
                    RANDOM_TREE_COUNT,
                    TANK_SPEED,
                    new Random()
            );
        }

        String path = args.length > 1 && args[0].equals("--file")
                ? args[1]
                : DEFAULT_LEVEL_FILE;

        return new FileLevelGenerator(
                () -> Gdx.files.internal(path).reader("UTF-8"),
                TANK_SPEED
        );
    }
}