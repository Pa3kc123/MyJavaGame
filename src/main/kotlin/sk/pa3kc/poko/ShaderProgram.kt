package sk.pa3kc.poko

import org.lwjgl.opengl.GL20

data class ShaderProgram(
    val programId: Int
) : AutoCloseable {
    private val vertexShaders = ArrayList<VertexShader>()
    private val fragmentShaders = ArrayList<FragmentShader>()

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
