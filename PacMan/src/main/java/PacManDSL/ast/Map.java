package PacManDSL.ast;

public class Map extends Statement {
    private int width, height;

    public Map(int width, int height){
        this.width = width;
        this.height = height;
    }

    public int getWidth(){
        return width;
    }

    public void setWidth(int width){
        this.width = width;
    }

    public int getHeight(){
        return height;
    }

    public void setHeight(int height){
        this.height = height;
    }

    @Override
    public <C, T> T accept(C context, PacManVisitor<C, T> v) {
        return v.visit(context, this);
    }
}
