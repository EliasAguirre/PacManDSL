package PMLGraphics.Spawners;

import PMLGraphics.ECS.Components.*;
import PMLGraphics.ECS.Entity;
import PMLGraphics.ECS.EntityManager;

public class WallSpawner implements Spawner {
    public void createWall(EntityManager manager, float x, float y) {
        Entity wall = manager.createEntity();
        manager.addComponent(wall, new WallComponent());
        manager.addComponent(wall, new ColorComponent((float) 96/255, (float) 108/255, (float) 125/255)); //set to default colour for now
        manager.addComponent(wall, new PositionComponent(x - 0.5f, y - 0.5f));
        manager.addComponent(wall, new RenderableComponent(RenderableAssetType.WALL));
        manager.addComponent(wall, new CollidableComponent());
        manager.addComponent(wall, new AABBComponent(0.5f, 0.5f));
    }

    public void createWall(EntityManager manager, float startX, float startY, int width, int height) {
        int dx = width > 0? 1 : -1;
        int dy = height > 0? 1 : -1;
        for (float x = startX; x != startX + width; x += dx) {
            for (float y = startY; y != startY + height; y += dy) {
                createWall(manager, x, y);
            }
        }
    }

    @Override
    public void update(long sec) {

    }
}
