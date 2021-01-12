package PMLGraphics.ECS.Components;

import java.util.UUID;

public class CollidableComponent implements Component {
    public static final UUID id = UUID.randomUUID();
    @Override
    public UUID getID() {
        return id;
    }
}
