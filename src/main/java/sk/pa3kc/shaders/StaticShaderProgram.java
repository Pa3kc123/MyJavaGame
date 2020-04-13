package sk.pa3kc.shaders;

import java.util.HashMap;

import sk.pa3kc.entities.Camera;
import sk.pa3kc.entities.Light;
import sk.pa3kc.pojo.matrix.Matrix4f;
import sk.pa3kc.pojo.matrix.Vector3f;
import sk.pa3kc.util.MatrixMath;

public class StaticShaderProgram extends ShaderProgram {
    public static final HashMap<Integer, String> ATTRIBUTES = new HashMap<Integer, String>() {
        private static final long serialVersionUID = 1L;

        {
            super.put(0, "position");
            super.put(1, "textureCoord");
            super.put(2, "normal");
        }
    };

    private int location_transformationMatrix;
    private int location_projectionMatrix;
    private int location_viewMatrix;
    private int location_lightPosition;
    private int location_lightColor;

    public StaticShaderProgram(VertexShader vs, FragmentShader fs) {
        super(vs, fs);
    }

    @Override
    public void bindAttributes() {
        for (final int key : ATTRIBUTES.keySet()) {
            super.bindAttribute(key, ATTRIBUTES.get(key));
        }
    }

    @Override
    protected void getAllUniformLocations() {
        this.location_transformationMatrix = super.getUniformLocation("transformationMatrix");
        this.location_projectionMatrix = super.getUniformLocation("projectionMatrix");
        this.location_viewMatrix = super.getUniformLocation("viewMatrix");
        this.location_lightPosition = super.getUniformLocation("lightPosition");
        this.location_lightColor = super.getUniformLocation("lightColor");
    }

    public void loadTransformationMatrix(Matrix4f matrix) {
        super.loadMatrix(this.location_transformationMatrix, matrix);
    }

    public void loadProjectionMatrix(Matrix4f matrix) {
        super.loadMatrix(this.location_projectionMatrix, matrix);
    }

    public void loadViewMatrix(Camera camera) {
        final Matrix4f matrix = MatrixMath.createViewMatrix(camera);
        super.loadMatrix(this.location_viewMatrix, matrix);
    }

    public void loadLight(Light light) {
        final Vector3f pos = light.getPosition();
        final Vector3f color = light.getColor();

        super.loadVector(this.location_lightPosition, pos.x, pos.y, pos.z);
        super.loadVector(this.location_lightColor, color.x, color.y, color.z);
    }
}
