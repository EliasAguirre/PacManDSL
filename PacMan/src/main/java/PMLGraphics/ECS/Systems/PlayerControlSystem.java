package PMLGraphics.ECS.Systems;

import PMLGraphics.ECS.Components.Component;
import PMLGraphics.ECS.Components.PlayerComponent;
import PMLGraphics.ECS.Components.PositionComponent;
import PMLGraphics.ECS.Components.SpeedComponent;
import PMLGraphics.ECS.Entity;
import PMLGraphics.ECS.EntityManager;
import PMLGraphics.Game.World;

import java.util.ArrayList;
import java.util.UUID;

public class PlayerControlSystem extends EntitySystem {
    private final ArrayList<Entity> playerControlledEntities;
    private final UUID playerComponentId = PlayerComponent.id;
    private final World world;

    public PlayerControlSystem(EntityManager entityManager, World world) {
        this.playerControlledEntities = new ArrayList<>();
        this.world = world;
        this.entityManager = entityManager;
        this.entityManager.subscribe(this);
    }
    @Override
    public void update(float sec) {
        for (Entity e: playerControlledEntities) {
            updateEntity(e);
        }
    }

    private void updateEntity(Entity entity) {
        KeyboardService keyboardService = KeyboardService.getInstance();
        PlayerComponent playerComponent = (PlayerComponent) entity.getComponent(PlayerComponent.id);
        PositionComponent positionComponent = (PositionComponent) entity.getComponent(PositionComponent.id);
        SpeedComponent speedComponent = (SpeedComponent) entity.getComponent(SpeedComponent.id);
        float newX = positionComponent.x;
        float newY = positionComponent.y;
        float speed = speedComponent.speed;
        if (keyboardService.isPressed(KeyboardService.Key.UP) && !playerComponent.isBlocked(KeyboardService.Key.UP)) {
            newY = Math.min(newY + speed, this.world.getHeight() - 0.5f);
        }
        if (keyboardService.isPressed(KeyboardService.Key.DOWN) && !playerComponent.isBlocked(KeyboardService.Key.DOWN)) {
            newY = Math.max(newY - speed, 0.5f);
        }
        if (keyboardService.isPressed(KeyboardService.Key.RIGHT) && !playerComponent.isBlocked(KeyboardService.Key.RIGHT)) {
            newX = Math.min(newX + speed, this.world.getWidth() - 0.5f);
        }
        if (keyboardService.isPressed(KeyboardService.Key.LEFT) && !playerComponent.isBlocked(KeyboardService.Key.LEFT)) {
            newX = Math.max(newX - speed, 0.5f);
        }
        boolean entityHasMoved = newX != positionComponent.x || newY != positionComponent.y;
        if (entityHasMoved) {
            PositionComponent newPosition = new PositionComponent(newX, newY);
            entity.addComponent(newPosition);
        }
        playerComponent.reset();
    }

    @Override
    public void updateQueries(Entity e, Component component, boolean added) {
        if (!playerControlledEntities.contains(e)) {
            if (added && component.getID() == playerComponentId) {
                playerControlledEntities.add(e);
            }
        } else if (!added) {
            playerControlledEntities.remove(e);
        }
    }
}
