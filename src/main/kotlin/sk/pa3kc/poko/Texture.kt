package sk.pa3kc.poko

import org.lwjgl.opengl.GL11

data class Texture(
    val textureId: Int
) : AutoCloseable {
    fun bind() = GL11.glBindTexture(GL11.GL_TEXTURE_2D, this.textureId)

    override fun close() {
        GL11.glDeleteTextures(this.textureId)
    }
}
