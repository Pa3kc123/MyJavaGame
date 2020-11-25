package sk.pa3kc.poko.vertex

import org.lwjgl.opengl.GL15
import sk.pa3kc.holder.GLContext
import sk.pa3kc.util.GLBindable

abstract class VertexBufferObject(
    val target: Int
) : GLBindable, AutoCloseable {
    abstract val context: GLContext

    val id = GL15.glGenBuffers()

    protected inline fun editVbo(block: () -> Unit) {
        bind()
        block()
    }

    override fun bind() {
        GL15.glBindBuffer(this.target, this.id)
        context.vertexBufferObjects.targetList[this.target] = this
    }

    override fun close() {
        GL15.glDeleteBuffers(this.id)
    }
}
