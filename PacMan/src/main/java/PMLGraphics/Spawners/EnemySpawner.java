package PMLGraphics.Spawners;

import PMLGraphics.ECS.Components.*;
import PMLGraphics.ECS.Entity;
import PMLGraphics.ECS.EntityManager;
import PMLGraphics.Util.RenderingUtil;
import PacManDSL.ast.Enemy;
import PacManDSL.libs.Pair;

import java.util.List;

public class EnemySpawner implements Spawner{
    @Override
    public void update(long sec) {

    }

    public void createEnemy(EntityManager manager, float x, float y, String color, int speed, List<Pair<Enemy.DIRECTION, Integer>> moveList) {
        List<Float> rgb = RenderingUtil.getHexFromString(color);
        Entity enemy = manager.createEntity();
        manager.addComponent(enemy, new EnemyComponent());
        manager.addComponent(enemy, new ColorComponent(rgb.get(0), rgb.get(1), rgb.get(2)));
        manager.addComponent(enemy, new PositionComponent(x - 0.5f, y - 0.5f));
        manager.addComponent(enemy, new RenderableComponent(RenderableAssetType.ENEMY));
        manager.addComponent(enemy, new SpeedComponent((float)(speed * 0.03)));
        manager.addComponent(enemy, new CollidableComponent());
        manager.addComponent(enemy, new AABBComponent(0.5f, 0.5f));
        manager.addComponent(enemy, createLoopComponent(moveList));
    }

    private LoopComponent createLoopComponent(List<Pair<Enemy.DIRECTION, Integer>> list) {
        LoopComponent loopComponent = new LoopComponent();
        for (Pair<Enemy.DIRECTION, Integer> step: list) {
            if (step.getKey() == Enemy.DIRECTION.DOWN) {
                loopComponent.addLoopStep(new Pair<>(0, -1 * step.getValue()));
            } else if (step.getKey() == Enemy.DIRECTION.UP) {
                loopComponent.addLoopStep(new Pair<>(0, step.getValue()));
            } else if (step.getKey() == Enemy.DIRECTION.RIGHT) {
                loopComponent.addLoopStep(new Pair<>(step.getValue(), 0));
            } else if (step.getKey() == Enemy.DIRECTION.LEFT) {
                loopComponent.addLoopStep(new Pair<>(-1 * step.getValue(), 0));
            }
        }
        return loopComponent;
    }
}
