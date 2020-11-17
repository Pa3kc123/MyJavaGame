package sk.pa3kc.poko.shader

import org.lwjgl.opengl.GL20
import java.io.FileNotFoundException

data class VertexShader(
    override val shaderId: Int
) : Shader(shaderId) {
    companion object {
        @JvmStatic
        @Throws(FileNotFoundException::class)
        fun newVertexShader(path: String) = VertexShader(newShader(path, GL20.GL_VERTEX_SHADER))
    }

    override fun toString() = "VertexShader[shaderId=$shaderId]"
}
