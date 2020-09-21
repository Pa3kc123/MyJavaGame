package sk.pa3kc.poko

import java.nio.FloatBuffer
import org.lwjgl.BufferUtils
import org.lwjgl.opengl.GL11
import org.lwjgl.opengl.GL20
import sk.pa3kc.mylibrary.matrix.pojo.Matrix4f

@JvmField val buffer: FloatBuffer = BufferUtils.createFloatBuffer(16) // 4x4 matrix

abstract class ShaderProgram(
    val programId: Int,
    private val vertexShaders: Array<VertexShader>,
    private val fragmentShaders: Array<FragmentShader>
) : AutoCloseable {
//    protected fun bindAttribute(attr: Int, varName: String) {
//        GL20.glBindAttribLocation(this.programId, attr, varName)
//    }

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
        vertexShaders.forEach {
            GL20.glDetachShader(programId, it.shaderId)
            it.close()
        }

        fragmentShaders.forEach {
            GL20.glDetachShader(programId, it.shaderId)
            it.close()
        }

        GL20.glDeleteProgram(this.programId)
    }

    abstract class Builder<T : ShaderProgram> {
        protected val vertexShaders = ArrayList<VertexShader>()
        protected val fragmentShaders = ArrayList<FragmentShader>()

        abstract fun build(): T
    }
}
