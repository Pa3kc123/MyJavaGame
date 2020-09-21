package sk.pa3kc.holder

import org.lwjgl.BufferUtils
import org.lwjgl.opengl.GL20
import java.nio.FloatBuffer
import sk.pa3kc.poko.ShaderProgram

@JvmField val buffer: FloatBuffer = BufferUtils.createFloatBuffer(16) // 4x4 matrix

object ShaderPrograms : ArrayList<ShaderProgram>(), AutoCloseable {
    var hasActiveProgram = false
        private set

    fun useProgram(shaderProgram: ShaderProgram) {
        GL20.glUseProgram(shaderProgram.programId)
        this.hasActiveProgram = true
    }

    fun deactivatePrograms() {
        GL20.glUseProgram(0)
        this.hasActiveProgram = false
    }

    override fun close() = super.forEach {
        it.close()
    }
}
