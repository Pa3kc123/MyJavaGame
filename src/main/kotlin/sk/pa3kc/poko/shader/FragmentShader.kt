package sk.pa3kc.poko.shader

import org.lwjgl.opengl.GL20
import java.io.FileNotFoundException

data class FragmentShader(
    override val shaderId: Int
) : Shader(shaderId) {
    companion object {
        @JvmStatic
        @Throws(FileNotFoundException::class)
        fun newFragmentShader(path: String) = FragmentShader(newShader(path, GL20.GL_FRAGMENT_SHADER))
    }

    override fun toString() = "FragmentShader[shaderId=${super.shaderId}]"
}
