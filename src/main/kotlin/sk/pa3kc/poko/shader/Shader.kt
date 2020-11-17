package sk.pa3kc.poko.shader

import java.io.File
import org.lwjgl.opengl.GL11
import org.lwjgl.opengl.GL20
import sk.pa3kc.ex.GLShaderException
import java.io.FileNotFoundException
import sk.pa3kc.ui.Logger
import sk.pa3kc.util.validateAsFile
import java.io.IOException

abstract class Shader(
    open val shaderId: Int
) : AutoCloseable {
    var isDetached = false
        private set
    var isDeleted = false
        private set

    companion object {
        @JvmStatic
        @Throws(GLShaderException::class)
        internal fun newShader(path: String, type: Int): Int {
            val file = File(path).validateAsFile()

            val shaderSource: String
            try {
                shaderSource = file.readText(Charsets.UTF_8)
            } catch (e: IOException) {
                throw GLShaderException("Error occurred while reading shader file", e)
            }

            return GL20.glCreateShader(type).also {
                GL20.glShaderSource(it, shaderSource)
                GL20.glCompileShader(it)

                if (GL20.glGetShaderi(it, GL20.GL_COMPILE_STATUS) != GL11.GL_TRUE) {
                    System.err.println("Could not compile shader")
                    System.err.println(GL20.glGetShaderInfoLog(it))
                    throw GLShaderException("Could not compile shader")
                }
            }
        }
    }

    fun detach(programId: Int) {
        GL20.glDetachShader(programId, this.shaderId)
        this.isDetached = true
    }

    override fun close() {
        GL20.glDeleteShader(this.shaderId)
        this.isDeleted = true
    }

    override fun toString() = "Shader[shaderId=${this.shaderId}]"
}
