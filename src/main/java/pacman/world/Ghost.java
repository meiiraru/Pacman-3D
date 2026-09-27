package pacman.world;

import cinnamon.math.collision.Hit;
import cinnamon.registry.EntityRegistry;
import cinnamon.render.Camera;
import cinnamon.render.MatrixStack;
import cinnamon.utils.Resource;
import cinnamon.world.entity.PhysEntity;
import cinnamon.world.entity.living.Player;
import org.joml.Vector3f;

import java.util.UUID;

public class Ghost extends PhysEntity {

    private final GhostType type;
    private Direction currentDirection = Direction.UP;
    private int[] lastTile = new int[]{-1, -1};

    public Ghost(GhostType type) {
        super(UUID.randomUUID(), new Resource("pacman", "ghost/ghost.obj"));
        this.type = type;
        setGravity(0f);
    }

    @Override
    protected void renderModel(Camera camera, MatrixStack matrices, float delta) {
        if (model != null)
            model.render(matrices, model.getMaterials().get(type.name().toLowerCase()));
    }

    @Override
    public void tick() {
        super.tick();

        int[] tile = PacmanWorld.mapPosition(this);

        if ((tile[0] != lastTile[0] || tile[1] != lastTile[1]) && PacmanWorld.isInTileCenter(this)) {
            int[] target = PacmanWorld.mapPosition(((PacmanWorld) world).playerEntity);
            currentDirection = getNextDir(target[0], target[1], tile[0], tile[1]);
            lastTile = tile;
        }

        //rotate yaw towards the current direction
        this.rotateTo(0, currentDirection.rot, 0);

        //move in the current direction
        this.impulse(0f, 0f, 1f);
    }

    @Override
    protected void motionFallout() {
        this.motion.set(0f);
    }

    @Override
    public EntityRegistry getType() {
        return EntityRegistry.UNKNOWN;
    }

    @Override
    protected float getMoveSpeed() {
        return 1f + ((PacmanWorld) world).getDifficulty() * 0.5f;
    }

    @Override
    protected void collideEntity(PhysEntity entity, Hit result, Vector3f toMove) {
        if (entity instanceof Player player) {
            System.out.println("Player collided with ghost");
            player.kill();
        }
    }

    public Direction getNextDir(int targetX, int targetY, int tileX, int tileY) {
        Direction bestDirection = this.currentDirection;
        double shortestDistance = Double.MAX_VALUE;

        for (Direction dir : Direction.values()) {
            if (dir == this.currentDirection.getOpposite())
                continue;

            //check the coordinate of the potential next tile
            int nextTileX = tileX + dir.dx;
            int nextTileY = tileY + dir.dy;

            //avoid walls
            if (PacmanWorld.map[nextTileY][nextTileX] == '#')
                continue;

            //grab closest distance to target tile
            int distance = Math.abs(nextTileX - targetX) + Math.abs(nextTileY - targetY);
            if (distance < shortestDistance) {
                shortestDistance = distance;
                bestDirection = dir;
            }
        }

        return bestDirection;
    }

    public enum Direction {
        UP(0, -1, 0),
        DOWN(0, 1, 180),
        LEFT(-1, 0, 270),
        RIGHT(1, 0, 90);

        public final int dx, dy, rot;

        Direction(int dx, int dy, int rot) {
            this.dx = dx;
            this.dy = dy;
            this.rot = rot;
        }

        public Direction getOpposite() {
            return switch (this) {
                case UP -> DOWN;
                case DOWN -> UP;
                case LEFT -> RIGHT;
                case RIGHT -> LEFT;
            };
        }
    }

    public enum GhostType {
        BLINKY, //red    - chaser (directly targets player)
        PINKY,  //pink   - ambusher (aims ahead of player)
        INKY,   //cyan   - unpredictable (flanks using blinky position)
        CLYDE   //orange - shy (chases until close, then flees)
    }
}
