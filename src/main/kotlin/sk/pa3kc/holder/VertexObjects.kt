package sk.pa3kc.holder

import org.lwjgl.opengl.GL11
import org.lwjgl.opengl.GL15
import org.lwjgl.opengl.GL20
import org.lwjgl.opengl.GL30
import sk.pa3kc.poko.model.RawModel
import sk.pa3kc.poko.vertex.VertexArrayObject
import sk.pa3kc.poko.vertex.VertexBufferObject
import sk.pa3kc.poko.vertex.buffer.IndexBuffer
import sk.pa3kc.poko.vertex.buffer.VertexBuffer
import sk.pa3kc.util.newFloatBuffer
import sk.pa3kc.util.newIntBuffer

const val VERTICES = 0
const val TEXTURE_COORDS = 1
const val NORMALS = 2

fun loadModelToVAO(vertices: FloatArray, textCoords: FloatArray, normals: FloatArray, indices: IntArray): RawModel {
    val vao = VertexArrayObject()
    VertexArrayObjects.add(vao)
    vao.bind()
    bindIndicesBuffer(indices)
    storeDataInAttrList(VERTICES, 3, vertices)
    storeDataInAttrList(TEXTURE_COORDS, 2, textCoords)
    storeDataInAttrList(NORMALS, 3, normals)
    vao.unbind()
    return RawModel(vao.id, vertices.size)
}

private fun storeDataInAttrList(attrId: Int, size: Int, attrData: FloatArray) {
    VertexBufferObjects.add(
        VertexBuffer(newFloatBuffer(*attrData))
    )
    GL20.glVertexAttribPointer(attrId, size, GL11.GL_FLOAT, false, Float.SIZE_BYTES * size, 0)
}

private fun bindIndicesBuffer(indices: IntArray) {
    VertexBufferObjects.add(
        IndexBuffer(newIntBuffer(*indices))
    )
}
