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
import ru.mipt.bit.platformer.model.Level;
import ru.mipt.bit.platformer.model.Tank;
import ru.mipt.bit.platformer.model.Tree;
import ru.mipt.bit.platformer.util.TileMovement;
import ru.mipt.bit.platformer.view.TankView;
import ru.mipt.bit.platformer.view.TreeView;

import java.util.ArrayList;
import java.util.List;
import java.util.Random;

import static com.badlogic.gdx.graphics.GL20.GL_COLOR_BUFFER_BIT;
import static ru.mipt.bit.platformer.util.GdxGameUtils.*;

public class GameDesktopLauncher implements ApplicationListener {

    private static final String DEFAULT_LEVEL_FILE = "levels/level1.txt";
    private static final int LEVEL_WIDTH = 10;   // размеры совпадают с level.tmx
    private static final int LEVEL_HEIGHT = 8;
    private static final int RANDOM_TREE_COUNT = 15;
    private static final int AI_TANK_COUNT = 5;
    private static final float TANK_SPEED = 0.4f;
    private static final Color PLAYER_TINT = Color.WHITE;
    private static final Color AI_TINT = new Color(1f, 0.55f, 0.55f, 1f);

    private final LevelGenerator levelGenerator;

    private Batch batch;
    private TiledMap map;
    private MapRenderer levelRenderer;
    private TileMovement tileMovement;

    private Texture blueTankTexture;
    private Texture greenTreeTexture;

    private Game game;
    private final List<TankView> tankViews = new ArrayList<>();
    private final List<TreeView> treeViews = new ArrayList<>();

    public GameDesktopLauncher(LevelGenerator levelGenerator) {
        this.levelGenerator = levelGenerator;
    }

    @Override
    public void create() {
        batch = new SpriteBatch();

        map = new TmxMapLoader().load("level.tmx");
        levelRenderer = createSingleLayerMapRenderer(map, batch);
        TiledMapTileLayer groundLayer = getSingleLayer(map);
        tileMovement = new TileMovement(groundLayer, Interpolation.smooth);

        Level level = levelGenerator.generate();
        checkLevelFitsMap(level, groundLayer);

        InputHandler inputHandler = new InputHandler();
        Random aiRandom = new Random();
        game = GameFactory.create(level,
                new PlayerControlStrategy(inputHandler),
                () -> new RandomControlStrategy(aiRandom));

        blueTankTexture = new Texture("images/tank_blue.png");
        TextureRegion tankRegion = new TextureRegion(blueTankTexture);
        for (Tank tank : level.getAllTanks()) {
            Color tint = tank == level.getPlayerTank() ? PLAYER_TINT : AI_TINT;
            tankViews.add(new TankView(tankRegion, tileMovement, tank, tint));
        }

        greenTreeTexture = new Texture("images/greenTree.png");
        TextureRegion treeRegion = new TextureRegion(greenTreeTexture);
        for (Tree tree : level.getTrees()) {
            treeViews.add(new TreeView(treeRegion, groundLayer, tree));
        }

        Gdx.input.setInputProcessor(inputHandler);
    }

    private static void checkLevelFitsMap(Level level, TiledMapTileLayer groundLayer) {
        if (level.getWidth() > groundLayer.getWidth() || level.getHeight() > groundLayer.getHeight()) {
            throw new IllegalStateException(String.format(
                    "Level %dx%d does not fit map %dx%d",
                    level.getWidth(), level.getHeight(), groundLayer.getWidth(), groundLayer.getHeight()));
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
        for (TankView tankView : tankViews) {
            tankView.render(batch);
        }
        for (TreeView treeView : treeViews) {
            treeView.render(batch);
        }
        batch.end();
    }

    @Override
    public void resize(int width, int height) {}
    @Override
    public void pause() {}
    @Override
    public void resume() {}

    @Override
    public void dispose() {
        greenTreeTexture.dispose();
        blueTankTexture.dispose();
        map.dispose();
        batch.dispose();
    }


    public static void main(String[] args) {
        Lwjgl3ApplicationConfiguration config = new Lwjgl3ApplicationConfiguration();
        config.setWindowedMode(1280, 1024);
        new Lwjgl3Application(new GameDesktopLauncher(chooseLevelGenerator(args)), config);
    }

    private static LevelGenerator chooseLevelGenerator(String[] args) {
        return new RandomTanksLevelGenerator(chooseBaseLevelGenerator(args), AI_TANK_COUNT, TANK_SPEED, new Random());
    }

    private static LevelGenerator chooseBaseLevelGenerator(String[] args) {
        if (args.length > 0 && args[0].equals("--random")) {
            return new RandomLevelGenerator(LEVEL_WIDTH, LEVEL_HEIGHT, RANDOM_TREE_COUNT, TANK_SPEED, new Random());
        }
        String path = args.length > 1 && args[0].equals("--file") ? args[1] : DEFAULT_LEVEL_FILE;
        return new FileLevelGenerator(() -> Gdx.files.internal(path).reader("UTF-8"), TANK_SPEED);
    }
}