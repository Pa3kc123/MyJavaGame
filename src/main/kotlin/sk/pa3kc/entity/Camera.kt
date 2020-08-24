package sk.pa3kc.entity

import org.lwjgl.glfw.GLFW
import sk.pa3kc.KEYBOARD

import sk.pa3kc.mylibrary.matrix.pojo.Vector3f

data class Camera(
    var position: Vector3f = Vector3f(0f, 0f, 0f),
    var pitch: Float = 0f,
    var yaw: Float = 0f,
    var roll: Float = 0f
)

fun Camera.move() {
    if (KEYBOARD.isKeyPressed(GLFW.GLFW_KEY_W)) {
        this.position.z -= 0.02f
    }
    if (KEYBOARD.isKeyPressed(GLFW.GLFW_KEY_A)) {
        this.position.x -= 0.02f
    }
    if (KEYBOARD.isKeyPressed(GLFW.GLFW_KEY_S)) {
        this.position.z += 0.02f
    }
    if (KEYBOARD.isKeyPressed(GLFW.GLFW_KEY_D)) {
        this.position.x += 0.02f
    }
    if (KEYBOARD.isKeyPressed(GLFW.GLFW_KEY_SPACE)) {
        this.position.y += 0.02f
    }
    if (KEYBOARD.isKeyPressed(GLFW.GLFW_KEY_COMMA)) {
        this.position.y -= 0.02f
    }
}

fun Camera.rotate() {
    if (KEYBOARD.isKeyPressed(GLFW.GLFW_KEY_Q)) {
        this.roll += 0.02f
    }
    if (KEYBOARD.isKeyPressed(GLFW.GLFW_KEY_E)) {
        this.roll -= 0.02f
    }
}
