package sk.pa3kc.holder

import org.lwjgl.opengl.GL30
import sk.pa3kc.poko.vertex.VertexArrayObject
import sk.pa3kc.util.GLCollection

object VertexArrayObjects : GLCollection<VertexArrayObject>() {
    var hasBoundObject = false
    var boundObject: VertexArrayObject? = null

    override fun close() {
        if (hasBoundObject) {
            GL30.glBindVertexArray(0)
        }
        super.close()
    }
}
