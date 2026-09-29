package pacman.gui;

import cinnamon.Client;
import cinnamon.gui.DebugScreen;
import cinnamon.math.Maths;
import cinnamon.model.GeometryHelper;
import cinnamon.render.MatrixStack;
import cinnamon.render.WorldRenderer;
import cinnamon.render.batch.VertexConsumer;
import cinnamon.text.Text;
import cinnamon.utils.Alignment;
import cinnamon.utils.Colors;
import cinnamon.vr.XrManager;
import cinnamon.world.entity.Entity;
import cinnamon.world.gui.Hud;
import pacman.Main;
import pacman.world.Coin;
import pacman.world.Ghost;
import pacman.world.PacmanWorld;

public class PacHud extends Hud {

    @Override
    public void render(MatrixStack matrices, float delta) {
        Client c = Client.getInstance();
        PacmanWorld world = (PacmanWorld) c.world;

        //draw chat first
        drawRecentChat(matrices, c, delta);

        //score
        Text.of(world.getScore() + "00 pts")
                .render(VertexConsumer.MAIN, matrices, 4, 4, Alignment.TOP_LEFT);

        //coins
        Text.of(world.getCollectedCoins() + "/" + world.getTotalCoins() + " \u00A2")
                .render(VertexConsumer.MAIN, matrices, c.window.getGUIWidth() - 4, 4, Alignment.TOP_RIGHT);

        //compass
        float a = 20f;
        matrices.pushMatrix();
        matrices.translate(c.window.getGUIWidth() / 2f, 4f, 0f);
        matrices.rotateY(Maths.getYaw(world.playerEntity.getTransform().getRot()));

        matrices.pushMatrix();
        matrices.translate(0f, 0f, a);
        Text.of("N")
                .render(VertexConsumer.MAIN, matrices, 0, 0, Alignment.TOP_CENTER);
        matrices.popMatrix();

        matrices.pushMatrix();
        matrices.rotateY(90f);
        matrices.translate(0f, 0f, a);
        Text.of("W")
                .render(VertexConsumer.MAIN, matrices, 0, 0, Alignment.TOP_CENTER);
        matrices.popMatrix();

        matrices.pushMatrix();
        matrices.rotateY(180f);
        matrices.translate(0f, 0f, a);
        Text.of("S")
                .render(VertexConsumer.MAIN, matrices, 0, 0, Alignment.TOP_CENTER);
        matrices.popMatrix();

        matrices.pushMatrix();
        matrices.rotateY(270f);
        matrices.translate(0f, 0f, a);
        Text.of("E")
                .render(VertexConsumer.MAIN, matrices, 0, 0, Alignment.TOP_CENTER);
        matrices.popMatrix();

        matrices.popMatrix();

        //debug minimap
        if (Main.renderDebug.get()) {
            float s = 4f;
            matrices.pushMatrix();
            matrices.translate(c.window.getGUIWidth() - PacmanWorld.map[0].length * s - 4, c.window.getGUIHeight() - PacmanWorld.map.length * s - 4, 0f);

            for (int i = 0; i < PacmanWorld.map.length; i++) {
                for (int j = 0; j < PacmanWorld.map[i].length; j++) {
                    if (PacmanWorld.map[i][j] == '#')
                        VertexConsumer.MAIN.consume(GeometryHelper.rectangle(matrices, j * s, i * s, (j + 1) * s, (i + 1) * s, Colors.BLUE.argb));
                }
            }
            int[] pos = PacmanWorld.mapPosition(world.playerEntity);
            VertexConsumer.MAIN.consume(GeometryHelper.rectangle(matrices, pos[0] * s, pos[1] * s, (pos[0] + 1) * s, (pos[1] + 1) * s, 0xFFFFFF00));

            for (Entity entity : world.getAllEntities()) {
                if (entity instanceof Ghost g) {
                    pos = PacmanWorld.mapPosition(entity);
                    VertexConsumer.MAIN.consume(GeometryHelper.rectangle(matrices, pos[0] * s, pos[1] * s, (pos[0] + 1) * s, (pos[1] + 1) * s, g.getGhostType().color.argb));
                }

                if (entity instanceof Coin) {
                    pos = PacmanWorld.mapPosition(entity);
                    VertexConsumer.MAIN.consume(GeometryHelper.rectangle(matrices, pos[0] * s + s/4f, pos[1] * s + s/4f, (pos[0] + 1) * s - s/4f, (pos[1] + 1) * s - s/4f, 0xFFFFFFFF));
                }
            }

            matrices.popMatrix();
        }

        //draw fade
        if (!XrManager.isInXR() && fadeTicks > 0)
            drawFade(matrices, c, delta);

        //finish rendering
        WorldRenderer.finishMaterials(c.camera);

        //draw crosshair separated
        if (!DebugScreen.isActive() && !XrManager.isInXR())
            drawCrosshair(matrices);
    }
}
