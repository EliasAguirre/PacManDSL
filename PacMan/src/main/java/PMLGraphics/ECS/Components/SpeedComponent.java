package PMLGraphics.ECS.Components;

import java.util.UUID;

public class SpeedComponent implements Component {
    public static final UUID id = UUID.randomUUID();
    public float speed;
    public SpeedComponent(float speed) {
        this.speed = speed;
    }
    @Override
    public UUID getID() {
        return id;
    }
}