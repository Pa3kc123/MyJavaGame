package sk.pa3kc.entities;

import org.lwjgl.glfw.GLFW;

import sk.pa3kc.App;
import sk.pa3kc.pojo.matrix.Vector3f;

public class Camera {
    public Vector3f position = new Vector3f(0f, 0f, 0f);
    public float pitch;
    public float yaw;
    public float roll;

    public Camera() {}

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
    }
}
