package pacman.world;

import cinnamon.model.GeometryHelper;
import cinnamon.registry.SkyBoxRegistry;
import cinnamon.render.Camera;
import cinnamon.render.MatrixStack;
import cinnamon.render.WorldRenderer;
import cinnamon.world.entity.Entity;
import cinnamon.world.sky.CubemapSky;
import cinnamon.world.terrain.PrimitiveTerrain;
import cinnamon.world.terrain.Terrain;
import cinnamon.world.world.WorldClient;
import pacman.gui.PacDeathScreen;
import pacman.gui.PacHud;
import pacman.gui.PacPauseMenu;

public class PacmanWorld extends WorldClient {

    private int score = 0;

    private int coins = 0;
    private int collectedCoins = 0;

    private static final char[][] map = {
            {'#', '#', '#', '#', '#', '#', '#', '#', '#', '#', '#', '#', '#', '#', '#', '#', '#', '#', '#'},
            {'#', '*', '*', '*', '*', '*', '*', '*', '*', '#', '*', '*', '*', '*', '*', '*', '*', '*', '#'},
            {'#', '*', '#', '#', '*', '#', '#', '#', '*', '#', '*', '#', '#', '#', '*', '#', '#', '*', '#'},
            {'#', '*', '#', '#', '*', '#', '#', '#', '*', '#', '*', '#', '#', '#', '*', '#', '#', '*', '#'},
            {'#', '*', '*', '*', '*', '*', '*', '*', '*', '*', '*', '*', '*', '*', '*', '*', '*', '*', '#'},
            {'#', '*', '#', '#', '*', '#', '*', '#', '#', '#', '#', '#', '*', '#', '*', '#', '#', '*', '#'},
            {'#', '*', '*', '*', '*', '#', '*', '*', '*', '#', '*', '*', '*', '#', '*', '*', '*', '*', '#'},
            {'#', '#', '#', '#', '*', '#', '#', '#', '*', '#', '*', '#', '#', '#', '*', '#', '#', '#', '#'},
            {'#', ' ', ' ', '#', '*', '#', '*', '*', '*', '*', '*', '*', '*', '#', '*', '#', ' ', ' ', '#'},
            {'#', '#', '#', '#', '*', '#', '*', '#', '#', ' ', '#', '#', '*', '#', '*', '#', '#', '#', '#'},
            {'#', '*', '*', '*', '*', '*', '*', '#', ' ', ' ', ' ', '#', '*', '*', '*', '*', '*', '*', '#'},
            {'#', '#', '#', '#', '*', '#', '*', '#', '#', '#', '#', '#', '*', '#', '*', '#', '#', '#', '#'},
            {'#', ' ', ' ', '#', '*', '#', '*', '*', '*', '@', '*', '*', '*', '#', '*', '#', ' ', ' ', '#'},
            {'#', '#', '#', '#', '*', '#', '*', '#', '#', '#', '#', '#', '*', '#', '*', '#', '#', '#', '#'},
            {'#', '*', '*', '*', '*', '*', '*', '*', '*', '#', '*', '*', '*', '*', '*', '*', '*', '*', '#'},
            {'#', '*', '#', '#', '*', '#', '#', '#', '*', '#', '*', '#', '#', '#', '*', '#', '#', '*', '#'},
            {'#', '*', '*', '#', '*', '*', '*', '*', '*', '*', '*', '*', '*', '*', '*', '#', '*', '*', '#'},
            {'#', '#', '*', '#', '*', '#', '*', '#', '#', '#', '#', '#', '*', '#', '*', '#', '*', '#', '#'},
            {'#', '*', '*', '*', '*', '#', '*', '*', '*', '#', '*', '*', '*', '#', '*', '*', '*', '*', '#'},
            {'#', '*', '#', '#', '#', '#', '#', '#', '*', '#', '*', '#', '#', '#', '#', '#', '#', '*', '#'},
            {'#', '*', '*', '*', '*', '*', '*', '*', '*', '*', '*', '*', '*', '*', '*', '*', '*', '*', '#'},
            {'#', '#', '#', '#', '#', '#', '#', '#', '#', '#', '#', '#', '#', '#', '#', '#', '#', '#', '#'},
    };

