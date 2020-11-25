package sk.pa3kc.holder

import org.lwjgl.opengl.GL15
import sk.pa3kc.poko.vertex.VertexBufferObject
import sk.pa3kc.util.GLCollection

class VertexBufferObjects : GLCollection<VertexBufferObject>() {
    val targetList = HashMap<Int, VertexBufferObject?>()

    fun unbind(index: Int) {
        GL15.glBindBuffer(index, 0)
        targetList[index] = null
    }
    override fun unbind() {
        this.targetList.forEach { (target, obj) ->
            if (obj !== null) {
                GL15.glBindBuffer(target, 0)
            }
        }
    }

    override fun close() {
        this.unbind()
        super.close()
    }
}
