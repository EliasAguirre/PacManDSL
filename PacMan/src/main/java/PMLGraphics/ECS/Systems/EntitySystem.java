package PMLGraphics.ECS.Systems;

import PMLGraphics.ECS.Components.Component;
import PMLGraphics.ECS.Entity;
import PMLGraphics.ECS.EntityManager;

public abstract class EntitySystem {
    EntityManager entityManager;
    public abstract void update(float sec);
    public abstract void updateQueries(Entity e, Component component, boolean added);
}
