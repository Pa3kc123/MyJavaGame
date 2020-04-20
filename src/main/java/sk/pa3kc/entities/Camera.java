package sk.pa3kc.entities;

import org.lwjgl.glfw.GLFW;

import sk.pa3kc.App;
import sk.pa3kc.pojo.matrix.Vector3f;

public class Camera {
    private Vector3f position = new Vector3f(0f, 0f, 0f);
    private float pitch = 0f;
    private float yaw = 0f;
    private float roll = 0f;

    public Vector3f getPosition() {
        return this.position;
    }
    public float getPitch() {
        return this.pitch;
    }
    public float getYaw() {
        return this.yaw;
    }
    public float getRoll() {
        return this.roll;
    }

    public void move() {
        if (App.KEYBOARD.isKeyPressed(GLFW.GLFW_KEY_W)) {
            this.position.z -= 0.02f;
        }
        if (App.KEYBOARD.isKeyPressed(GLFW.GLFW_KEY_A)) {
            this.position.x -= 0.02f;
        }
        if (App.KEYBOARD.isKeyPressed(GLFW.GLFW_KEY_S)) {
            this.position.z += 0.02f;
        }
        if (App.KEYBOARD.isKeyPressed(GLFW.GLFW_KEY_D)) {
            this.position.x += 0.02f;
        }
        if (App.KEYBOARD.isKeyPressed(GLFW.GLFW_KEY_SPACE)) {
            this.position.y += 0.02f;
        }
        if (App.KEYBOARD.isKeyPressed(GLFW.GLFW_KEY_COMMA)) {
            this.position.y -= 0.02f;
        }
    }
    public void rotate() {
        if (App.KEYBOARD.isKeyPressed(GLFW.GLFW_KEY_Q)) {
            this.roll += 0.02f;
        }
        if (App.KEYBOARD.isKeyPressed(GLFW.GLFW_KEY_E)) {
            this.roll -= 0.02f;
        }
    }
}
