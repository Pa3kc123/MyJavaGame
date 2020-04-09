package sk.pa3kc.pojo;

import static org.lwjgl.opengl.GL11.*;
import static org.lwjgl.opengl.GL15.GL_ARRAY_BUFFER;
import static org.lwjgl.opengl.GL15.GL_STATIC_DRAW;
import static org.lwjgl.opengl.GL15C.glBindBuffer;
import static org.lwjgl.opengl.GL15C.glBufferData;
import static org.lwjgl.opengl.GL15C.glGenBuffers;

import java.nio.FloatBuffer;
import java.nio.IntBuffer;

import org.lwjgl.BufferUtils;

public class Model {
    private static int NULL = 0;

    private int drawCount;
    private int vertexId;
    private int textureId;

    public Model(float[] vertecies, int[] textureCoords) {
        this.drawCount = vertecies.length;

        final FloatBuffer verteciesBuffer = BufferUtils.createFloatBuffer(vertecies.length);
        verteciesBuffer.put(vertecies);
        verteciesBuffer.flip();

        final IntBuffer textureCoordsBuffer = BufferUtils.createIntBuffer(textureCoords.length);
        textureCoordsBuffer.put(textureCoords);
        textureCoordsBuffer.flip();

        this.vertexId = glGenBuffers();
        glBindBuffer(GL_ARRAY_BUFFER, this.vertexId);
        glBufferData(GL_ARRAY_BUFFER, verteciesBuffer, GL_STATIC_DRAW);

        this.textureId = glGenBuffers();
        glBindBuffer(GL_ARRAY_BUFFER, this.textureId);
        glBufferData(GL_ARRAY_BUFFER, textureCoordsBuffer, GL_STATIC_DRAW);

        glBindBuffer(GL_ARRAY_BUFFER, NULL);
    }

    public void render() {
        glEnableClientState(GL_VERTEX_ARRAY);
        glEnableClientState(GL_TEXTURE_COORD_ARRAY);

        glBindBuffer(GL_ARRAY_BUFFER, this.vertexId);
        glVertexPointer(3, GL_FLOAT, NULL, NULL);

        glBindBuffer(GL_ARRAY_BUFFER, this.textureId);
        glTexCoordPointer(2, GL_INT, NULL, NULL);

        glDrawArrays(GL_TRIANGLES, 0, this.drawCount);

        glBindBuffer(GL_ARRAY_BUFFER, NULL);

        glDisableClientState(GL_VERTEX_ARRAY);
        glDisableClientState(GL_TEXTURE_COORD_ARRAY);
    }
}
