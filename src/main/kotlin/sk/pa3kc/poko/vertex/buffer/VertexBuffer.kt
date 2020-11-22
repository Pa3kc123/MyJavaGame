package sk.pa3kc.poko.vertex.buffer

import org.lwjgl.opengl.GL11
import org.lwjgl.opengl.GL15
import org.lwjgl.opengl.GL20
import org.lwjgl.opengl.GL30
import sk.pa3kc.poko.vertex.VertexBufferObject
import java.nio.FloatBuffer

class VertexBuffer() : VertexBufferObject(GL15.GL_ARRAY_BUFFER) {
    constructor(data: FloatBuffer, usage: Int = GL15.GL_STATIC_DRAW, unbind: Boolean = true) {
        bufferData(data, usage, unbind)
    }

    fun bufferData(data: FloatBuffer, usage: Int = GL15.GL_STATIC_DRAW, unbind: Boolean = true) {
        this.bind()
        GL15.glBufferData(super.target, data, usage)

        GL20.glEnableVertexAttribArray(0)
        GL20.glVertexAttribPointer(0, 2, GL11.GL_FLOAT, false, Float.SIZE_BYTES * 2, 0L)

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
