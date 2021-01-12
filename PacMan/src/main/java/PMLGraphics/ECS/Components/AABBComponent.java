package PMLGraphics.ECS.Components;

import java.util.UUID;

public class AABBComponent implements Component {
    public static final UUID id = UUID.randomUUID();
    public float halfX, halfY;
    public AABBComponent(float halfX, float halfY) {
        this.halfX = halfX;
        this.halfY = halfY;
    }

    public float getHalfX() {
        return halfX;
    }

    public float getHalfY() {
        return halfY;
    }

    @Override
    public UUID getID() {
        return id;
    }
}
