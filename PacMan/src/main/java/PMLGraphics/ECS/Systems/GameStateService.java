package PMLGraphics.ECS.Systems;

public class GameStateService {
    public enum GameState {
        ACTIVE,
        PAUSED,
        WON
    }
    private static GameStateService instance = null;
    private GameState currentState;

    private GameStateService() {
        currentState = GameState.ACTIVE;
    }

    public static GameStateService getInstance() {
        if (instance == null) {
            instance = new GameStateService();
        }
        return instance;
    }

    public GameState getCurrentState() {
        return currentState;
    }

    public void changeState(GameState target) {
        currentState = target;
    }
}
