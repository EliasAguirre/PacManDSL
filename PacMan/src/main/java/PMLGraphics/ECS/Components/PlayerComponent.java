package PMLGraphics.ECS.Components;

import PMLGraphics.ECS.Systems.KeyboardService;

import java.util.HashMap;
import java.util.UUID;

public class PlayerComponent implements Component {
    public final static UUID id = UUID.randomUUID();
    private final HashMap<KeyboardService.Key, Boolean> blockedDirection;
    public PlayerComponent() {
        blockedDirection = new HashMap<>();
        reset();
    }
    public void reset() {
        for (KeyboardService.Key key: KeyboardService.Key.values()) {
            blockedDirection.put(key, false);
        }
    }
    public void blockDirection(KeyboardService.Key key) {
        blockedDirection.put(key, true);
    }
    public boolean isBlocked(KeyboardService.Key key) {
        return blockedDirection.get(key);
    }
    @Override
    public UUID getID() {
        return id;
    }
}
