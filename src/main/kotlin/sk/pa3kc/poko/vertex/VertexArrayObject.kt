package sk.pa3kc.poko.vertex

import org.lwjgl.opengl.GL11
import org.lwjgl.opengl.GL20
import org.lwjgl.opengl.GL30
import sk.pa3kc.holder.GLContext
import sk.pa3kc.poko.vertex.buffer.VertexBuffer
import sk.pa3kc.util.GLBindable
import sk.pa3kc.util.forEachApply
import java.nio.FloatBuffer

class VertexArrayObject(
    val context: GLContext
) : GLBindable, AutoCloseable {
    val id = GL30.glGenVertexArrays()

    fun addBuffers(vararg layouts: FloatBufferLayout) = editVao {
        layouts.forEachApply {
            context.addVertexBufferObjects(VertexBuffer(context, data))

            GL20.glEnableVertexAttribArray(index)
            GL20.glVertexAttribPointer(index, size, GL11.GL_FLOAT, normalized, Float.SIZE_BYTES * size, 0L)
        }
    }

    private inline fun editVao(block: () -> Unit) {
        bind()
        block()
    }

    override fun bind() {
        GL30.glBindVertexArray(this.id)
        this.context.vertexArrayObjects.boundObject = this
    }

    override fun close() {
        GL30.glDeleteVertexArrays(this.id)
    }

    override fun equals(other: Any?) = other === this || (other is VertexBufferObject && other.id == this.id)
    override fun hashCode() = id
}

data class FloatBufferLayout(
    val index: Int,
    val data: FloatBuffer,
    val size: Int,
    val normalized: Boolean = false
)
