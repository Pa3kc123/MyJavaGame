package sk.pa3kc.ui.calls;

import org.lwjgl.glfw.GLFW;
import org.lwjgl.glfw.GLFWKeyCallback;

public class KeyCallback extends GLFWKeyCallback {
    private boolean[] keys = new boolean[512];

    @Override
    public void invoke(long window, int key, int scancode, int action, int mods) {
        this.keys[key] = action != GLFW.GLFW_RELEASE;
    }

    public boolean isKeyPressed(int keyCode) {
        return this.keys[keyCode];
    }
}
