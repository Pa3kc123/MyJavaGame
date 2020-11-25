package sk.pa3kc.poko

import org.lwjgl.opengl.GL11
import sk.pa3kc.util.GLBindable

data class Texture(
    val id: Int
) : GLBindable, AutoCloseable {
    override fun bind() = GL11.glBindTexture(GL11.GL_TEXTURE_2D, this.id)

    override fun close() {
        GL11.glDeleteTextures(this.id)
    }
}
