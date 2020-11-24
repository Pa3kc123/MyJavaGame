package sk.pa3kc.poko.vertex

import org.lwjgl.opengl.GL15
import sk.pa3kc.holder.GLContext
import sk.pa3kc.util.GLBindable

abstract class VertexBufferObject(
    val target: Int
) : GLBindable, AutoCloseable {
    abstract val context: GLContext

    val id = GL15.glGenBuffers()
    var isBound = true
        private set

    protected inline fun editVbo(block: () -> Unit) {
        bind()
        block()
        unbind()
    }

    override fun bind() {
        GL15.glBindBuffer(this.target, this.id)
        this.isBound = true
        context.vertexBufferObjects.targetList[this.target] = this
    }
    override fun unbind() {
        GL15.glBindBuffer(this.target, 0)
        this.isBound = false
        context.vertexBufferObjects.targetList[this.target] = null
    }

    override fun close() {
        if (this.isBound) {
            this.unbind()
        }
        GL15.glDeleteBuffers(this.id)
    }
}
