package sk.pa3kc.ui.call

import org.lwjgl.glfw.GLFW
import org.lwjgl.glfw.GLFWKeyCallback

class KeyCallback : GLFWKeyCallback() {
    private val keys = BooleanArray(512)

    override fun invoke(window: Long, key: Int, scancode: Int, action: Int, mods: Int) {
        this.keys[key] = action != GLFW.GLFW_RELEASE
    }

    fun isKeyPressed(keyCode: Int): Boolean = this.keys[keyCode]
}
