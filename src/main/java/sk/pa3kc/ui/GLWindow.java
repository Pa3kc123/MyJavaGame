package sk.pa3kc.ui;

import org.lwjgl.opengl.GL;
import org.lwjgl.opengl.GL11;
import org.lwjgl.opengl.GLCapabilities;
import org.lwjgl.glfw.GLFWErrorCallback;
import org.lwjgl.glfw.GLFWKeyCallback;
import org.lwjgl.glfw.GLFWKeyCallbackI;
import org.lwjgl.glfw.GLFWWindowCloseCallback;
import org.lwjgl.glfw.GLFWWindowCloseCallbackI;

import sk.pa3kc.util.UIThread;

import static org.lwjgl.glfw.GLFW.*;
import static org.lwjgl.opengl.GL11.*;
import static org.lwjgl.system.MemoryUtil.NULL;

public class GLWindow implements AutoCloseable {
    private final GLCapabilities capabilities;
    private final GLFWErrorCallback errCallback;
    private final GLFWWindowCloseCallbackI windowCloseCallback;
    private final long windowId;

    private GLFWKeyCallbackI keyCallback;
    public UIThread uiThread;

    public GLWindow(int width, int height, CharSequence title) {
        // Create callbacks
        this.errCallback = GLFWErrorCallback.createPrint(System.err);
        this.windowCloseCallback = GLFWWindowCloseCallback.create((long window) -> this.close());

        // Initialize GLFW
        if (glfwInit() == false) {
            glfwTerminate();
            throw new IllegalStateException("Unable to initialize GLFW");
        }

        // Setup window properties
        glfwDefaultWindowHints();
        glfwWindowHint(GLFW_RESIZABLE, GLFW_FALSE);
        glfwWindowHint(GLFW_VISIBLE, GLFW_FALSE);
        glfwWindowHint(GLFW_DECORATED, GLFW_FALSE);

        // Create OpenGL window
        this.windowId = glfwCreateWindow(width, height, title, NULL, NULL);
        if (this.windowId == NULL) {
            glfwTerminate();
            throw new IllegalStateException("Unable to create GLFW window");
        }

        // Setup window position to upper left corner (Windowed fullscreen)
        glfwSetWindowPos(this.windowId, 0, 0);

        // Setup callbacks
        glfwSetErrorCallback(this.errCallback);
        glfwSetWindowCloseCallback(this.windowId, this.windowCloseCallback);

        glfwMakeContextCurrent(this.windowId);

        glfwSwapInterval(0);

        this.capabilities = GL.createCapabilities();

        glEnable(GL_TEXTURE_2D);
        glEnable(GL_CULL_FACE);
        glCullFace(GL_BACK);

        glfwMakeContextCurrent(NULL);

        // Create UIThread for refreshing game logic and rendering loop
        this.uiThread = new UIThread(this.windowId, this.capabilities);
    }

    public long getWindowId() {
        return this.windowId;
    }

    public void setBackgroundColor(float r, float g, float b) {
        this.setBackgroundColor(r, g, b, 1f);
    }
    public void setBackgroundColor(float r, float g, float b, float a) {
        GL11.glClearColor(r, g, b, a);
    }

    public void setKeyCallback(GLFWKeyCallbackI keyCallback) {
        this.keyCallback = keyCallback;
        glfwSetKeyCallback(this.windowId, this.keyCallback);
    }

    public void show() {
        glfwShowWindow(this.windowId);
        this.uiThread.start();
    }

    public void hide() {
        glfwHideWindow(this.windowId);
    }

    @Override
    public void close() {
        this.uiThread.stop();
        GLFWErrorCallback.free(this.errCallback.address());
        GLFWWindowCloseCallback.free(this.windowCloseCallback.address());
        if (this.keyCallback != null) {
            GLFWKeyCallback.free(this.keyCallback.address());
        }
        glfwDestroyWindow(this.windowId);
        glfwTerminate();
    }
}
