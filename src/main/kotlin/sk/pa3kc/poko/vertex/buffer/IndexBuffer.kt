package sk.pa3kc.poko.vertex.buffer

import org.lwjgl.opengl.GL15
import sk.pa3kc.GL_DEFAULT_VBO_USAGE
import sk.pa3kc.holder.GLContext
import sk.pa3kc.poko.vertex.VertexBufferObject
import java.nio.IntBuffer

class IndexBuffer(
    override val context: GLContext
) : VertexBufferObject(GL15.GL_ELEMENT_ARRAY_BUFFER) {
    constructor(context: GLContext, data: IntBuffer, usage: Int = GL_DEFAULT_VBO_USAGE) : this(context) {
        bufferData(data, usage)
    }

    fun bufferData(data: IntBuffer, usage: Int = GL_DEFAULT_VBO_USAGE) = editVbo {
        GL15.glBufferData(super.target, data, usage)
    }
}
