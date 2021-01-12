package PMLGraphics.ECS.Systems;

import PMLGraphics.ECS.Components.*;
import PMLGraphics.ECS.Entity;
import PMLGraphics.ECS.EntityManager;
import PMLGraphics.Game.World;
import PMLGraphics.Util.GLError;
import PMLGraphics.Util.RenderProgram;
import PMLGraphics.Util.RenderingUtil;
import org.lwjgl.BufferUtils;

import static org.lwjgl.opengl.GL11.*;
import static org.lwjgl.opengl.GL15.*;
import static org.lwjgl.opengl.GL20.*;
import static org.lwjgl.opengl.GL30.*;
import java.nio.FloatBuffer;
import java.nio.IntBuffer;
import java.util.ArrayList;
import java.util.List;
import java.util.UUID;

public class RenderingSystem extends EntitySystem {
    private final ArrayList<Entity> renderableEntityQuery;
    private final UUID renderableComponentId = RenderableComponent.id;
    private final RenderProgram renderProgram;
    private final RenderProgram wallRenderProgram;
    private final RenderProgram enemyRenderProgram;
    private final RenderProgram goalRenderProgram;
    private final RenderProgram winScreenRenderProgram;
    private final float[] projectionMatrix;
    private final World world;
    int vao, ibo, vbo, cbo, tbo;

    public RenderingSystem(EntityManager entityManager, float[] projectionMatrix, World world) {
        this.entityManager = entityManager;
        this.projectionMatrix = projectionMatrix;
        this.renderableEntityQuery = new ArrayList<>();
        this.renderProgram = new RenderProgram();
        this.wallRenderProgram = new RenderProgram();
        this.enemyRenderProgram = new RenderProgram();
        this.goalRenderProgram = new RenderProgram();
        this.winScreenRenderProgram = new RenderProgram();
        this.entityManager.subscribe(this);
        this.world = world;
        setUp();
    }

    @Override
    public void update(float sec) {
        render();
        if (GLError.check()) {
            System.err.print("OpenGL Error: " + GLError.lastErrorMessage + "\n");
        }
    }

    public void render() {
        glClear(GL_COLOR_BUFFER_BIT | GL_DEPTH_BUFFER_BIT);
        boolean renderFinalScreen = GameStateService.getInstance().getCurrentState() == GameStateService.GameState.WON;
        if (renderFinalScreen) {
            renderFinalScreen();
        } else {
            for (Entity entity: renderableEntityQuery) {
                render(entity);
            }
        }
    }

    public void updateEntityColor(RenderableAssetType type, String color) {
        List<Float> rgb = RenderingUtil.getHexFromString(color);
        Component component;
        if (type == RenderableAssetType.WALL) {
            component = new WallComponent();
        } else {
            component = new PlayerComponent();
        }
        for (Entity entity: renderableEntityQuery) {
            if (entity.hasComponent(component.getID())) {
                entity.addComponent(new ColorComponent(rgb.get(0), rgb.get(1), rgb.get(2)));
            }
        }
    }

