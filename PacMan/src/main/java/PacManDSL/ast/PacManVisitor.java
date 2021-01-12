package PacManDSL.ast;

public interface PacManVisitor<C, T> {
    T visit(C context, Program p);
    T visit(C context,BuildWall b);
    T visit(C context,Enemy e);
    T visit(C context,WallColour w);
    T visit(C context,CharacterColour c);
    T visit(C context,MapColour m);
    T visit(C context, Map map);
}
