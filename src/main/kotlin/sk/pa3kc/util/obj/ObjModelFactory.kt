@file:JvmName("ObjModelFactory")

package sk.pa3kc.util.obj

import sk.pa3kc.ex.GLModelException
import sk.pa3kc.ex.ObjParsingException
import sk.pa3kc.util.validateAsFile
import java.io.File
import java.io.FileReader
import java.io.IOException

@Throws(ObjParsingException::class)
fun loadObjModel(filename: String): ObjModel {
    val file = File(filename).validateAsFile()

    val vertices = ArrayList<Float>()
    val texturesRaw = ArrayList<Float>()
    val normalsRaw = ArrayList<Float>()
    val indices = ArrayList<Int>()

    val faces = ArrayList<Array<String>>()

    val lines = try{
        FileReader(file).use { reader ->
            reader.readLines().filterNot { it.startsWith("#") || it.isBlank() }
        }
    } catch (e: IOException) {
        throw GLModelException("Error occurred while reading ${file.path}", e)
    }

    for (line in lines) {
        val splits = line.split("\\s+").toTypedArray()

        when(splits[0]) {
            "v" -> processVertex2f(vertices, splits)
            "vt" -> processVertex2f(texturesRaw, splits)
            "vn" -> processVertex3f(normalsRaw, splits)
            "f" -> faces += splits
        }
    }

    val textures = FloatArray(texturesRaw.size)
    val normals = FloatArray(normalsRaw.size)

    for (face in faces) {
        processFace(face, indices, texturesRaw, normalsRaw, textures, normals)
    }

    return ObjModel(
        vertices.toFloatArray(),
        textures,
        normals,
        indices.toIntArray()
    )
}

private fun processVertex2f(list: MutableList<Float>, splits: Array<out String>) {
    list.addAll(arrayOf(splits[1].toFloat(), splits[2].toFloat()))
}
private fun processVertex3f(list: MutableList<Float>, splits: Array<out String>) {
    list.addAll(arrayOf(splits[1].toFloat(), splits[2].toFloat(), splits[3].toFloat()))
}
private fun processFace(
    vertexData: Array<out String>,
    indices: MutableList<Int>,
    texturesRaw: List<Float>,
    normalsRaw: List<Float>,
    textures: FloatArray,
    normals: FloatArray
) {
    for (group in vertexData) {
        val indexes = group.split("/")

        val currVertexIndex = indexes[0].toInt() - 1
        indices.add(currVertexIndex)

        //TODO: Try to avoid reassigning of already assigned values

        val currTextureIndex = (indexes[1].toInt() - 1) * 2
        repeat(2) {
            textures[currVertexIndex + it] = texturesRaw[currTextureIndex + it]
        }

        val currNormalIndex = (indexes[2].toInt() - 1) * 3
        repeat(3) {
            normals[currVertexIndex + it] = normalsRaw[currNormalIndex + it]
        }
    }
}