    @Override
    public void init() {
        this.chatScreen = PacPauseMenu::new;
        this.pauseScreen = PacPauseMenu::new;
        this.deathScreen = PacDeathScreen::new;

        this.hud = new PacHud();

        this.sky = new CubemapSky();
        ((CubemapSky) this.sky).setSkyBox(SkyBoxRegistry.WHITE.resource);
        this.sky.setTint(0xFF02011A);

        super.init();
    }

    @Override
    protected void levelLoad() {
        this.coins = 0;

        //floor
        Terrain floor = new PrimitiveTerrain(GeometryHelper.box(null, 1, -1, 1, map[0].length * 3 - 3, 0, map.length * 3 - 3, 0xFF02011A));
        addTerrain(floor);

        for (int i = 1; i < map.length - 1; i++) {
            for (int j = 1; j < map[i].length - 1; j++) {
                char c = map[i][j];

                switch (c) {
                    case '#' -> { continue; }
                    case '*' -> {
                        //coins
                        Entity coin = new Coin();
                        coin.setPos(j * 3 + 0.5f, 0.5f, i * 3 + 0.5f);
                        addEntity(coin);
                        coins++;
                    }
                    case '@' -> {
                        //player spawn
                        this.playerEntity.setPos(j * 3 + 0.5f, 0, i * 3 + 0.5f);
                        this.playerEntity.setRot(0f, -90f, 0f);
                    }
                }

                //top is wall
                if (map[i - 1][j] == '#') {
                    Terrain wall = new Wall();
                    wall.setPos(j * 3, 0, i * 3 - 1.5f);
                    addTerrain(wall);
                }

                //bottom is wall
                if (map[i + 1][j] == '#') {
                    Terrain wall = new Wall();
                    wall.setPos(j * 3, 0, i * 3 + 1.5f);
                    addTerrain(wall);
                }

                //left is wall
                if (map[i][j - 1] == '#') {
                    Terrain wall = new Wall();
                    wall.setPos(j * 3 - 1.5f, 0, i * 3);
                    wall.setRotation(0, 90, 0);
                    addTerrain(wall);
                }

                //right is wall
                if (map[i][j + 1] == '#') {
                    Terrain wall = new Wall();
                    wall.setPos(j * 3 + 1.5f, 0, i * 3);
                    wall.setRotation(0, 90, 0);
                    addTerrain(wall);
                }
            }
        }
    }

    @Override
    public void render(MatrixStack matrices, float delta) {
        WorldRenderer.renderClouds = false;
        WorldRenderer.renderShadows = false;
        WorldRenderer.renderWater = false;
        WorldRenderer.renderFire = false;
        WorldRenderer.renderSSR = false;
        WorldRenderer.renderLights = false;
        WorldRenderer.renderVolLight = false;
        WorldRenderer.renderDecals = false;
        WorldRenderer.renderRefract = false;
        super.render(matrices, delta);
    }

    @Override
    protected void updateSky(Camera camera, float delta) {
        setTime(6000);
        super.updateSky(camera, delta);
    }

    @Override
    protected void updateCamera(Camera sourceCamera, Entity camEntity, int cameraMode, float delta) {
        super.updateCamera(sourceCamera, camEntity, 0, delta);
    }

    @Override
    public void respawn(boolean init) {
        PacPlayer player = new PacPlayer();
        this.playerEntity = player;
        this.cameraEntity = player;
        this.addEntity(player);
    }

    public void awardScore(int amount) {
        score += amount;
        System.out.println("Score: " + score);
    }

    public void collectCoin() {
        collectedCoins++;
        awardScore(1);
    }

    public int getScore() {
        return score;
    }

    public int getCollectedCoins() {
        return collectedCoins;
    }

    public int getTotalCoins() {
        return coins;
    }
}
