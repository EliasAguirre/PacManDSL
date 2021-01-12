package PMLGraphics.Spawners;

import PMLGraphics.ECS.Components.*;
import PMLGraphics.ECS.Entity;
import PMLGraphics.ECS.EntityManager;

public class PlayerSpawner implements Spawner {
    Entity player;
    @Override
    public void update(long sec) {}

    public void createPlayer(EntityManager manager, float x, float y) {
        player = manager.createEntity();
        manager.addComponent(player, new PlayerComponent());
        manager.addComponent(player, new ColorComponent((float)144/255, (float)238/255, (float)144/255));
        manager.addComponent(player, new PositionComponent(x, y));
        manager.addComponent(player, new RenderableComponent(RenderableAssetType.PLAYER));
        manager.addComponent(player, new SpeedComponent((float)0.1));
        manager.addComponent(player, new CollidableComponent());
        manager.addComponent(player, new AABBComponent(0.5f, 0.5f));
    }

    public void reset(float x, float y) {
        player.addComponent(new PositionComponent(x, y));
    }
}
