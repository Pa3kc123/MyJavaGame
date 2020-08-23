package sk.pa3kc.poko

import org.lwjgl.opengl.GL20

data class Shader(
    val shaderType: Int,
    val shaderId: Int
) : AutoCloseable {
    override fun close() {
        GL20.glDeleteShader(this.shaderId)
    }
}
