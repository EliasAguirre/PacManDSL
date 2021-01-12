package PMLGraphics.ECS.Components;

import java.util.UUID;

public class EndGoalComponent implements Component {
    public static UUID id = UUID.randomUUID();
    @Override
    public UUID getID() {
        return id;
    }
}
