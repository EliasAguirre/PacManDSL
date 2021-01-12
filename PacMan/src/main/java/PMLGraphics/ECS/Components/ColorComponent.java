package PMLGraphics.ECS.Components;

import java.util.UUID;

public class ColorComponent implements Component {
    public final static UUID id = UUID.randomUUID();
    public float r, g, b;
    public ColorComponent(float r, float g, float b) {
        this.r = r;
        this.g = g;
        this.b = b;
    }
    @Override
    public UUID getID() {
        return id;
    }
}
