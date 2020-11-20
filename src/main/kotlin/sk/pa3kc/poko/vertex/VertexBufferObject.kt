package sk.pa3kc.poko.vertex

import org.lwjgl.opengl.GL15
import sk.pa3kc.holder.VertexBufferObjects

abstract class VertexBufferObject(
    val target: Int
) : AutoCloseable {
    val id = GL15.glGenBuffers()

    open fun bind() {
        VertexBufferObjects.targetList[this.target] = true
    }
    open fun unbind() {
        VertexBufferObjects.targetList[this.target] = false
    }

    override fun close() {
        GL15.glDeleteBuffers(this.id)
    }
}
