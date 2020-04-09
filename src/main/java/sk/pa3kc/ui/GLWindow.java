package sk.pa3kc.ui;

import org.lwjgl.opengl.GL;

import sk.pa3kc.util.UIThread;

import org.lwjgl.glfw.GLFWErrorCallback;
import org.lwjgl.glfw.GLFWKeyCallback;
import org.lwjgl.glfw.GLFWKeyCallbackI;

import static org.lwjgl.glfw.GLFW.*;
import static org.lwjgl.opengl.GL11.*;
import static org.lwjgl.system.MemoryUtil.NULL;

import java.io.IOException;

public class GLWindow implements AutoCloseable {
    private final GLFWErrorCallback errCallback;
    private final long windowId;

    private GLFWKeyCallbackI keyCallback;
    private UIThread uiThread;

    public GLWindow(int width, int height, CharSequence title) {
        this.errCallback = GLFWErrorCallback.createPrint(System.err).set();

        if (glfwInit() == false) {
            glfwTerminate();
            throw new IllegalStateException("Unable to initialize GLFW");
        }

        glfwDefaultWindowHints();
        glfwWindowHint(GLFW_RESIZABLE, GLFW_FALSE);
        glfwWindowHint(GLFW_VISIBLE, GLFW_FALSE);
        glfwWindowHint(GLFW_DECORATED, GLFW_FALSE);

        this.windowId = glfwCreateWindow(width, height, "My Game", NULL, NULL);
        if (this.windowId == NULL) {
            glfwTerminate();
            throw new IllegalStateException("Unable to create GLFW window");
        }

        glfwSetWindowPos(this.windowId, 0, 0);

        this.uiThread = new UIThread(this.windowId);
    }

    public void setBackgroundColor(float r, float g, float b) {
        this.setBackgroundColor(r, g, b, 1f);
    }
    public void setBackgroundColor(float r, float g, float b, float a) {
        glClearColor(r, g, b, a);
    }

    public void setKeyCallback(GLFWKeyCallbackI keyCallback) {
        this.keyCallback = keyCallback;
        glfwSetKeyCallback(this.windowId, this.keyCallback);
    }

    public void show() {
        glfwShowWindow(this.windowId);
    }

    public void hide() {
        glfwHideWindow(this.windowId);
    }

    @Override
    public void close() throws IOException {
        GLFWErrorCallback.free(this.errCallback.address());
        GLFWKeyCallback.free(this.keyCallback.address());
        glfwDestroyWindow(this.windowId);
        glfwTerminate();
    }
}
