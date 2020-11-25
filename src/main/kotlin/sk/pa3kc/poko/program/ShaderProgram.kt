package sk.pa3kc.poko.program

import java.nio.FloatBuffer
import org.lwjgl.BufferUtils
import org.lwjgl.opengl.GL11
import org.lwjgl.opengl.GL20
import sk.pa3kc.holder.GLContext
import sk.pa3kc.mylibrary.matrix.pojo.Matrix4f
import sk.pa3kc.poko.shader.FragmentShader
import sk.pa3kc.poko.shader.VertexShader
import sk.pa3kc.util.GLBindable

@JvmField val buffer: FloatBuffer = BufferUtils.createFloatBuffer(16) // 4x4 matrix

abstract class ShaderProgram(
    val id: Int
) : GLBindable, AutoCloseable {
    protected abstract val context: GLContext

    protected fun bindAttribute(attr: Int, varName: String) {
        GL20.glBindAttribLocation(this.id, attr, varName)
    }

    protected fun getUniformLocation(uniformName: String): Int {
        return GL20.glGetUniformLocation(this.id, uniformName)
    }

    protected fun loadFloat(location: Int, value: Float) {
        GL20.glUniform1f(location, value)
    }

    protected fun loadVector(location: Int, x: Float, y: Float, z: Float) {
        GL20.glUniform3f(location, x, y, z)
    }

    protected fun loadBool(location: Int, value: Boolean) {
        GL20.glUniform1i(location, if (value) GL11.GL_TRUE else GL11.GL_FALSE)
    }

    protected fun loadMatrix(location: Int, matrix: Matrix4f) {
        buffer.put(matrix.matrix, 0, matrix.matrix.size)
        buffer.rewind()
        GL20.glUniformMatrix4fv(location, false, buffer)
    }

    override fun bind() {
        GL20.glUseProgram(this.id)
        this.context.shaderPrograms.activeProgram = this
    }

    override fun close() = GL20.glDeleteProgram(this.id)

    abstract class Builder {
        protected val vertexShaders = ArrayList<VertexShader>()
        protected val fragmentShaders = ArrayList<FragmentShader>()

        abstract fun build(context: GLContext): ShaderProgram
    }
}
