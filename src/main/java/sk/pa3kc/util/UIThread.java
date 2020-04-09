package sk.pa3kc.util;

import org.lwjgl.opengl.GL;

import static org.lwjgl.glfw.GLFW.*;
import static org.lwjgl.opengl.GL11.*;
import static org.lwjgl.system.MemoryUtil.NULL;

import org.lwjgl.glfw.GLFW;

public class UIThread implements Runnable {
    private enum ThreadState {
        STARTING,
        RUNNING,
        PAUSING,
        PAUSED,
        STOPPING,
        STOPPED
    }

    private final Thread thread;
    private final long windowId;

    private ThreadState state = ThreadState.STOPPED;

    public UIThread(final long windowId) {
        this.thread = new Thread(this);
        this.windowId = windowId;
    }

    public void start() {
        this.state = ThreadState.STARTING;
        this.thread.start();
    }

    public void stop() {
        this.state = ThreadState.STOPPING;
    }

    public void pause() {
        this.state = ThreadState.PAUSING;
    }

    @Override
    public void run() {
        glfwMakeContextCurrent(this.windowId);

        glfwSwapInterval(0);

        GL.createCapabilities();

        glEnable(GL_TEXTURE_2D);

        while (!glfwWindowShouldClose(this.windowId) && this.state != ThreadState.STOPPING) {
            glClear(GL_COLOR_BUFFER_BIT | GL_DEPTH_BUFFER_BIT);

            glfwPollEvents();

            // textures[this.textureIndex].bind();

            // model.render();

            glfwSwapBuffers(this.windowId);
        }

        GLFW.glfwMakeContextCurrent(NULL);
    }
}
