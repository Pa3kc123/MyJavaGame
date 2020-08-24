package sk.pa3kc.poko

import org.lwjgl.opengl.GL11
import org.lwjgl.opengl.GL20
import java.io.File
import java.io.FileNotFoundException
import java.io.FileReader
import java.io.IOException

private const val VERTEX_SHADER: Int = GL20.GL_VERTEX_SHADER
private const val FRAGMENT_SHADER: Int = GL20.GL_FRAGMENT_SHADER

abstract class Shader(
    val shaderId: Int
) : AutoCloseable {
    override fun close() {
        GL20.glDeleteShader(this.shaderId)
    }

    override fun toString(): String = log()

    abstract fun log(): String
}

class VertexShader(shaderId: Int) : Shader(shaderId) {
    override fun log(): String = "VertexShader[shaderId=${super.shaderId}]"
}
class FragmentShader(shaderId: Int) : Shader(shaderId) {
    override fun log(): String = "FragmentShader[shaderId=${super.shaderId}]"
}

fun loadVertexShader(file: File) = VertexShader(loadShader(file, VERTEX_SHADER))
fun loadFragmentShader(file: File) = FragmentShader(loadShader(file, FRAGMENT_SHADER))

@Throws(IOException::class, IllegalStateException::class)
private fun loadShader(file: File, type: Int): Int {
    if (!file.exists()) {
        throw FileNotFoundException("${file.path} does not exists")
    }

    val shaderSource: String
    try {
        shaderSource = FileReader(file).readText()
    } catch (e: Exception) {
        e.printStackTrace()
        throw e
    }

    return GL20.glCreateShader(type).also {
        GL20.glShaderSource(it, shaderSource)
        GL20.glCompileShader(it)

        if (GL20.glGetShaderi(it, GL20.GL_COMPILE_STATUS) != GL11.GL_TRUE) {
            System.err.println(GL20.glGetShaderInfoLog(it))
            throw IllegalStateException("Could not compile shader")
        }
    }
}
