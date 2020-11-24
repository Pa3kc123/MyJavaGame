package sk.pa3kc.holder

import sk.pa3kc.poko.program.ShaderProgram
import sk.pa3kc.poko.vertex.VertexArrayObject
import sk.pa3kc.poko.vertex.VertexBufferObject

object GLContext : AutoCloseable {
    val vertexArrayObjects = VertexArrayObjects()
    val vertexBufferObjects = VertexBufferObjects()
    val shaderPrograms = ShaderPrograms()

    fun addVertexArrayObjects(obj: VertexArrayObject) = this.vertexArrayObjects.add(obj)
    fun addVertexBufferObjects(obj: VertexBufferObject) = this.vertexBufferObjects.add(obj)
    fun addShaderPrograms(obj: ShaderProgram) = this.shaderPrograms.add(obj)

    fun bindVAO(index: Int) = this.vertexArrayObjects[index].bind()
//!   Not the best idea due to Index/Vertex buffers share same collection
//    fun bindVBO(index: Int) = this.vertexBufferObjects[index].bind()
    fun bindProgram(index: Int) = this.shaderPrograms.bindProgram(index)

    override fun close() {
        if (this.shaderPrograms.hasActiveProgram) {
            this.shaderPrograms.unbindProgram()
        }
        this.vertexArrayObjects.close()
        this.vertexBufferObjects.close()
    }
}
