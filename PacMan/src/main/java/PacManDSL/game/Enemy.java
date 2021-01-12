package PacManDSL.game;

import PacManDSL.libs.Pair;

import java.util.ArrayList;
import java.util.List;

public class Enemy {
    private List<Pair<Enemy.DIRECTION, Integer>> moveList;
    private int start_x, start_y, colour, speed;
    public enum DIRECTION{
        UP,
        DOWN,
        LEFT,
        RIGHT
    }

    public Enemy(int start_x, int start_y, int colour, int speed, List moveList){
        this.start_x = start_x;
        this.start_y = start_y;
        this.colour = colour;
        this.speed = speed;
        this.moveList = moveList;
    }

}
