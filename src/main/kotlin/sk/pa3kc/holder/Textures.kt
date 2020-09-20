package sk.pa3kc.holder

import java.io.File
import java.io.FileNotFoundException
import java.io.IOException
import java.nio.ByteBuffer
import javax.imageio.ImageIO
import org.lwjgl.BufferUtils
import org.lwjgl.opengl.GL11
import sk.pa3kc.poko.Texture

private const val TEXTURES: String = "./textures"
private const val BLOCKS: String = "$TEXTURES/blocks"

object Textures : ArrayList<Texture>(), AutoCloseable {
    override fun close() = super.forEach {
        it.close()
    }
}

@Throws(IOException::class, IllegalStateException::class)
fun loadTexture(path: String): Texture {
    if (path.isBlank()) {
        throw IllegalArgumentException("name cannot be blank")
    }

    return loadTexture(File(path))
}

@Throws(IOException::class)
fun loadTexture(file: File): Texture {
    if (!file.exists()) {
        throw FileNotFoundException("${file.path} was not found")
    }

    val path = "$BLOCKS/${file.path}"
    val bufferedImage = ClassLoader.getSystemClassLoader().getResourceAsStream(path)?.use {
        try {
            ImageIO.read(it)
        } catch (e: Exception) {
            e.printStackTrace()
            throw e
        }
    } ?: throw FileNotFoundException("Unable to open $path")

    val width = bufferedImage.width
    val height = bufferedImage.height

    val rawPixels = bufferedImage.getRGB(0, 0, width, height, null, 0, width)
    val pixelBuffer = BufferUtils.createByteBuffer(width * height * 4).apply {
        fun ByteBuffer.put(value: Int) = this.put(value.toByte())

        rawPixels.forEach {
            //? BufferedImage returns ARGB, but OpenGL wants RGBA
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
        Textures += it
    }
}
