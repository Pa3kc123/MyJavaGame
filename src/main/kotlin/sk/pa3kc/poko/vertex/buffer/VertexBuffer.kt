package sk.pa3kc.poko.vertex.buffer

import org.lwjgl.opengl.GL15
import sk.pa3kc.poko.vertex.VertexBufferObject
import java.nio.FloatBuffer

class VertexBuffer() : VertexBufferObject(GL15.GL_ARRAY_BUFFER) {
    constructor(data: FloatBuffer, usage: Int = GL15.GL_STATIC_DRAW, unbind: Boolean = true) {
        bufferData(data, usage, unbind)
    }

    fun bufferData(data: FloatBuffer, usage: Int = GL15.GL_STATIC_DRAW, unbind: Boolean = true) {
        this.bind()
        GL15.glBufferData(super.target, data, usage)

        if (unbind) {
            this.unbind()
        }
    }

    override fun bind() {
        GL15.glBindBuffer(super.target, super.id)
        super.bind()
    }
    override fun unbind() {
        GL15.glBindBuffer(super.target, 0)
        super.unbind()
    }
}
