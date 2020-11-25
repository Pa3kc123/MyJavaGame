package sk.pa3kc.holder

import sk.pa3kc.poko.program.ShaderProgram
import sk.pa3kc.poko.vertex.VertexArrayObject
import sk.pa3kc.poko.vertex.VertexBufferObject

class GLContext : AutoCloseable {
    val vertexArrayObjects = VertexArrayObjects()
    val vertexBufferObjects = VertexBufferObjects()
    val shaderPrograms = ShaderPrograms()

    fun addVertexArrayObjects(obj: VertexArrayObject) = this.vertexArrayObjects.add(obj)
    fun addVertexBufferObjects(obj: VertexBufferObject) = this.vertexBufferObjects.add(obj)
    fun addShaderPrograms(obj: ShaderProgram) = this.shaderPrograms.add(obj)

    override fun close() {
        if (this.vertexArrayObjects.hasBoundObject) {
            this.vertexArrayObjects.unbind()
        }
        if (this.vertexBufferObjects.targetList.isNotEmpty()) {
            this.vertexBufferObjects.unbind()
        }
        if (this.shaderPrograms.hasActiveProgram) {
            this.shaderPrograms.unbind()
        }

        this.vertexArrayObjects.close()
        this.vertexBufferObjects.close()
        this.shaderPrograms.close()
    }
}
