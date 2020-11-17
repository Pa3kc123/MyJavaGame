package sk.pa3kc.holder

import java.nio.FloatBuffer
import java.nio.IntBuffer
import org.lwjgl.BufferUtils
import org.lwjgl.opengl.GL11
import org.lwjgl.opengl.GL15
import org.lwjgl.opengl.GL20
import org.lwjgl.opengl.GL30
import sk.pa3kc.poko.model.RawModel

const val VERTICES = 0
const val TEXTURE_COORDS = 1
const val NORMALS = 2

object VertexArrayObjects : ArrayList<Int>(), AutoCloseable {
    override fun close() = super.forEach {
        GL30.glDeleteVertexArrays(it)
    }
}

object VertexBufferObjects : ArrayList<Int>(), AutoCloseable {
    override fun close() = super.forEach {
        GL20.glDeleteBuffers(it)
    }
}

fun loadModelToVAO(vertices: FloatArray, textCoords: FloatArray, normals: FloatArray, indices: IntArray): RawModel {
    val vaoId = createVAO()
    bindVAO(vaoId)
    bindIndicesBuffer(indices)
    storeDataInAttrList(VERTICES, 3, vertices)
    storeDataInAttrList(TEXTURE_COORDS, 2, textCoords)
    storeDataInAttrList(NORMALS, 3, normals)
    unbindVAO()
    return RawModel(vaoId, vertices.size)
}

private fun createVAO(): Int = GL30.glGenVertexArrays().also { VertexArrayObjects.add(it) }
private fun createVBO(): Int = GL15.glGenBuffers().also { VertexBufferObjects.add(it) }

private fun bindVAO(vaoId: Int) = GL30.glBindVertexArray(vaoId)

private fun storeDataInAttrList(attrId: Int, size: Int, attrData: FloatArray) {
    val vboId = createVBO()
    val dataBuffer = createFloatBufferWithData(*attrData)

    GL15.glBindBuffer(GL15.GL_ARRAY_BUFFER, vboId)
    GL15.glBufferData(GL15.GL_ARRAY_BUFFER, dataBuffer, GL15.GL_STATIC_DRAW)
    GL20.glVertexAttribPointer(attrId, size, GL11.GL_FLOAT, false, 0, 0)

    GL15.glBindBuffer(GL15.GL_ARRAY_BUFFER, 0)
}

private fun unbindVAO() {
    GL30.glBindVertexArray(0)
}

private fun bindIndicesBuffer(indices: IntArray) {
    val vboId = createVBO()
    GL15.glBindBuffer(GL15.GL_ELEMENT_ARRAY_BUFFER, vboId)

    val indicesBuffer = createIntBufferWithData(*indices)
    GL15.glBufferData(GL15.GL_ELEMENT_ARRAY_BUFFER, indicesBuffer, GL15.GL_STATIC_DRAW)

    GL15.glBindBuffer(GL15.GL_ELEMENT_ARRAY_BUFFER, 0)
}

private fun createFloatBufferWithData(vararg fData: Float): FloatBuffer {
    return BufferUtils.createFloatBuffer(fData.size).apply {
        put(fData)
        flip()
    }
}

private fun createIntBufferWithData(vararg iData: Int): IntBuffer {
    return BufferUtils.createIntBuffer(iData.size).apply {
        put(iData)
        flip()
    }
}
