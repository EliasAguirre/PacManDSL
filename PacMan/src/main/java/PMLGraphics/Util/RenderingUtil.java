package PMLGraphics.Util;

import java.util.ArrayList;
import java.util.List;

public class RenderingUtil {
    public static List<Float> getHexFromString(String color) {
        List<Float> hexColors = new ArrayList<>();
        for (int i = 0; i < 6; i += 2) {
            int c = Integer.parseInt(color.substring(i, i + 2), 16);
            hexColors.add((float) c/255);
        }
        return hexColors;
    }
}
