package sk.pa3kc.holder

import org.lwjgl.BufferUtils
import org.lwjgl.opengl.GL11
import sk.pa3kc.ex.GLTextureException
import sk.pa3kc.poko.Texture
import sk.pa3kc.util.GLCollection
import java.awt.image.BufferedImage
import java.io.File
import java.io.IOException
import java.nio.ByteBuffer
import javax.imageio.ImageIO

object Textures : GLCollection<Texture>() {
    @JvmStatic
    @Throws(GLTextureException::class)
    fun loadTexture(path: String): Texture {
        val file = File(path)

        if (!file.exists()) {
            throw GLTextureException("$path does not exists")
        }

        if (file.isDirectory) {
            throw GLTextureException("$path is directory")
        }

        val image: BufferedImage
        try {
            image = ImageIO.read(file)
        } catch (e: IOException) {
            throw GLTextureException("Error occurred while reading $path", e)
        }

        val width = image.width
        val height = image.height

        val rawPixels = image.getRGB(0, 0, width, height, null, 0, width)
        val pixelBuffer = BufferUtils.createByteBuffer(width * height * 4).apply {
            fun ByteBuffer.put(value: Int) = this.put(value.toByte())

            rawPixels.forEach {
                //? image returns ARGB, but OpenGL wants RGBA
                put(it shr 16 and 0xFF)   // R
                put(it shr 8 and 0xFF)    // G
                put(it shr 0 and 0xFF)    // B
                put(it shr 24 and 0xFF)   // A
            }

            flip()
        }

        val textureId = GL11.glGenTextures().also {
            GL11.glBindTexture(GL11.GL_TEXTURE_2D, it)
            GL11.glTexParameteri(GL11.GL_TEXTURE_2D, GL11.GL_TEXTURE_MIN_FILTER, GL11.GL_NEAREST)
            GL11.glTexParameteri(GL11.GL_TEXTURE_2D, GL11.GL_TEXTURE_MAG_FILTER, GL11.GL_NEAREST)
            GL11.glTexImage2D(GL11.GL_TEXTURE_2D, 0, GL11.GL_RGBA, width, height, 0, GL11.GL_RGBA, GL11.GL_UNSIGNED_BYTE, pixelBuffer)
        }

        return Texture(textureId).also {
            Textures.add(it)
        }
    }
}
