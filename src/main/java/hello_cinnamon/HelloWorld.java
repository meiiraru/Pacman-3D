package hello_cinnamon;

import cinnamon.gui.Toast;
import cinnamon.registry.EntityRegistry;
import cinnamon.sound.SoundCategory;
import cinnamon.sound.SoundManager;
import cinnamon.utils.Resource;
import cinnamon.world.entity.PhysEntity;
import cinnamon.world.entity.collectable.Collectable;
import cinnamon.world.entity.misc.Spawner;
import cinnamon.world.world.WorldClient;

import java.util.UUID;

public class HelloWorld extends WorldClient {

    private int score = 0;

    @Override
    protected void levelLoad() {
        //basic world loading (world floor)
        super.levelLoad();

        //reset the score
        this.score = 0;

        //chips spawner
        Spawner<Collectable> chipsSpawner = new Spawner<>(UUID.randomUUID(), 1, () ->
                //spawn a custom collectable entity that increases the player score when picked up
                new Collectable(UUID.randomUUID(), new Resource("hello_cinnamon", "models/chips.obj")) {
                    @Override
                    protected boolean onPickUp(PhysEntity entity) {
                        //not the player
                        if (entity != HelloWorld.this.playerEntity)
                            return false;

                        //increase the score
                        HelloWorld.this.score++;

                        //feedback to the player
                        SoundManager.playSound(new Resource("hello_cinnamon", "sounds/eat1.ogg"), SoundCategory.ENTITY, getTransform().getPos());
                        Toast.addToast("Score: " + HelloWorld.this.score);

                        return true;
                    }

                    @Override
                    public EntityRegistry getType() {
                        return EntityRegistry.UNKNOWN;
                    }
                });
        chipsSpawner.setPos(0, 5, 0);
        chipsSpawner.setExtents(7.5f, 0.5f, 7.5f);
        addEntity(chipsSpawner);
    }
}
