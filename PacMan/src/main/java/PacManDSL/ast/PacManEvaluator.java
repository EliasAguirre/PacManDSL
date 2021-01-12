package PacManDSL.ast;

import PMLGraphics.ECS.Components.RenderableAssetType;
import PMLGraphics.Game.World;

public class PacManEvaluator implements PacManVisitor<Void, Integer> {
    private World world;

    @Override
    public Integer visit(Void v, Program p) {
        for (Statement s : p.getStatements()) {
            s.accept(null, this);
        }
        while (!world.isOver()) {
            world.update(0);
        }
        world.destroy();
        return null;
    }

    @Override
    public Integer visit(Void v,Map m) {
        world = new World(m.getWidth(), m.getHeight());
        return null;
    }

    @Override
    public Integer visit(Void v,BuildWall b) {
        world.addWall(b.getStart_x(), b.getStart_y(), b.getWidth(), b.getHeight());
        return null;
    }

    @Override
    public Integer visit(Void v,Enemy e) {
        world.addEnemy(e.getStart_x(), e.getStart_y(), e.getColour(), e.getSpeed(), e.getMoveList());
        return null;
    }

    @Override
    public Integer visit(Void v,WallColour w) {
        world.updateAssetColor(RenderableAssetType.WALL, w.getColour());
        return null;
    }

    @Override
    public Integer visit(Void v,CharacterColour c) {
        world.updateAssetColor(RenderableAssetType.PLAYER, c.getColour());
        return null;
    }

    @Override
    public Integer visit(Void v,MapColour m) {
        world.updateMapColor(m.getColour());
        return null;
    }
}