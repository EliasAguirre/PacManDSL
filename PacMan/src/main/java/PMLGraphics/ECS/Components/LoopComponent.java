package PMLGraphics.ECS.Components;

import PMLGraphics.Game.World;
import PacManDSL.libs.Pair;

import javax.swing.text.Position;
import java.util.ArrayList;
import java.util.UUID;

public class LoopComponent implements Component {
    public static final UUID id = UUID.randomUUID();
    private ArrayList<Pair<Integer, Integer>> loop = new ArrayList<>();
    private int currentLoopStepIndex;
    float remainingX, remainingY;

    public LoopComponent() {
        currentLoopStepIndex = 0;
    }

    public LoopComponent(ArrayList<Pair<Integer, Integer>> loop) {
        currentLoopStepIndex = 0;
        this.loop = loop;
    }

    public void addLoopStep(Pair<Integer, Integer> step) {
        this.loop.add(step);
    }

    public void reset() {
        this.loop.clear();
    }

    public PositionComponent move(PositionComponent currentPosition, SpeedComponent speed) {
        if (currentLoopStepIndex == loop.size()) {
            currentLoopStepIndex = 0;
        }
        Pair<Integer, Integer> step = loop.get(currentLoopStepIndex);
        int dx = step.getKey();
        int dy = step.getValue();
        if (remainingX == 0) {
            remainingX = Math.abs(dx);
        }
        if (remainingY == 0) {
            remainingY = Math.abs(dy);
        }
        float x = currentPosition.x;
        float y = currentPosition.y;
        float sp = speed.speed;
        float newX = x;
        float newY = y;
        if (dx != 0) {
            if (sp > remainingX) {
                newX = (dx > 0) ? x + remainingX : x - remainingX;
                remainingX = 0;
                currentLoopStepIndex++;
            } else {
                newX = (dx > 0) ? x + sp : x - sp;
                remainingX -= sp;
            }
        }
        if (dy != 0) {
            if (sp > remainingY) {
                newY = (dy > 0) ? y + remainingY : y - remainingY;
                remainingY = 0;
                currentLoopStepIndex++;
            } else {
                newY = (dy > 0) ? y + sp : y - sp;
                remainingY -= sp;
            }
        }
        return new PositionComponent(newX, newY);
    }
    @Override
    public UUID getID() {
        return id;
    }
}
