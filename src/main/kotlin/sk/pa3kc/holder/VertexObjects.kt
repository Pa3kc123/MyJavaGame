package sk.pa3kc.holder

import sk.pa3kc.poko.model.RawModel
import sk.pa3kc.poko.vertex.BufferLayout
import sk.pa3kc.poko.vertex.VertexArrayObject
import sk.pa3kc.util.newFloatBuffer
import sk.pa3kc.util.newIntBuffer

const val VERTICES = 0
const val TEXTURE_COORDS = 1
const val NORMALS = 2

fun loadModelToVAO(vertices: FloatArray, textCoords: FloatArray, normals: FloatArray, indices: IntArray): RawModel {
    val vao = VertexArrayObject()
    VertexArrayObjects.add(vao)
    vao.setIndexBuffer(newIntBuffer(*indices))
    vao.addBuffers(
        BufferLayout(VERTICES, newFloatBuffer(*vertices), 3, false),
        BufferLayout(TEXTURE_COORDS, newFloatBuffer(*textCoords), 2, false),
        BufferLayout(NORMALS, newFloatBuffer(*normals), 3, false)
    )
    return RawModel(vao.id, vertices.size)
}