    private void render(Entity entity) {
        PositionComponent positionComponent = (PositionComponent) entity.getComponent(PositionComponent.id);
        ColorComponent colorComponent = (ColorComponent) entity.getComponent(ColorComponent.id);
        RenderableComponent renderableComponent = (RenderableComponent) entity.getComponent(RenderableComponent.id);
        RenderProgram programInUse = renderProgram;
        switch (renderableComponent.assetType) {
            case WALL -> programInUse = wallRenderProgram;
            case ENEMY -> programInUse = enemyRenderProgram;
            case GOAL -> programInUse = goalRenderProgram;
        }
        glUseProgram(programInUse.getProgram());
        glBindVertexArray(vao);
        glEnableVertexAttribArray(0);

        float[] transformMatrix = new float[]{0.5f,                   0,                   0,
                                               0,                   0.5f,                   0,
                                               positionComponent.x, positionComponent.y, 1 };
        FloatBuffer transformBuffer = BufferUtils.createFloatBuffer(transformMatrix.length);
        transformBuffer.put(transformMatrix);
        transformBuffer.flip();
        int transformLocation = glGetUniformLocation(renderProgram.getProgram(), "transform");
        glUniformMatrix3fv(transformLocation, false, transformBuffer);

        FloatBuffer projectionBuffer = BufferUtils.createFloatBuffer(projectionMatrix.length);
        projectionBuffer.put(projectionMatrix);
        projectionBuffer.flip();
        int projectionLocation;
        projectionLocation = glGetUniformLocation(programInUse.getProgram(), "projection");
        glUniformMatrix3fv(projectionLocation, false, projectionBuffer);

        if (programInUse != renderProgram) {
            float[] textureVertices = {
                    0.0f, 0.0f,
                    1.0f, 0.0f,
                    1.0f, 1.0f,
                    0.0f, 1.0f
            };
            FloatBuffer textureBuffer = BufferUtils.createFloatBuffer(textureVertices.length);
            textureBuffer.put(textureVertices);
            textureBuffer.flip();
            glActiveTexture(GL_TEXTURE0);
            glBindTexture(GL_TEXTURE_2D, programInUse.getTexture());
            glBindBuffer(GL_ARRAY_BUFFER, tbo);
            glBufferData(GL_ARRAY_BUFFER, textureBuffer, GL_STATIC_DRAW);
            glEnableVertexAttribArray(1);
            glVertexAttribPointer(1, 2, GL_FLOAT, false, 0, 0);
        }

        float[] color = new float[]{ colorComponent.r, colorComponent.g, colorComponent.b };
        FloatBuffer colorBuffer = BufferUtils.createFloatBuffer(color.length);
        colorBuffer.put(color);
        colorBuffer.flip();
        int colorLocation = glGetUniformLocation(programInUse.getProgram(), "color");
        glUniform3fv(colorLocation, colorBuffer);

        glDrawElements(GL_TRIANGLES, 6, GL_UNSIGNED_INT, 0);
        glDisableVertexAttribArray(0);
        glBindVertexArray(0);
        if (GLError.check()) {
            System.err.print("OpenGL Error: " + GLError.lastErrorMessage + "\n");
        }
    }

    private void renderFinalScreen() {
        glUseProgram(winScreenRenderProgram.getProgram());
        glBindVertexArray(vao);
        glEnableVertexAttribArray(0);

        float[] transformMatrix = new float[]{5f,                   0,                   0,
                                              0,                   5f,                   0,
                                              world.getWidth()/2f, world.getHeight()/2f,   1 };
        FloatBuffer transformBuffer = BufferUtils.createFloatBuffer(transformMatrix.length);
        transformBuffer.put(transformMatrix);
        transformBuffer.flip();
        int transformLocation = glGetUniformLocation(winScreenRenderProgram.getProgram(), "transform");
        glUniformMatrix3fv(transformLocation, false, transformBuffer);

        FloatBuffer projectionBuffer = BufferUtils.createFloatBuffer(projectionMatrix.length);
        projectionBuffer.put(projectionMatrix);
        projectionBuffer.flip();
        int projectionLocation;
        projectionLocation = glGetUniformLocation(winScreenRenderProgram.getProgram(), "projection");
        glUniformMatrix3fv(projectionLocation, false, projectionBuffer);

        float[] textureVertices = {
                0.0f, 0.0f,
                1.0f, 0.0f,
                1.0f, 1.0f,
                0.0f, 1.0f
        };
        FloatBuffer textureBuffer = BufferUtils.createFloatBuffer(textureVertices.length);
        textureBuffer.put(textureVertices);
        textureBuffer.flip();
        glActiveTexture(GL_TEXTURE0);
        glBindTexture(GL_TEXTURE_2D, winScreenRenderProgram.getTexture());
        glBindBuffer(GL_ARRAY_BUFFER, tbo);
        glBufferData(GL_ARRAY_BUFFER, textureBuffer, GL_STATIC_DRAW);
        glEnableVertexAttribArray(1);
        glVertexAttribPointer(1, 2, GL_FLOAT, false, 0, 0);

        float[] color = new float[]{ 1, 1, 1 };
        FloatBuffer colorBuffer = BufferUtils.createFloatBuffer(color.length);
        colorBuffer.put(color);
        colorBuffer.flip();
        int colorLocation = glGetUniformLocation(winScreenRenderProgram.getProgram(), "color");
        glUniform3fv(colorLocation, colorBuffer);

        glDrawElements(GL_TRIANGLES, 6, GL_UNSIGNED_INT, 0);
        glDisableVertexAttribArray(0);
        glBindVertexArray(0);
        if (GLError.check()) {
            System.err.print("OpenGL Error: " + GLError.lastErrorMessage + "\n");
        }
    }

