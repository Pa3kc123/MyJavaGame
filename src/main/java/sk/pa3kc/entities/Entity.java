package sk.pa3kc.entities;

import sk.pa3kc.pojo.TexturedModel;
import sk.pa3kc.pojo.matrix.Vector3f;

public class Entity {
    private TexturedModel model;
    private Vector3f position;
    private float rotX;
    private float rotY;
    private float rotZ;
    private float scale;

    public Entity(
        TexturedModel model,
        Vector3f position,
        float rotX,
        float rotY,
        float rotZ,
        float scale
    ) {
        super();
        this.model = model;
        this.position = position;
        this.rotX = rotX;
        this.rotY = rotY;
        this.rotZ = rotZ;
        this.scale = scale;
    }

    public TexturedModel getModel() {
        return this.model;
    }
    public Vector3f getPosition() {
        return this.position;
    }
    public float getRotX() {
        return this.rotX;
    }
    public float getRotY() {
        return this.rotY;
    }
    public float getRotZ() {
        return this.rotZ;
    }
    public float getScale() {
        return this.scale;
    }

    public void setModel(TexturedModel model) {
        this.model = model;
    }
    public void setPosition(Vector3f position) {
        this.position = position;
    }
    public void setRotX(float rotX) {
        this.rotX = rotX;
    }
    public void setRotY(float rotY) {
        this.rotY = rotY;
    }
    public void setRotZ(float rotZ) {
        this.rotZ = rotZ;
    }
    public void setScale(float scale) {
        this.scale = scale;
    }

    public void move(float dx, float dy, float dz) {
        this.position.x += dx;
        this.position.y += dy;
        this.position.z += dz;
    }
    public void rotate(float dx, float dy, float dz) {
        this.rotX += dx;
        this.rotY += dy;
        this.rotZ += dz;
    }
}
