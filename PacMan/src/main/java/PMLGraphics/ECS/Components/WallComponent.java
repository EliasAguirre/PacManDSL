package PMLGraphics.ECS.Components;

import java.util.UUID;

public class WallComponent implements Component {
    public final static UUID id = UUID.randomUUID();
    @Override
    public UUID getID() {
        return id;
    }
}
