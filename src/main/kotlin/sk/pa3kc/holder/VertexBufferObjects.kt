package sk.pa3kc.holder

import org.lwjgl.opengl.GL15
import sk.pa3kc.poko.vertex.VertexBufferObject
import sk.pa3kc.util.GLCollection

object VertexBufferObjects : GLCollection<VertexBufferObject>() {
    val targetList = HashMap<Int, Boolean>()

    override fun onAdd(index: Int, element: VertexBufferObject) {
        if (!this.targetList.containsKey(element.target)) {
            this.targetList[element.target] = false
        }
    }

    override fun onAddAll(index: Int, elements: Collection<VertexBufferObject>) {
        for (element in elements) {
            val key = element.target

            if (!this.targetList.containsKey(key)) {
                this.targetList[key] = false
            }
        }
    }

    override fun close() {
        for ((target, isBound) in this.targetList) {
            if (isBound) {
                GL15.glBindBuffer(target, 0)
            }
        }
        super.close()
    }
}
