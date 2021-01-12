package PacManDSL.ast;

import PacManDSL.libs.Pair;

import java.util.ArrayList;
import java.util.List;

public class Enemy extends Statement {
    private List<Pair<DIRECTION, Integer>> moveList;
    private int start_x, start_y, speed;
    private String colour;
    public enum DIRECTION{
        UP,
        DOWN,
        LEFT,
        RIGHT
    }

    public Enemy(int start_x, int start_y){
        this.start_x = start_x;
        this.start_y = start_y;
        this.colour = "FF0000";
        this.speed = 0;
        moveList = new ArrayList<Pair<DIRECTION, Integer>>();
    }

    public int getStart_x() {
        return start_x;
    }

    public void setStart_x(int start_x) {
        this.start_x = start_x;
    }

    public int getStart_y() {
        return start_y;
    }

    public void setStart_y(int start_y) {
        this.start_y = start_y;
    }

    public String getColour() {
        return colour;
    }

    public void setColour(String colour) {
        this.colour = colour;
    }

    public int getSpeed() {
        return speed;
    }

    public void setSpeed(int speed) {
        this.speed = speed;
    }

    public List<Pair<DIRECTION, Integer>> getMoveList() {
        return moveList;
    }

    public Pair<DIRECTION, Integer> getAMove(int i) {
        return moveList.get(i);
    }

    public void setMoveList(Pair<DIRECTION, Integer> move) {
        moveList.add(move);
    }

    @Override
    public <C, T> T accept(C context, PacManVisitor<C, T> v) {
        return v.visit(context, this);
    }

}
