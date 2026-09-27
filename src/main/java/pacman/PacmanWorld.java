package pacman;

import cinnamon.model.GeometryHelper;
import cinnamon.render.Camera;
import cinnamon.render.MatrixStack;
import cinnamon.render.WorldRenderer;
import cinnamon.world.Abilities;
import cinnamon.world.entity.Entity;
import cinnamon.world.terrain.PrimitiveTerrain;
import cinnamon.world.terrain.Terrain;
import cinnamon.world.world.WorldClient;

public class PacmanWorld extends WorldClient {

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
            {'#', '#', '#', '#', '*', '#', '*', '#', '#', '*', '#', '#', '*', '#', '*', '#', '#', '#', '#'},
            {'#', '*', '*', '*', '*', '*', '*', '#', '*', '*', '*', '#', '*', '*', '*', '*', '*', '*', '#'},
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
    protected void levelLoad() {
        //floor
        Terrain floor = new PrimitiveTerrain(GeometryHelper.box(null, 1, -1, 1, map[0].length * 3 - 3, 0, map.length * 3 - 3, 0xFF02011A));
        addTerrain(floor);

        for (int i = 1; i < map.length - 1; i++) {
            for (int j = 1; j < map[i].length - 1; j++) {
                char c = map[i][j];

                switch (c) {
                    case '#' -> { continue; }
                    case '*' -> {
                        //orb
                        Entity orb = new Coin();
                        orb.setPos(j * 3 + 0.5f, 0.5f, i * 3 + 0.5f);
                        addEntity(orb);
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
        WorldRenderer.renderSky = false;
        WorldRenderer.renderClouds = false;
        WorldRenderer.renderShadows = false;
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
        player.getAbilities()
                .set(Abilities.Ability.CAN_BUILD, false)
                .set(Abilities.Ability.CAN_FLY, true);

        this.playerEntity = player;
        this.cameraEntity = player;
        this.addEntity(player);
    }
}
