package pacman;

import cinnamon.registry.EntityRegistry;
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
        return entity instanceof Player;
    }

    @Override
    public EntityRegistry getType() {
        return EntityRegistry.UNKNOWN;
    }
}
