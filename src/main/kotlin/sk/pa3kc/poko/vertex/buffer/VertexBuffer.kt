package sk.pa3kc.poko.vertex.buffer

import org.lwjgl.opengl.GL11
import org.lwjgl.opengl.GL15
import org.lwjgl.opengl.GL20
import org.lwjgl.opengl.GL30
import sk.pa3kc.GL_DEFAULT_VBO_USAGE
import sk.pa3kc.holder.GLContext
import sk.pa3kc.poko.vertex.VertexBufferObject
import java.nio.FloatBuffer

class VertexBuffer(
    @JvmField
    override val context: GLContext
) : VertexBufferObject(GL15.GL_ARRAY_BUFFER) {
    constructor(context: GLContext, data: FloatBuffer, usage: Int = GL_DEFAULT_VBO_USAGE) : this(context) {
        bufferData(data, usage)
    }

    fun bufferData(data: FloatBuffer, usage: Int = GL_DEFAULT_VBO_USAGE) = editVbo {
        GL15.glBufferData(super.target, data, usage)

        GL20.glEnableVertexAttribArray(0)
        GL20.glVertexAttribPointer(0, 2, GL11.GL_FLOAT, false, Float.SIZE_BYTES * 2, 0L)
    }
}
