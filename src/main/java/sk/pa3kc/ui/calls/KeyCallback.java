package sk.pa3kc.ui.calls;

import org.lwjgl.glfw.GLFW;
import org.lwjgl.glfw.GLFWKeyCallback;

public class KeyCallback extends GLFWKeyCallback {
    private static boolean[] keys = new boolean[65536];

    @Override
    public void invoke(long window, int key, int scancode, int action, int mods) {
        KeyCallback.keys[key] = action != GLFW.GLFW_RELEASE;
    }

    public boolean isKeyPressed(int keyCode) {
        return KeyCallback.keys[keyCode];
    }
}
