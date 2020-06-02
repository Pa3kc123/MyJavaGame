package sk.pa3kc.entities;

import sk.pa3kc.mylibrary.matrix.pojo.Vector3f;

public class Light {
    private Vector3f position;
    private Vector3f color;

    public Light(Vector3f position, Vector3f color) {
        this.position = position;
        this.color = color;
    }

    public Vector3f getPosition() {
        return this.position;
    }
    public Vector3f getColor() {
        return this.color;
    }
}
