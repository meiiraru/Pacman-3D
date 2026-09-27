package pacman.gui;

import cinnamon.Client;
import cinnamon.gui.DebugScreen;
import cinnamon.math.Maths;
import cinnamon.render.MatrixStack;
import cinnamon.render.WorldRenderer;
import cinnamon.render.batch.VertexConsumer;
import cinnamon.text.Text;
import cinnamon.utils.Alignment;
import cinnamon.vr.XrManager;
import cinnamon.world.gui.Hud;
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
