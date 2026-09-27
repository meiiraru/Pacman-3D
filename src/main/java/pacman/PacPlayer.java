package pacman;

import cinnamon.model.GeometryHelper;
import cinnamon.registry.LivingModelRegistry;
import cinnamon.render.batch.VertexConsumer;
import cinnamon.world.Abilities;
import cinnamon.world.entity.living.LocalPlayer;

public class PacPlayer extends LocalPlayer {

    public PacPlayer() {
        super("player", LivingModelRegistry.STRAWBERRY);
        addRenderFeature((entity, camera, matrices, delta) -> {
            matrices.pushMatrix();
            matrices.translate(getPos(delta));
            applyModelPose(camera, matrices, delta);
            VertexConsumer.WORLD_MAIN.consume(GeometryHelper.sphere(matrices, 0, 0.75f, 0, 0.75f, 12, 0xFFFFD700));
            matrices.popMatrix();
        });
        setVisible(false);
        getAbilities().set(Abilities.Ability.CAN_BUILD, false);
    }

    @Override
    public void updateMovementFlags(boolean sneaking, boolean sprinting, boolean flying) {
        super.updateMovementFlags(false, false, flying);
    }

    @Override
    public void impulse(float left, float up, float forwards) {
        super.impulse(left, 0, forwards);
    }
}
