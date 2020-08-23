package sk.pa3kc.holder

import java.io.File
import java.io.FileNotFoundException
import java.io.FileReader
import java.io.IOException
import org.lwjgl.opengl.GL11
import org.lwjgl.opengl.GL20
import sk.pa3kc.poko.Shader

private const val VERTEX_SHADER: Int = GL20.GL_VERTEX_SHADER
private const val FRAGMENT_SHADER: Int = GL20.GL_FRAGMENT_SHADER

object Shaders : ArrayList<Shader>(), AutoCloseable {
    override fun close() {
        super.forEach {
            it.close()
        }
    }
}

fun loadVertexShader(file: File): Shader = loadShader(file, VERTEX_SHADER)
fun loadFragmentShader(file: File): Shader = loadShader(file, FRAGMENT_SHADER)

@Throws(IOException::class, IllegalStateException::class)
private fun loadShader(file: File, type: Int): Shader {
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

    val shaderId = GL20.glCreateShader(type).also {
        GL20.glShaderSource(it, shaderSource)
        GL20.glCompileShader(it)

        if (GL20.glGetShaderi(it, GL20.GL_COMPILE_STATUS) != GL11.GL_TRUE) {
            System.err.println(GL20.glGetShaderInfoLog(it))
            throw IllegalStateException("Could not compile shader")
        }
    }

    return Shader(type, shaderId).also {
        Shaders += it
    }
}
