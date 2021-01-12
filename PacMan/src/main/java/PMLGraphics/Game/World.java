package PMLGraphics.Game;

import PMLGraphics.ECS.Components.*;
import PMLGraphics.ECS.Entity;
import PMLGraphics.ECS.EntityManager;
import PMLGraphics.ECS.Systems.*;
import PMLGraphics.Spawners.EnemySpawner;
import PMLGraphics.Spawners.PlayerSpawner;
import PMLGraphics.Spawners.WallSpawner;
import PMLGraphics.Util.RenderingUtil;
import PacManDSL.ast.Enemy;
import PacManDSL.libs.Pair;
import org.lwjgl.glfw.*;
import org.lwjgl.opengl.*;
import org.lwjgl.system.Callback;

import java.nio.ByteBuffer;
import java.nio.IntBuffer;
import java.util.ArrayList;
import java.util.List;

import static org.lwjgl.glfw.Callbacks.*;
import static org.lwjgl.glfw.GLFW.*;
import static org.lwjgl.opengl.GL11.*;

import static org.lwjgl.system.MemoryUtil.*;

public class World {
    public final static int TILE_SIZE = 50;

    private long window;
    private final EntityManager entityManager;
    private final PlayerSpawner playerSpawner;
    private final EnemySpawner enemySpawner;
    private final WallSpawner wallSpawner;
    private final ArrayList<EntitySystem> registeredSystems;
    private final RenderingSystem renderingSystem;
    private float[] worldProjection;
    private final int height;
    private final int width;

    public World(int width, int height) {
        this.width = width;
        this.height = height;
        initRender(width, height);
        entityManager = new EntityManager();
        playerSpawner = new PlayerSpawner();
        enemySpawner = new EnemySpawner();
        wallSpawner = new WallSpawner();
        registeredSystems = new ArrayList<>();
        renderingSystem = new RenderingSystem(entityManager, worldProjection, this);
        registerSystem(renderingSystem);
        registerSystem(new PlayerControlSystem(entityManager, this));
        registerSystem(new CollisionSystem(entityManager));
        registerSystem(new EnemyMovementSystem(entityManager));
        playerSpawner.createPlayer(entityManager, 0.5f, 0.5f);
        makeEndGoal();
        GameStateService.getInstance().changeState(GameStateService.GameState.ACTIVE);
    }

    public void addWall(float startX, float startY, int width, int height) {
        this.wallSpawner.createWall(this.entityManager, startX, startY, width, height);
    }

    public void addEnemy(float x, float y, String color, int speed, List<Pair<Enemy.DIRECTION, Integer>> moveList) {
        this.enemySpawner.createEnemy(this.entityManager, x, y, color, speed, moveList);
    }

    public void updateAssetColor(RenderableAssetType type, String color) {
        this.renderingSystem.updateEntityColor(type, color);
    }

    public void updateMapColor(String color) {
        List<Float> rgb = RenderingUtil.getHexFromString(color);
        glClearColor(rgb.get(0), rgb.get(1), rgb.get(2), 1.0f);
    }

    public int getWidth() {
        return this.width;
    }

    public int getHeight() {
        return this.height;
    }

    public void destroy() {
        glfwFreeCallbacks(window);
        glfwDestroyWindow(window);
        glfwTerminate();
        glfwSetErrorCallback(null).free();
    }

    public void update(float sec) {
        boolean paused = GameStateService.getInstance().getCurrentState() == GameStateService.GameState.PAUSED;
        if (!paused) {
            for (EntitySystem system: registeredSystems) {
                system.update(sec);
            }
            glfwSwapBuffers(window);
        }
        glfwPollEvents();
    }

    public void registerSystem(EntitySystem system) {
        registeredSystems.add(system);
    }

