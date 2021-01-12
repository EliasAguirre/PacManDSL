package PMLGraphics.Util;
import static org.lwjgl.opengl.GL11.*;
import static org.lwjgl.opengl.GL30.*;

public class GLError {
    public static String lastErrorMessage = "GL_NO_ERROR";
    public static boolean check() {
        int error = glGetError();
        if (error == GL_NO_ERROR) {
            lastErrorMessage = "GL_NO_ERROR";
            return false;
        } else {
            switch (error) {
                case GL_INVALID_ENUM: {
                    lastErrorMessage = "GL_INVALID_ENUM";
                }
                case GL_INVALID_VALUE: {
                    lastErrorMessage = "GL_INVALID_VALUE";
                }
                case GL_INVALID_OPERATION: {
                    lastErrorMessage = "GL_INVALID_OPERATION";
                }
                case GL_INVALID_FRAMEBUFFER_OPERATION: {
                    lastErrorMessage = "GL_INVALID_FRAMEBUFFER_OPERATION";
                }
                case GL_OUT_OF_MEMORY: {
                    lastErrorMessage = "GL_OUT_OF_MEMORY";
                }
                case GL_STACK_UNDERFLOW: {
                    lastErrorMessage = "GL_STACK_UNDERFLOW";
                }
                case GL_STACK_OVERFLOW: {
                    lastErrorMessage = "GL_STACK_OVERFLOW";
                }
            }
            return true;
        }
    }
}
