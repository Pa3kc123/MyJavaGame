package sk.pa3kc.pojo.matrix;

public class Vector2f {
    public float x;
    public float y;

    public Vector2f() {
        this(0f, 0f);
    }
    public Vector2f(float x, float y) {
        this.x = x;
        this.y = y;
    }

    public static Vector2f negate(Vector2f vec) {
        return new Vector2f(-vec.x, -vec.y);
    }
}
