package sk.pa3kc.holder

import org.lwjgl.BufferUtils
import org.lwjgl.opengl.GL20
import sk.pa3kc.shader.program.ShaderProgram
import java.nio.FloatBuffer

@JvmField val buffer: FloatBuffer = BufferUtils.createFloatBuffer(16) // 4x4 matrix

object ShaderPrograms : ArrayList<ShaderProgram>(), AutoCloseable {
    fun useProgram(shaderProgram: ShaderProgram) {
        GL20.glUseProgram(shaderProgram.programId)
    }

    override fun close() {
        super.forEach {
            it.close()
        }
    }
}