    public void initRender(int width, int height) {
        GLFWErrorCallback.createPrint(System.err).set();
        if ( !glfwInit() )
            throw new IllegalStateException("Unable to initialize GLFW");
        glfwWindowHint(GLFW_CONTEXT_VERSION_MAJOR, 3);
        glfwWindowHint(GLFW_CONTEXT_VERSION_MINOR, 3);
        glfwWindowHint(GLFW_OPENGL_PROFILE, GLFW_OPENGL_CORE_PROFILE);
        glfwWindowHint(GLFW_OPENGL_DEBUG_CONTEXT, 1);
        String operatingSystem = System.getProperty("os.name").toLowerCase();
        boolean isMac = operatingSystem.contains("mac");
        if (isMac)
            glfwWindowHint(GLFW_OPENGL_FORWARD_COMPAT, GL_TRUE);
        glfwWindowHint(GLFW_VISIBLE, GLFW_FALSE);
        glfwWindowHint(GLFW_RESIZABLE, GLFW_TRUE);
        int absWidth = width * TILE_SIZE;
        int absHeight = height * TILE_SIZE;
        window = glfwCreateWindow(absWidth, absHeight, "PacMan Maze Creator", NULL, NULL);
        if ( window == NULL )
            throw new RuntimeException("Failed to create the GLFW window");
        glfwSetKeyCallback(window, this::pressedKey);
        glfwMakeContextCurrent(window);
        glfwSwapInterval(1);
        glfwShowWindow(window);
        GL.createCapabilities();
        Callback debugProc = GLUtil.setupDebugMessageCallback();
        glViewport(0, 0, absWidth, absHeight);
        glClearColor(1.0f, 1.0f, 1.0f, 1.0f);
        initWorldProjection((float) width);
    }

    private void initWorldProjection(float screenWidth) {
        IntBuffer frameBufferWidth = ByteBuffer.allocateDirect(4).asIntBuffer();
        IntBuffer frameBufferHeight = ByteBuffer.allocateDirect(4).asIntBuffer();
        glfwGetFramebufferSize(window, frameBufferWidth, frameBufferHeight);
        float bufferWidth = (float) frameBufferWidth.get();
        float bufferHeight = (float) frameBufferHeight.get();
        float screenScale = bufferWidth / screenWidth;
        float left = 0;
        float top = 0;
        float right = bufferWidth / screenScale;
        float bottom = bufferHeight / screenScale;
        float scaleX = 2 / (right - left);
        float scaleY = -2 / (top - bottom);
        float translateX = -(right + left) / (right - left);
        float translateY = (top + bottom) / (top - bottom);
        worldProjection = new float[]{ scaleX,     0,          0,
                                       0,          scaleY,     0,
                                       translateX, translateY, 1 };
    }

    public void pressedKey(long window, long key, long scancode, long action, long mods) {
        KeyboardService keyboardService = KeyboardService.getInstance();
        boolean won = GameStateService.getInstance().getCurrentState() == GameStateService.GameState.WON;
        if (won) {
            if (key == GLFW_KEY_R && action == GLFW_RELEASE) {
                GameStateService.getInstance().changeState(GameStateService.GameState.ACTIVE);
                playerSpawner.reset(0.5f, 0.5f);
            }
            if (key == GLFW_KEY_Q && action == GLFW_RELEASE) {
                glfwSetWindowShouldClose(window, true);
            }
        }
        if ( key == GLFW_KEY_ESCAPE && action == GLFW_RELEASE ) {
            GameStateService gameStateServiceInstance = GameStateService.getInstance();
            GameStateService.GameState current = gameStateServiceInstance.getCurrentState();
            if (current == GameStateService.GameState.PAUSED) {
                gameStateServiceInstance.changeState(GameStateService.GameState.ACTIVE);
            } else {
                gameStateServiceInstance.changeState(GameStateService.GameState.PAUSED);
            }
        }
        KeyboardService.Key targetKey = null;
        if ( key == GLFW_KEY_UP ) { targetKey = KeyboardService.Key.UP; }
        if ( key == GLFW_KEY_DOWN ) { targetKey = KeyboardService.Key.DOWN; }
        if ( key == GLFW_KEY_RIGHT ) { targetKey = KeyboardService.Key.RIGHT; }
        if ( key == GLFW_KEY_LEFT ) { targetKey = KeyboardService.Key.LEFT; }
        if ( targetKey != null ) {
            if (action == GLFW_PRESS) {
                keyboardService.pressKey(targetKey);
            } else if (action == GLFW_RELEASE) {
                keyboardService.unPressKey(targetKey);
            }
        }
    }

    private void makeEndGoal() {
        Entity endGoal = entityManager.createEntity();
        entityManager.addComponent(endGoal, new RenderableComponent(RenderableAssetType.GOAL));
        entityManager.addComponent(endGoal, new EndGoalComponent());
        entityManager.addComponent(endGoal, new ColorComponent(1, 1, 0));
        entityManager.addComponent(endGoal, new PositionComponent(0.5f, this.height - 0.5f));
        entityManager.addComponent(endGoal, new CollidableComponent());
        entityManager.addComponent(endGoal, new AABBComponent(0.5f, 0.5f));
    }

    public boolean isOver() {
        return glfwWindowShouldClose(window);
    }
}
