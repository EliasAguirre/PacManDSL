package PMLGraphics.ECS.Systems;

import PMLGraphics.ECS.Components.*;
import PMLGraphics.ECS.Entity;
import PMLGraphics.ECS.EntityManager;

import java.util.ArrayList;
import java.util.UUID;

public class EnemyMovementSystem extends EntitySystem {
    private final ArrayList<Entity> movableEntityQuery;
    private final UUID enemyComponentId = EnemyComponent.id;

    public EnemyMovementSystem(EntityManager entityManager) {
        this.movableEntityQuery = new ArrayList<>();
        this.entityManager = entityManager;
        entityManager.subscribe(this);
    }

    @Override
    public void update(float sec) {
        for (Entity e: movableEntityQuery) {
            moveEntity(e);
        }
    }

    private void moveEntity(Entity e) {
        PositionComponent positionComponent = (PositionComponent) e.getComponent(PositionComponent.id);
        SpeedComponent speedComponent = (SpeedComponent) e.getComponent(SpeedComponent.id);
        LoopComponent loopComponent = (LoopComponent) e.getComponent(LoopComponent.id);
        e.addComponent(loopComponent.move(positionComponent, speedComponent));
    }

    @Override
    public void updateQueries(Entity e, Component component, boolean added) {
        if (!movableEntityQuery.contains(e)) {
            if (added && component.getID() == enemyComponentId) {
                movableEntityQuery.add(e);
            }
        } else if (!added) {
            movableEntityQuery.remove(e);
        }
    }
}
