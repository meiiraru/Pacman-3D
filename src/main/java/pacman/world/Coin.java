package pacman.world;

import cinnamon.registry.EntityRegistry;
import cinnamon.sound.SoundCategory;
import cinnamon.utils.Resource;
import cinnamon.world.entity.PhysEntity;
import cinnamon.world.entity.collectable.Collectable;
import cinnamon.world.entity.living.Player;

import java.util.UUID;

public class Coin extends Collectable {

    public Coin() {
        super(UUID.randomUUID(), new Resource("pacman", "coin/coin.obj"));
        setGravity(0f);
        setScale(0.5f);
    }

    @Override
    protected boolean onPickUp(PhysEntity entity) {
        if (!(entity instanceof Player))
            return false;

        PacmanWorld world = (PacmanWorld) entity.getWorld();
        world.playSound(new Resource("pacman", "coin/coin.wav"), SoundCategory.ENTITY, entity.getTransform().getPos());
        world.collectCoin();
        return true;
    }

    @Override
    public EntityRegistry getType() {
        return EntityRegistry.UNKNOWN;
    }
}
