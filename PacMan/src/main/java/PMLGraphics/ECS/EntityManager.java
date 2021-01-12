package PMLGraphics.ECS;

import PMLGraphics.ECS.Components.Component;
import PMLGraphics.ECS.Systems.EntitySystem;

import java.util.ArrayList;
import java.util.Stack;

public class EntityManager {
    private final Stack<Entity> reusableEntities;
    private final ArrayList<Entity> entities;
    private final ArrayList<EntitySystem> subscribedSystems;

    public EntityManager() {
        reusableEntities = new Stack<>();
        entities = new ArrayList<>();
        subscribedSystems = new ArrayList<>();
    }

    public Entity createEntity() {
        Entity entity;
        if (reusableEntities.size() > 0) {
            entity = reusableEntities.pop();
        } else {
            entity = new Entity();
        }
        entities.add(entity);
        return entity;
    }

    void destroyEntity(Entity entity) {
        reusableEntities.push(entity);
        entities.remove(entity);
    }

    public void addComponent(Entity entity, Component component) {
        entity.addComponent(component);
        for (EntitySystem system: subscribedSystems) {
            system.updateQueries(entity, component, true);
        }
    }

    public  void removeComponent(Entity entity, Component component) {
        entity.removeComponent(component.getID());
        for (EntitySystem system: subscribedSystems) {
            system.updateQueries(entity, component, false);
        }
    }

    public void subscribe(EntitySystem system) {
        subscribedSystems.add(system);
    }

    public void unsubscribe(EntitySystem system) {
        subscribedSystems.remove(system);
    }
}
