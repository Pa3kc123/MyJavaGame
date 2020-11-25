package sk.pa3kc.holder

import org.lwjgl.BufferUtils
import org.lwjgl.opengl.GL11
import sk.pa3kc.ex.GLTextureException
import sk.pa3kc.poko.Texture
import sk.pa3kc.util.GLCollection
import sk.pa3kc.util.validateAsFile
import java.awt.image.BufferedImage
import java.io.File
import java.io.IOException
import javax.imageio.ImageIO

object Textures : GLCollection<Texture>() {
    @JvmStatic
    @Throws(GLTextureException::class)
    fun loadTexture(path: String): Texture {
        val file = File(path).validateAsFile()

        val image: BufferedImage
        try {
            image = ImageIO.read(file)
        } catch (e: IOException) {
            throw GLTextureException("Error occurred while reading $path", e)
        }

        val textureId = with(image) {
            val rawPixels = image.getRGB(0, 0, width, height, null, 0, width)
            val pixelBuffer = BufferUtils.createByteBuffer(width * height * 4).apply {
                rawPixels.forEach {
                    //? image returns ARGB, but OpenGL wants RGBA
                    put((it shr 16 and 0xFF).toByte())   // R
                    put((it shr 8 and 0xFF).toByte())    // G
                    put((it shr 0 and 0xFF).toByte())    // B
                    put((it shr 24 and 0xFF).toByte())   // A
                }

                rewind()
            }

            GL11.glGenTextures().also {
                GL11.glBindTexture(GL11.GL_TEXTURE_2D, it)
                GL11.glTexParameteri(GL11.GL_TEXTURE_2D, GL11.GL_TEXTURE_MIN_FILTER, GL11.GL_NEAREST)
                GL11.glTexParameteri(GL11.GL_TEXTURE_2D, GL11.GL_TEXTURE_MAG_FILTER, GL11.GL_NEAREST)
                GL11.glTexImage2D(GL11.GL_TEXTURE_2D, 0, GL11.GL_RGBA8, width, height, 0, GL11.GL_RGBA, GL11.GL_UNSIGNED_BYTE, pixelBuffer)
            }
        }

        return Texture(textureId).also {
            Textures.add(it)
        }
    }

    override fun unbind() {
        GL11.glBindTexture(GL11.GL_TEXTURE_2D, 0)
    }
}
