package PacManDSL.ast;

import PacManDSL.libs.Pair;

import java.util.HashMap;

public class PacManChecker implements PacManVisitor<HashMap<String, Map>, String> {

    @Override
    public String visit(HashMap<String, Map> context, Program p) {
        String result = "";
        for (Statement s: p.getStatements()){
            result += s.accept(context, this);
        }
        return result;
    }

    @Override
    public String visit(HashMap<String, Map> context, Map m) {
        String result = "";
        if (m.getHeight() <= 0 || m.getWidth() <= 0) {
            result += "\n ERROR: Can't initialize a Map with height or width <= 0";
        }
        if(m.getHeight() > 25 || m.getWidth() > 25) {
            result += "\n ERROR: map size cant exceed 25 width or height";
        }
        if (m.getHeight() != m.getWidth()) {
            result += "\n ERROR: map width and height must be the same";
        }
        context.put("map size", m);
        return result;
    }

    @Override
    public String visit(HashMap<String, Map> context, BuildWall b) {
        String result = "";
        if(context.containsKey("map size")){
            Map m = context.get("map size");

            if (((b.getStart_x() + b.getWidth() - 1) <= m.getWidth()) && ((b.getStart_y() + b.getHeight() - 1) <= m.getHeight())){
                    result += "";
            } else {
                result += "\n ERROR: the dimensions of one of your walls exceed the size of the map!";
            }


            if((b.getStart_x() == 1 && (b.getStart_y() + b.getHeight() - 1 == m.getHeight()))){
                result += "\n ERROR: wall can't be cover the top left corner of map, x value 1 and height of map (this is goal location)";
            }
        }

        if ((b.getStart_x() < 1 || b.getStart_y() < 1)) {
            result += "\n ERROR: wall can't start at an x or y coordinate less than 1";
        }

        if ((b.getStart_x() == 1 && b.getStart_y() == 1)) {
            result += "\n ERROR: can't initialize a wall at (1,1), this is where the player begins";
        }
        return result;
    }

    @Override
    public String visit(HashMap<String, Map> context, Enemy e) {
        String result = "";
        Map m = context.get("map size");
        if (!isValidHexLength(e.getColour())){
            result += "\n ERROR: incorrect size input for a hexadecimal color value! Please recheck your enemy colors";
        }
        if(!isValidHex(e.getColour())){
            result += "\n ERROR: your hex color value for an enemy contains illegal hex characters, accepted (0-9,a-f,A-F)";
        }
        if (e.getSpeed() < 0) {
            result += "\n ERROR: enemy speed can't be < 0";
        }
        int x = e.getStart_x();
        int y = e.getStart_y();
        int w = m.getWidth();
        int h = m.getHeight();

        if (x == 1 && y == 1){
            result += "\n ERROR: enemy starts in player position, invalid coordinate (1,1)";
        }

        if (x <= w && y <= h){
            for (int i = 0; i < e.getMoveList().size(); i++){
                Pair<Enemy.DIRECTION, Integer> temp = e.getMoveList().get(i);
                switch (temp.getKey()){
                    case UP:
                        if (y + temp.getValue() <= h){
                            result += "";
                            y = y + temp.getValue();
                        }
                        else {
                            result += "\n ERROR: Invalid Enemy Found, movement upwards from one of your enemies exceeds map height";
                        }
                        break;
                    case DOWN:
                        if (y - temp.getValue() > 0){
                            result += "";
                            y = y - temp.getValue();
                        }
                        else {
                            result += "\n ERROR: Invalid Enemy Found, movement downwards from one of your enemies exceeds map lower bound";
                        }
                        break;
                    case LEFT:
                        if (x - temp.getValue() > 0){
                            result += "";
                            x = x - temp.getValue();
                        }
                        else {
                            result += "\n ERROR: Invalid Enemy Found, movement left from one of your enemies exceeds maps left bound";
                        }
                        break;
                    case RIGHT:
                        if (x + temp.getValue() <= w){
                            result += "";
                            x = x + temp.getValue();
                        }
                        else {
                            result += "\n ERROR: Invalid Enemy Found, movement right from one of your enemies exceeds map right bound";
                        }
                        break;
                }
            }
        }
        if (e.getStart_x() == x && e.getStart_y() == y){
            result += "";
            return result;
        }
        result += "\n ERROR, invalid enemy found";
        return result;
    }

    @Override
    public String visit(HashMap<String, Map> context, WallColour w) {
        String result = "";
        if (!isValidHexLength(w.getColour())){
            result += "\n ERROR: incorrect size input for a hexadecimal color value! Please recheck your wall colors";
        }
        if(!isValidHex(w.getColour())){
            result += "\n ERROR: your hex color value for a wall contains illegal hex characters, accepted (0-9,a-f,A-F)";
        }
        return result;
    }

    @Override
    public String visit(HashMap<String, Map> context, CharacterColour c) {
        String result = "";
        if (!isValidHexLength(c.getColour())){
            result += "\n ERROR: incorrect size input for a hexadecimal color value! Please recheck your character color";
        }
        if(!isValidHex(c.getColour())){
            result += "\n ERROR: your hex color value for the character contains illegal hex characters, accepted (0-9,a-f,A-F)";
        }
        return result;
    }

    @Override
    public String visit(HashMap<String, Map> context, MapColour m) {
        String result = "";
        if (!isValidHexLength(m.getColour())){
            result += "\n ERROR: incorrect size input for a hexadecimal color value! Please recheck your map color";
        }
        if(!isValidHex(m.getColour())){
            result += "\n ERROR: your hex color value for the map contains illegal hex characters, accepted (0-9,a-f,A-F)";
        }
        return result;
    }

    public Boolean isValidHex(String s){
        Boolean valid = true;
        char[] c = s.toCharArray();
        for (char ch: c)
        {
            valid = ((ch >= 'a') && (ch <= 'f')) ||
                    ((ch >= 'A') && (ch <= 'F')) ||
                    ((ch >= '0') && (ch <= '9'));

            if (!valid)
            {
                break;
            }
        }

        return valid;
    }

    public Boolean isValidHexLength(String s){
        return (s.length() == 6);
    }
}
