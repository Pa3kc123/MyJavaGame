package sk.pa3kc.holder

import org.lwjgl.opengl.GL15
import sk.pa3kc.poko.vertex.VertexBufferObject
import sk.pa3kc.util.GLCollection

class VertexBufferObjects : GLCollection<VertexBufferObject>() {
    val targetList = HashMap<Int, VertexBufferObject?>()

    override fun close() {
        this.targetList.forEach { (target, obj) ->
            if (obj !== null) {
                GL15.glBindBuffer(target, 0)
            }
        }
        super.close()
    }
}
