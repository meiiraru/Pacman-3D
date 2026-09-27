package pacman.world;

import cinnamon.model.material.Material;
import cinnamon.registry.TerrainRegistry;
import cinnamon.render.Camera;
import cinnamon.render.MatrixStack;
import cinnamon.render.shader.Shader;
import cinnamon.utils.Resource;
import cinnamon.world.terrain.Terrain;

public class Wall extends Terrain {

    public Wall() {
        super(new Resource("pacman", "wall/wall.obj"), TerrainRegistry.CUSTOM);
    }

    @Override
    protected void renderModel(Camera camera, Material material, MatrixStack matrices, float delta) {
        Shader.activeShader.applyColorRGBA(0xFF0065C4);
        super.renderModel(camera, material, matrices, delta);
        Shader.activeShader.applyColorRGBA(0xFFFFFFFF);
    }
}
