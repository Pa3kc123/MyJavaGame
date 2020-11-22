package sk.pa3kc.poko.vertex

import org.lwjgl.opengl.GL11
import org.lwjgl.opengl.GL20
import org.lwjgl.opengl.GL30
import sk.pa3kc.holder.VertexArrayObjects
import sk.pa3kc.holder.VertexBufferObjects
import sk.pa3kc.poko.vertex.buffer.IndexBuffer
import sk.pa3kc.poko.vertex.buffer.VertexBuffer
import sk.pa3kc.util.GLCollection
import java.nio.FloatBuffer
import java.nio.IntBuffer

class VertexArrayObject : AutoCloseable {
    val id = GL30.glGenVertexArrays()

    lateinit var indexBuffer: IndexBuffer
        private set
    private val buffers = GLCollection<VertexBuffer>()

    fun bind() {
        GL30.glBindVertexArray(this.id)
        VertexArrayObjects.hasBoundObject = true
        VertexArrayObjects.boundObject = this
    }
    fun unbind() {
        GL30.glBindVertexArray(0)
        VertexArrayObjects.hasBoundObject = false
        VertexArrayObjects.boundObject = null
    }

    fun setIndexBuffer(data: IntBuffer) = editVao {
        indexBuffer = IndexBuffer(data)
    }

    fun addBuffers(vararg layouts: BufferLayout) = editVao {
        layouts.forEach { layout ->
            with(layout) {
                buffers.add(VertexBuffer(data))

                GL20.glEnableVertexAttribArray(index)
                GL20.glVertexAttribPointer(index, size, GL11.GL_FLOAT, normalized, Float.SIZE_BYTES * size, 0L)
            }
        }
    }

    private inline fun editVao(block: () -> Unit) {
        val lastBoundObject = VertexArrayObjects.boundObject

        if (lastBoundObject != this@VertexArrayObject) {
            bind()
        }

        block()

        if (lastBoundObject != this@VertexArrayObject) {
            lastBoundObject?.bind() ?: unbind()
        }
    }

    override fun close() {
        if (VertexArrayObjects.boundObject == this) {
            unbind()
        }

        this.buffers.close()
        if (this::indexBuffer.isInitialized) {
            this.indexBuffer.close()
        }
        GL30.glDeleteVertexArrays(this.id)
    }

    override fun equals(other: Any?) = other === this || (other is VertexBufferObject && other.id == this.id)
    override fun hashCode() = id
}

data class BufferLayout(
    val index: Int,
    val data: FloatBuffer,
    val size: Int,
    val normalized: Boolean = false
)
