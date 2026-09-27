package pacman.world;

import cinnamon.math.Maths;
import cinnamon.math.collision.Hit;
import cinnamon.registry.EntityRegistry;
import cinnamon.render.Camera;
import cinnamon.render.MatrixStack;
import cinnamon.utils.Colors;
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
            int[] target = getTargetTile(tile);
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
        return 1.5f + ((PacmanWorld) world).getDifficulty() * 0.75f;
    }

    @Override
    protected void collideEntity(PhysEntity entity, Hit result, Vector3f toMove) {
        if (entity instanceof Player player) {
            System.out.println("Player collided with ghost");
            player.kill();
        }
    }

    public GhostType getGhostType() {
        return type;
    }

    private int[] getTargetTile(int[] ghostTile) {
        Player player = (Player) ((PacmanWorld) world).playerEntity;
        int[] playerPos = PacmanWorld.mapPosition(player);

        return switch (this.type) {
            //blinky directly targets the player current tile
            case BLINKY -> playerPos;
            //pinky targets 2 tiles ahead of the player current direction
            case PINKY -> {
                Direction pDir = getEntityDirection(player);
                yield new int[]{playerPos[0] + (pDir.dx * 2), playerPos[1] + (pDir.dy * 2)};
            }
            //inky picks a pivot 1 tiles ahead of the player, draws a vector from blinky to that pivot, and doubles it
            case INKY -> {
                Direction pDir = getEntityDirection(player);
                int pivotX = playerPos[0] + pDir.dx;
                int pivotY = playerPos[1] + pDir.dy;

                Ghost blinky = ((PacmanWorld) world).findBlinky();
                if (blinky != null) {
                    int[] blinkyPos = PacmanWorld.mapPosition(blinky);
                    int vectorX = pivotX - blinkyPos[0];
                    int vectorY = pivotY - blinkyPos[1];
                    yield new int[]{pivotX + vectorX, pivotY + vectorY};
                }
                //fallback to pinky logic if blinky is missing
                yield new int[]{pivotX, pivotY};
            }
            //clyde targets the player if further than 8 tiles away
            //if 8 tiles or closer, he retreats to his scatter corner (bottom-left)
            case CLYDE -> {
                int distSq = getDistanceSquared(ghostTile[0], ghostTile[1], playerPos[0], playerPos[1]);
                if (distSq > 8 * 8) {
                    yield playerPos;
                } else {
                    yield new int[]{0, PacmanWorld.map.length - 2}; //bottom-left corner
                }
            }
        };
    }

    private Direction getNextDir(int targetX, int targetY, int tileX, int tileY) {
        Direction bestDirection = this.currentDirection;
        int shortestDistanceSq = Integer.MAX_VALUE;

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
            int distanceSq = getDistanceSquared(nextTileX, nextTileY, targetX, targetY);
            if (distanceSq < shortestDistanceSq) {
                shortestDistanceSq = distanceSq;
                bestDirection = dir;
            }
        }

        //check if forwards is a wall, and if so, run backwards
        int forwardTileX = tileX + bestDirection.dx;
        int forwardTileY = tileY + bestDirection.dy;
        if (PacmanWorld.map[forwardTileY][forwardTileX] == '#')
            bestDirection = bestDirection.getOpposite();

        return bestDirection;
    }

    private int getDistanceSquared(int x1, int y1, int x2, int y2) {
        int dx = x1 - x2;
        int dy = y1 - y2;
        return (dx * dx) + (dy * dy);
    }

    private Direction getEntityDirection(PhysEntity entity) {
        float yaw = Maths.dirToRot(entity.getMotion().normalize(new Vector3f())).y;
        //normalize rotation to 0-360 degrees
        float normalized = (yaw % 360 + 360) % 360;

        if (normalized >=  45 && normalized < 135) return Direction.RIGHT; //90
        if (normalized >= 135 && normalized < 225) return Direction.DOWN;  //180
        if (normalized >= 225 && normalized < 315) return Direction.LEFT;  //270
        return Direction.UP; //0
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
        BLINKY(Colors.RED),   //red    - chaser (directly targets player)
        PINKY(Colors.PINK),   //pink   - ambusher (aims ahead of player)
        INKY(Colors.CYAN),    //cyan   - unpredictable (flanks using blinky position)
        CLYDE(Colors.ORANGE); //orange - shy (chases until close, then flees)

        public final Colors color;

        GhostType(Colors color) {
            this.color = color;
        }
    }
}
