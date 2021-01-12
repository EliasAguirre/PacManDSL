package PMLGraphics.ECS.Components;

import java.util.UUID;

public class RenderableComponent implements Component {
    public static final UUID id = UUID.randomUUID();
    public RenderableAssetType assetType;
    public RenderableComponent(RenderableAssetType type) {
        this.assetType = type;
    }
    @Override
    public UUID getID() {
        return id;
    }
}
