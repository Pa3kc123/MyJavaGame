package sk.pa3kc.poko

import java.io.File
import java.io.FileReader
import org.lwjgl.opengl.GL11
import org.lwjgl.opengl.GL20
import sk.pa3kc.CLASS_LOADER
import java.io.FileInputStream
import java.io.FileNotFoundException

abstract class Shader(
    val shaderId: Int
) : AutoCloseable {
    override fun close() {
        GL20.glDeleteShader(this.shaderId)
    }

    override fun toString() = "Shader[shaderId=${this.shaderId}]"
}

class VertexShader(shaderId: Int) : Shader(shaderId) {
    override fun toString() = "VertexShader[shaderId=${super.shaderId}]"
}

class FragmentShader(shaderId: Int) : Shader(shaderId) {
    override fun toString() = "FragmentShader[shaderId=${super.shaderId}]"
}

@Throws(FileNotFoundException::class)
fun newVertexShaderFromRes(path: String) = VertexShader(newShaderFromRes(path, GL20.GL_VERTEX_SHADER))
@Throws(FileNotFoundException::class)
fun newFragmentShaderFromRes(path: String) = FragmentShader(newShaderFromRes(path, GL20.GL_FRAGMENT_SHADER))
@Throws(FileNotFoundException::class)
private fun newShaderFromRes(path: String, type: Int): Int {
    val shaderSource = CLASS_LOADER.getResource(path)?.readText() ?: throw FileNotFoundException("classpath:${path} does not exists")

    return createShaderObject(shaderSource, type)
}

@Throws(FileNotFoundException::class)
fun newVertexShader(path: String) = VertexShader(newShader(path, GL20.GL_VERTEX_SHADER))
@Throws(FileNotFoundException::class)
fun newVertexShader(file: File) = VertexShader(newShader(file, GL20.GL_VERTEX_SHADER))
@Throws(FileNotFoundException::class)
fun newFragmentShader(path: String) = FragmentShader(newShader(path, GL20.GL_FRAGMENT_SHADER))
@Throws(FileNotFoundException::class)
fun newFragmentShader(file: File) = FragmentShader(newShader(file, GL20.GL_FRAGMENT_SHADER))

@Throws(FileNotFoundException::class)
private fun newShader(path: String, type: Int): Int = newShader(File(path), type)
@Throws(FileNotFoundException::class)
private fun newShader(file: File, type: Int): Int {
    val shaderSource = file.let { sourceFile ->
        if (!sourceFile.exists()) {
            throw FileNotFoundException("${sourceFile.path} does not exists")
        }

        sourceFile.readText(Charsets.UTF_8)
    }

    return createShaderObject(shaderSource, type)
}

private fun createShaderObject(shaderSource: String, type: Int): Int = GL20.glCreateShader(type).also {
    GL20.glShaderSource(it, shaderSource)
    GL20.glCompileShader(it)

    if (GL20.glGetShaderi(it, GL20.GL_COMPILE_STATUS) != GL11.GL_TRUE) {
        System.err.println(GL20.glGetShaderInfoLog(it))
        throw IllegalStateException("Could not compile shader")
    }
}
