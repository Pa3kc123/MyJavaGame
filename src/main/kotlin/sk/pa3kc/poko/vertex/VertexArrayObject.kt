package sk.pa3kc.poko.vertex

import org.lwjgl.opengl.GL20
import org.lwjgl.opengl.GL30
import sk.pa3kc.holder.VertexArrayObjects

class VertexArrayObject : AutoCloseable {
    val id = GL30.glGenVertexArrays()

    fun addBuffer(index: Int, vbo: VertexBufferObject, layout: VertexArrayObjectLayout): Boolean {
        val lastBoundObject = VertexArrayObjects.lastBoundObject

        if (lastBoundObject != this) {
            this.bind()
        }

        GL20.glEnableVertexAttribArray(index)
        with(layout) {
            GL20.glVertexAttribPointer(index, size, type, normalize, strideSize * size, 0L)
        }

        if (lastBoundObject != this) {
            lastBoundObject?.bind() ?: this.unbind()
        }

        return true
    }

    fun bind() {
        GL30.glBindVertexArray(this.id)
        VertexArrayObjects.hasBoundObject = true
        VertexArrayObjects.lastBoundObject = this
    }
    fun unbind() {
        GL30.glBindVertexArray(0)
        VertexArrayObjects.hasBoundObject = false
        VertexArrayObjects.lastBoundObject = null
    }

    override fun close() {
        GL30.glDeleteVertexArrays(this.id)
    }

    override fun equals(other: Any?) = other === this || (other is VertexBufferObject && other.id == this.id)
    override fun hashCode() = id
}

data class VertexArrayObjectLayout(
    val size: Int,
    val type: Int,
    val normalize: Boolean = false,
    val strideSize: Int
)
