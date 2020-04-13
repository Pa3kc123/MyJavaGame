package sk.pa3kc.util;

import java.nio.FloatBuffer;
import java.nio.IntBuffer;
import java.util.ArrayList;
import java.util.List;

import org.lwjgl.BufferUtils;
import org.lwjgl.opengl.GL11;
import org.lwjgl.opengl.GL15;
import org.lwjgl.opengl.GL20;
import org.lwjgl.opengl.GL30;

import sk.pa3kc.pojo.RawModel;

public class Loader implements AutoCloseable {
    public static final int VERTICIES_ATTR_ID = 0;
    public static final int TEXTURE_COORDS_ATTR_ID = 1;
    public static final int NORMALS_ATTR_ID = 2;

    private List<Integer> vaos = new ArrayList<>();
    private List<Integer> vbos = new ArrayList<>();

    public RawModel loadModelToVAO(float[] verticies, float[] textCoords, float[] normals, int[] indicies) {
        final int vaoId = createVAO();
        bindIndiciesBuffer(indicies);
        storeDataInAttrList(VERTICIES_ATTR_ID, 3, verticies);
        storeDataInAttrList(TEXTURE_COORDS_ATTR_ID, 2, textCoords);
        storeDataInAttrList(NORMALS_ATTR_ID, 3, normals);
        unbindVAO();
        return new RawModel(vaoId, verticies.length);
    }

    private int createVAO() {
        final int vaoId = GL30.glGenVertexArrays();
        this.vaos.add(vaoId);

        GL30.glBindVertexArray(vaoId);

        return vaoId;
    }

    private void storeDataInAttrList(int attrId, int size, float[] data) {
        final int vboId = GL15.glGenBuffers();
        this.vbos.add(vboId);

        final FloatBuffer dataBuffer = this.createFloatBufferWithData(data);

        GL15.glBindBuffer(GL15.GL_ARRAY_BUFFER, vboId);
        GL15.glBufferData(GL15.GL_ARRAY_BUFFER, dataBuffer, GL15.GL_STATIC_DRAW);
        GL20.glVertexAttribPointer(attrId, size, GL11.GL_FLOAT, false, 0, 0);

        GL15.glBindBuffer(GL15.GL_ARRAY_BUFFER, 0);
    }

    private void unbindVAO() {
        GL30.glBindVertexArray(0);
    }

    private void bindIndiciesBuffer(int[] indicies) {
        final int vboId = GL15.glGenBuffers();
        this.vbos.add(vboId);

        GL15.glBindBuffer(GL15.GL_ELEMENT_ARRAY_BUFFER, vboId);

        final IntBuffer indiciesBuffer = this.createIntBufferWithData(indicies);

        GL15.glBufferData(GL15.GL_ELEMENT_ARRAY_BUFFER, indiciesBuffer, GL15.GL_STATIC_DRAW);
    }

    private FloatBuffer createFloatBufferWithData(float... data) {
        if (data == null) return null;

        final FloatBuffer buffer = BufferUtils.createFloatBuffer(data.length);
        buffer.put(data);
        buffer.flip();

        return buffer;
    }

    private IntBuffer createIntBufferWithData(int... data) {
        if (data == null) return null;

        final IntBuffer buffer = BufferUtils.createIntBuffer(data.length);
        buffer.put(data);
        buffer.flip();

        return buffer;
    }

    @Override
    public void close() {
        for (final int vao : this.vaos) {
            GL30.glDeleteVertexArrays(vao);
        }
        for (final int vbo : this.vbos) {
            GL15.glDeleteBuffers(vbo);
        }
    }
}
