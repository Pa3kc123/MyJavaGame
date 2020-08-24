package sk.pa3kc.poko

import org.lwjgl.opengl.GL20

data class ShaderProgram(
    private val vertexShaders: ArrayList<Shader> = ArrayList(),
    private val fragmentShaders: ArrayList<Shader> = ArrayList(),
    val programId: Int
) : AutoCloseable {
    var isRunning = false
        set(value) {
            if (value == field) return
            field = value

            if (field) {
                GL20.glUseProgram(this.programId)
            } else {
                GL20.glUseProgram(0)
            }
        }

    override fun close() {
        if (isRunning) {
            isRunning = false
        }

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
}
