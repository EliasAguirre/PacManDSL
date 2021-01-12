package PMLGraphics.ECS.Systems;

import PMLGraphics.ECS.Components.*;
import PMLGraphics.ECS.Entity;
import PMLGraphics.ECS.EntityManager;

import java.util.ArrayList;
import java.util.UUID;

public class CollisionSystem extends EntitySystem {
    private final ArrayList<Entity> collidableEntityQuery;
    private Entity player = null;
    private final UUID playerComponentId = PlayerComponent.id;
    private final UUID enemyComponentId = EnemyComponent.id;
    private final UUID wallComponentId = WallComponent.id;
    private final UUID endGoalComponentId = EndGoalComponent.id;

    public CollisionSystem(EntityManager entityManager) {
        this.collidableEntityQuery = new ArrayList<>();
        this.entityManager = entityManager;
        this.entityManager.subscribe(this);
    }
    @Override
    public void update(float sec) {
        for (Entity e: collidableEntityQuery) {
            checkCollisionWithPlayer(e);
        }
    }

    private void checkCollisionWithPlayer(Entity e) {
        PlayerComponent playerComponent = (PlayerComponent) player.getComponent(PlayerComponent.id);
        PositionComponent playerPosition = (PositionComponent) player.getComponent(PositionComponent.id);
        AABBComponent playerAABB = (AABBComponent) player.getComponent(AABBComponent.id);
        PositionComponent collidablePosition = (PositionComponent) e.getComponent(PositionComponent.id);
        AABBComponent collidableAABB = (AABBComponent) e.getComponent(AABBComponent.id);
        if (isColliding(playerPosition, playerAABB, collidablePosition, collidableAABB)) {
            if (e.hasComponent(enemyComponentId)) {
                player.addComponent(new PositionComponent(0.5f, 0.5f));
            } else if (e.hasComponent(wallComponentId)){
                /* Code encapsulated within this if statement derived from top answer in
                https://stackoverflow.com/questions/5062833/detecting-the-direction-of-a-collision*/
                float playerBottom = playerPosition.y + playerAABB.halfY;
                float wallBottom = collidablePosition.y + collidableAABB.halfY;
                float playerRight = playerPosition.x + playerAABB.halfX;
                float wallRight = collidablePosition.x + collidableAABB.halfX;
                float bottomCollision = wallBottom - playerPosition.y;
                float topCollision = playerBottom - collidablePosition.y;
                float leftCollision = playerRight - collidablePosition.x;
                float rightCollision = wallRight - playerPosition.x;
                if (topCollision < bottomCollision && topCollision < leftCollision && topCollision < rightCollision) {
                    playerComponent.blockDirection(KeyboardService.Key.DOWN);
                    player.addComponent(new PositionComponent(playerPosition.x, collidablePosition.y - collidableAABB.halfY - playerAABB.halfY));
                }
                else if (bottomCollision < topCollision && bottomCollision < leftCollision && bottomCollision < rightCollision) {
                   playerComponent.blockDirection(KeyboardService.Key.UP);
                    player.addComponent(new PositionComponent(playerPosition.x, collidablePosition.y + collidableAABB.halfY + playerAABB.halfY));
                }
                else if (rightCollision < topCollision && rightCollision < leftCollision && rightCollision < bottomCollision) {
                    playerComponent.blockDirection(KeyboardService.Key.LEFT);
                    player.addComponent(new PositionComponent(collidablePosition.x + collidableAABB.halfX + playerAABB.halfX, playerPosition.y));
                }
                else if (leftCollision < topCollision && leftCollision < bottomCollision && leftCollision < rightCollision) {
                    playerComponent.blockDirection(KeyboardService.Key.RIGHT);
                    player.addComponent(new PositionComponent(collidablePosition.x - collidableAABB.halfX - playerAABB.halfX, playerPosition.y));
                }
            } else if (e.hasComponent(endGoalComponentId)) {
                GameStateService.getInstance().changeState(GameStateService.GameState.WON);
            }
        }
    }

    private boolean isColliding(PositionComponent pos1, AABBComponent aabb1, PositionComponent pos2, AABBComponent aabb2) {
        float x1 = pos1.x;
        float y1 = pos1.y;
        float halfX1 = aabb1.getHalfX();
        float halfY1 = aabb1.getHalfY();
        float x2 = pos2.x;
        float y2 = pos2.y;
        float halfX2 = aabb2.getHalfX();
        float halfY2 = aabb2.getHalfY();
        return (x1 - halfX1 < x2 + halfX2 &&
                x1 + halfX1 > x2 - halfX2 &&
                y1 - halfY1 < y2 + halfY2 &&
                y1 + halfY1 > y2 - halfY2    );
    }

    @Override
    public void updateQueries(Entity e, Component component, boolean added) {
        if (!collidableEntityQuery.contains(e)) {
            if (added && component.getID() == playerComponentId) {
                player = e;
            } else if (added && component.getID() == CollidableComponent.id) {
                collidableEntityQuery.add(e);
            }
        } else if (!added) {
            collidableEntityQuery.remove(e);
        }
    }
}
