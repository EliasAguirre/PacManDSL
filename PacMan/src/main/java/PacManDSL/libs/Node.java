package PacManDSL.libs;

import PacManDSL.ast.PacManVisitor;

public abstract class Node {
    abstract public <C, T> T accept(C context, PacManVisitor<C, T> v);
}
