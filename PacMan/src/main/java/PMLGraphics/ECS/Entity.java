package PMLGraphics.ECS;

import PMLGraphics.ECS.Components.Component;

import java.util.HashMap;
import java.util.UUID;

public class Entity {
    private final UUID id = UUID.randomUUID();
    private final HashMap<UUID, Component> components;

    Entity() {
        components = new HashMap<>();
    }

    @Override
    public boolean equals(Object o) {
        if (o == this) {
            return true;
        }
        if (!(o instanceof Entity)) {
            return false;
        }
        Entity e = (Entity) o;
        return this.id == e.id;
    }

    @Override
    public int hashCode() {
        return id.hashCode();
    }

    public void addComponent(Component component) {
        components.put(component.getID(), component);
    }

    public void removeComponent(Component component) {
        components.remove(component.getID());
    }

    public void removeComponent(UUID id) {
        components.remove(id);
    }

    public boolean hasComponent(UUID id) {
        return components.containsKey(id);
    }

    public Component getComponent(UUID id) throws Error {
        if (components.containsKey(id)) {
            return components.get(id);
        }
        throw new Error("No component with id found");
    }
}
