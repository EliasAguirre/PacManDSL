package PMLGraphics.ECS.Components;

import java.util.UUID;

public class PositionComponent implements Component{
    public final static UUID id = UUID.randomUUID();
    public float x, y;
    public PositionComponent(float x, float y) {
        this.x = x;
        this.y = y;
    }
    @Override
    public UUID getID() {
        return id;
    }
}
