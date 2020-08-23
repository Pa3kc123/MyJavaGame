package sk.pa3kc.poko

import org.lwjgl.opengl.GL20

data class ShaderProgramInfo(
        val vertexShader: Shader,
        val fragmentShader: Shader,
        val programId: Int
) : AutoCloseable {
    override fun close() {
        GL20.glDeleteProgram(this.programId)
    }
}
