package sk.pa3kc.shader.program

import java.nio.FloatBuffer
import kotlin.system.exitProcess

import org.lwjgl.BufferUtils
import org.lwjgl.opengl.GL11
import org.lwjgl.opengl.GL20

import sk.pa3kc.mylibrary.matrix.pojo.Matrix4f
import sk.pa3kc.poko.Shader

@JvmField val buffer: FloatBuffer = BufferUtils.createFloatBuffer(16) // 4x4 matrix

abstract class ShaderProgram(
        private val vs: Shader,
        private val fs: Shader
) : AutoCloseable {
    private val programId: Int

    init {
        this.programId = GL20.glCreateProgram().also {
            GL20.glAttachShader(it, vs.shaderId)
            GL20.glAttachShader(it, fs.shaderId)

            bindAttributes()

            GL20.glLinkProgram(it)

            if (GL20.glGetProgrami(it, GL20.GL_LINK_STATUS) != GL11.GL_TRUE) {
                System.err.println(GL20.glGetProgramInfoLog(it))
                System.err.println("Could not compile shader program")
                exitProcess(-1)
            }

            GL20.glValidateProgram(it)

            if (GL20.glGetProgrami(it, GL20.GL_VALIDATE_STATUS) != GL11.GL_TRUE) {
                System.err.println(GL20.glGetProgramInfoLog(it))
                System.err.println("Validation was not successful for shader program")
                exitProcess(-1)
            }
        }

        getAllUniformLocations()
    }

    fun start() = GL20.glUseProgram(this.programId)
    fun stop() = GL20.glUseProgram(0)

    protected fun bindAttribute(attr: Int, varName: String) {
        GL20.glBindAttribLocation(this.programId, attr, varName)
    }

    protected fun getUniformLocation(uniformName: String): Int {
        return GL20.glGetUniformLocation(this.programId, uniformName)
    }

    protected fun loadFloat(location: Int, value: Float) {
        GL20.glUniform1f(location, value)
    }

    protected fun loadVector(location: Int, x: Float, y: Float, z: Float) {
        GL20.glUniform3f(location, x, y, z)
    }

    private fun Boolean.toGlBoolean() = if (this) GL11.GL_TRUE else GL11.GL_FALSE
    protected fun loadBool(location: Int, value: Boolean) {
        GL20.glUniform1i(location, value.toGlBoolean())
    }

    protected fun loadMatrix(location: Int, matrix: Matrix4f) {
        buffer.put(matrix.matrix, 0, matrix.matrix.size)
        buffer.rewind()
        GL20.glUniformMatrix4fv(location, false, buffer)
    }

    override fun close() {
        // Stop using current program
        this.stop()

        // Detach all shaders from current program
        GL20.glDetachShader(this.programId, this.vs.shaderId)
        GL20.glDetachShader(this.programId, this.fs.shaderId)

        // Dispose all shaders in this program
        this.vs.close()
        this.fs.close()

        // Dispose current program
        GL20.glDeleteProgram(this.programId)
    }

    protected abstract fun getAllUniformLocations()
    abstract fun bindAttributes()
}
