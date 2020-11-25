package sk.pa3kc.holder

import org.lwjgl.opengl.GL30
import sk.pa3kc.poko.vertex.VertexArrayObject
import sk.pa3kc.util.GLCollection

class VertexArrayObjects : GLCollection<VertexArrayObject>() {
    var hasBoundObject = false
        private set
    var boundObject: VertexArrayObject? = null
        set(value) {
            field = value
            this.hasBoundObject = field != null
        }

    override fun unbind() {
        GL30.glBindVertexArray(0)
        this.boundObject = null
    }

    override fun close() {
        if (this.hasBoundObject) {
            this.unbind()
        }
        super.close()
    }
}
