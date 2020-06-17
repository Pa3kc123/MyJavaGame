package sk.pa3kc.shaders;

import sk.pa3kc.entities.Camera;
import sk.pa3kc.entities.Light;
import sk.pa3kc.mylibrary.matrix.math.MatrixMath;
import sk.pa3kc.mylibrary.matrix.pojo.Matrix4f;
import sk.pa3kc.mylibrary.matrix.pojo.Vector3f;

public class StaticShaderProgram extends ShaderProgram {
    public StaticShaderProgram(VertexShader vs, FragmentShader fs) {
        super(vs, fs);
    }

    @Override
    public void bindAttributes() {
        for (final Attribute attr : Attribute.values()) {
            super.bindAttribute(attr.index, attr.attributeName);
        }
    }

    @Override
    protected void getAllUniformLocations() {
        for (final UniformLocation ul : UniformLocation.values()) {
            ul.location = super.getUniformLocation(ul.uniformName);
        }
    }

    public void loadTransformationMatrix(Matrix4f matrix) {
        super.loadMatrix(UniformLocation.TRANSFORMATION_MATRIX.location, matrix);
    }

    public void loadProjectionMatrix(Matrix4f matrix) {
        super.loadMatrix(UniformLocation.PROJECTION_MATRIX.location, matrix);
    }

    public void loadViewMatrix(Camera camera) {
        final Matrix4f matrix = MatrixMath.createViewMatrix(camera.getPosition(), camera.getPitch(), camera.getYaw(), camera.getRoll());
        super.loadMatrix(UniformLocation.VIEW_MATRIX.location, matrix);
    }

    public void loadLight(Light light) {
        final Vector3f pos = light.getPosition();
        final Vector3f color = light.getColor();

        super.loadVector(UniformLocation.LIGHT_POSITION.location, pos.x, pos.y, pos.z);
        super.loadVector(UniformLocation.LIGHT_COLOR.location, color.x, color.y, color.z);
    }
}

enum Attribute {
    POSITION(0, "position"),
    TEXTURE_COORD(1, "textureCoord"),
    NORMAL(2, "normal");

    public final int index;
    public final String attributeName;
    private Attribute(int index, String attributeName) {
        this.index = index;
        this.attributeName = attributeName;
    }
}

enum UniformLocation {
    TRANSFORMATION_MATRIX("transformationMatrix"),
    PROJECTION_MATRIX("projectionMatrix"),
    VIEW_MATRIX("viewMatrix"),
    LIGHT_POSITION("lightPosition"),
    LIGHT_COLOR("lightColor");

    int location;
    public final String uniformName;
    private UniformLocation(String uniformName) {
        this.uniformName = uniformName;
    }
}
