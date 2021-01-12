package PMLGraphics.Util;

import org.lwjgl.BufferUtils;

import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.*;
import java.nio.file.Paths;

import static org.lwjgl.opengl.GL11.*;
import static org.lwjgl.stb.STBImage.*;
import static org.lwjgl.opengl.GL20.*;

public class RenderProgram {
    private int vertex, fragment, program, texture;
    private static final String shaderDir = "src/main/java/PMLGraphics/Shaders/";
    private static final String assetDir = "src/main/java/PMLGraphics/Assets/";
    private static final String vertexShaderFileExtension = ".vs.glsl";
    private static final String fragmentShaderFileExtension = ".fs.glsl";
    private static final String pngExtension = ".png";

    public boolean loadShadersFromFile(String vertexShaderFileName, String fragmentShaderFileName) {
        if (!loadFiles(vertexShaderFileName, fragmentShaderFileName)) {
            return false;
        }
        if (!(compileShader(vertex) && compileShader(fragment))) {
            return false;
        }
        return link();
    }

    private boolean loadFiles(String vertexShaderFileName, String fragmentShaderFileName) {
        String vertexSource;
        String fragmentSource;
        try {
            Path vertexShaderPath = Paths.get(shaderDir + vertexShaderFileName + vertexShaderFileExtension);
            vertexSource = new String(Files.readAllBytes(vertexShaderPath));
            Path fragmentShaderPath = Paths.get(shaderDir + fragmentShaderFileName + fragmentShaderFileExtension);
            fragmentSource = new String(Files.readAllBytes(fragmentShaderPath));
        } catch (IOException e) {
            return false;
        }
        vertex = glCreateShader(GL_VERTEX_SHADER);
        glShaderSource(vertex, vertexSource);
        fragment = glCreateShader(GL_FRAGMENT_SHADER);
        glShaderSource(fragment, fragmentSource);
        return true;
    }

    private boolean link() {
        program = glCreateProgram();
        glAttachShader(program, vertex);
        glAttachShader(program, fragment);
        glLinkProgram(program);
        IntBuffer success = ByteBuffer.allocateDirect(4).asIntBuffer();
        glGetProgramiv(program, GL_LINK_STATUS, success);
        if (success.get() == GL_FALSE) {
            System.out.println(glGetProgramInfoLog(program));
            return false;
        }
        glDetachShader(program, vertex);
        glDetachShader(program, fragment);
        glDeleteShader(vertex);
        glDeleteShader(fragment);
        return true;
    }

    private boolean compileShader(int shader) {
        glCompileShader(shader);
        IntBuffer success = ByteBuffer.allocateDirect(4).asIntBuffer();
        glGetShaderiv(shader, GL_COMPILE_STATUS, success);
        if (success.get() == GL_FALSE) {
            System.out.println(glGetShaderInfoLog(shader));
            return false;
        }
        return true;
    }

    public boolean loadTextureFile(String file) {
        String fileName = assetDir + file + pngExtension;
        IntBuffer colorChannels = BufferUtils.createIntBuffer(1);
        IntBuffer width = BufferUtils.createIntBuffer(1);
        IntBuffer height = BufferUtils.createIntBuffer(1);
        ByteBuffer buffer = stbi_load(fileName, width, height, colorChannels, 0);
        texture = glGenTextures();
        glBindTexture(GL_TEXTURE_2D, texture);
        glTexImage2D(GL_TEXTURE_2D, 0, GL_RGBA, width.get(), height.get(), 0, GL_RGBA, GL_UNSIGNED_BYTE, buffer);
        glTexParameteri(GL_TEXTURE_2D, GL_TEXTURE_WRAP_S, GL_CLAMP_TO_EDGE);
        glTexParameteri(GL_TEXTURE_2D, GL_TEXTURE_WRAP_T, GL_CLAMP_TO_EDGE);
        glTexParameteri(GL_TEXTURE_2D, GL_TEXTURE_MAG_FILTER, GL_NEAREST);
        glTexParameteri(GL_TEXTURE_2D, GL_TEXTURE_MIN_FILTER, GL_NEAREST);
        stbi_image_free(buffer);
        if (GLError.check()) {
            System.err.print("OpenGL Error: " + GLError.lastErrorMessage + "\n");
        }
        return !GLError.check();
    }

    public int getProgram() {
        return program;
    }

    public int getTexture() {
        return texture;
    }

    void release() {
        glDeleteProgram(program);
        glDeleteShader(vertex);
        glDeleteShader(fragment);
    }
}