    public void setUp() {
        // VAO
        vao = glGenVertexArrays();
        glBindVertexArray(vao);

        //VBO
        float[] vertices = {
                -1.0f,  1.0f, 0.0f,
                 1.0f,  1.0f, 0.0f,
                 1.0f, -1.0f, 0.0f,
                -1.0f, -1.0f, 0.0f
        };
        FloatBuffer vboBuffer = BufferUtils.createFloatBuffer(vertices.length);
        vboBuffer.put(vertices);
        vboBuffer.flip();
        vbo = glGenBuffers();
        glBindBuffer(GL_ARRAY_BUFFER, vbo);
        glBufferData(GL_ARRAY_BUFFER, vboBuffer, GL_STATIC_DRAW);
        glVertexAttribPointer(0, 3, GL_FLOAT, false, 3 * 4, 0);
        glBindBuffer(GL_ARRAY_BUFFER, 0);

        // IBO
        int[] indices = { 0, 1, 2, 2, 3, 0 };
        IntBuffer iboBuffer = BufferUtils.createIntBuffer(indices.length);
        iboBuffer.put(indices);
        iboBuffer.flip();
        ibo = glGenBuffers();
        glBindBuffer(GL_ELEMENT_ARRAY_BUFFER, ibo);
        glBufferData(GL_ELEMENT_ARRAY_BUFFER, iboBuffer, GL_STATIC_DRAW);

        glBindVertexArray(0);
        cbo = glGenBuffers();
        tbo = glGenBuffers();
        loadAllProgramShadersAndTextures();
    }

    private void loadAllProgramShadersAndTextures() {
        if (!renderProgram.loadShadersFromFile("rectangle", "rectangle")) {
            throw new RuntimeException("LOADING SHADERS FAILED");
        }
        if (!wallRenderProgram.loadShadersFromFile("textured", "textured")) {
            throw new RuntimeException("LOADING TEXTURED SHADERS FAILED");
        }
        if (!wallRenderProgram.loadTextureFile("wall_texture")) {
            throw new RuntimeException("LOADING WALL TEXTURE FILE FAILED");
        }
        if (!enemyRenderProgram.loadShadersFromFile("textured", "textured")) {
            throw new RuntimeException("LOADING TEXTURED SHADERS FAILED");
        }
        if (!enemyRenderProgram.loadTextureFile("pacman_ghost")) {
            throw new RuntimeException("LOADING ENEMY TEXTURE FILE FAILED");
        }
        if (!goalRenderProgram.loadShadersFromFile("textured", "textured")) {
            throw new RuntimeException("LOADING TEXTURED SHADERS FAILED");
        }
        if (!goalRenderProgram.loadTextureFile("cherry")) {
            throw new RuntimeException("LOADING GOAL TEXTURE FILE FAILED");
        }
        if (!winScreenRenderProgram.loadShadersFromFile("textured", "textured")) {
            throw new RuntimeException("LOADING TEXTURED SHADERS FAILED");
        }
        if (!winScreenRenderProgram.loadTextureFile("winning_screen")) {
            throw new RuntimeException("LOADING WIN SCREEN TEXTURE FILE FAILED");
        }
    }

    @Override
    public void updateQueries(Entity e, Component component, boolean added) {
        if (!renderableEntityQuery.contains(e)) {
            if (added && component.getID() == renderableComponentId) {
                renderableEntityQuery.add(e);
            }
        } else if (!added) {
            renderableEntityQuery.remove(e);
        }
    }
}
