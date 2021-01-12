package PacManDSL.ast;

public class WallColour extends Statement {
    private String colour;

    public WallColour(String colour){
        this.colour = colour;
    }

    public String getColour() {
        return colour;
    }

    public void setColour(String colour) {
        this.colour = colour;
    }

    @Override
    public <C, T> T accept(C context, PacManVisitor<C, T> v) {
        return v.visit(context, this);
    }
}
