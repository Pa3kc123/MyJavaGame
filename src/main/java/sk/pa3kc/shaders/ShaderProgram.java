package sk.pa3kc.shaders;

import java.nio.FloatBuffer;

import org.lwjgl.BufferUtils;
import org.lwjgl.opengl.GL11;
import org.lwjgl.opengl.GL20;

import sk.pa3kc.pojo.matrix.Matrix4f;

public abstract class ShaderProgram implements AutoCloseable {
    private static FloatBuffer buffer = BufferUtils.createFloatBuffer(16); // 4x4 matrix

    private final int programId;
    private final VertexShader vs;
    private final FragmentShader fs;

    public ShaderProgram(VertexShader vs, FragmentShader fs) {
        this.vs = vs;
        this.fs = fs;

        this.programId = GL20.glCreateProgram();
        GL20.glAttachShader(this.programId, vs.getShaderId());
        GL20.glAttachShader(this.programId, fs.getShaderId());

        this.bindAttributes();

        GL20.glLinkProgram(this.programId);

        if (GL20.glGetProgrami(this.programId, GL20.GL_LINK_STATUS) != GL11.GL_TRUE) {
            System.err.println(GL20.glGetProgramInfoLog(this.programId));
            System.err.println("Could not compile shader program");
            System.exit(-1);
        }

        GL20.glValidateProgram(this.programId);

        if (GL20.glGetProgrami(this.programId, GL20.GL_VALIDATE_STATUS) != GL11.GL_TRUE) {
            System.err.println(GL20.glGetProgramInfoLog(this.programId));
            System.err.println("validation was not successful for shader program");
            System.exit(-1);
        }

        this.getAllUniformLocations();
    }

    public void start() {
        GL20.glUseProgram(this.programId);
    }

    public void stop() {
        GL20.glUseProgram(0);
    }

    protected void bindAttribute(int attr, String varName) {
        GL20.glBindAttribLocation(this.programId, attr, varName);
    }

    protected int getUniformLocation(String uniformName) {
        return GL20.glGetUniformLocation(this.programId, uniformName);
    }

    protected void loadFloat(int location, float value) {
        GL20.glUniform1f(location, value);
    }

    protected void loadVector(int location, float x, float y, float z) {
        GL20.glUniform3f(location, x, y, z);
    }

    protected void loadBool(int location, boolean value) {
        GL20.glUniform1f(location, value ? 1f : 0f);
    }

    protected void loadMatrix(int location, Matrix4f matrix) {
        buffer.put(matrix.getMatrix(), 0, matrix.getMatrix().length);
        buffer.rewind();
        GL20.glUniformMatrix4fv(location, false, buffer);
    }

    @Override
    public void close() {
        // Stop using current program
        stop();

        // Detach all shaders from current program
        GL20.glDetachShader(this.programId, this.vs.getShaderId());
        GL20.glDetachShader(this.programId, this.fs.getShaderId());

        // Dispose all shaders in this program
        this.vs.close();
        this.fs.close();

        // Dispose current program
        GL20.glDeleteProgram(this.programId);
    }

    protected abstract void getAllUniformLocations();
    public abstract void bindAttributes();
}
