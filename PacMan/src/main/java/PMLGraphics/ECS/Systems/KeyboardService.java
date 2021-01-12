package PMLGraphics.ECS.Systems;

import java.util.HashMap;

public class KeyboardService {
    private final HashMap<Key, Boolean> keyPressedStates;
    private static KeyboardService instance = null;
    public enum Key {
        UP,
        RIGHT,
        DOWN,
        LEFT
    }

    private KeyboardService() {
        this.keyPressedStates = new HashMap<>();
        reset();
    }

    public void pressKey(Key key) {
        keyPressedStates.put(key, true);
    }

    public void unPressKey(Key key) {
        keyPressedStates.put(key, false);
    }

    public boolean isPressed(Key key) {
        return keyPressedStates.get(key);
    }

    public static KeyboardService getInstance() {
        if (instance == null) {
            instance = new KeyboardService();
        }
        return instance;
    }

    public void reset() {
        for (Key k: Key.values()) {
            keyPressedStates.put(k, false);
        }
    }
}
