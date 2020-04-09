package sk.pa3kc.pojo;

import java.awt.image.BufferedImage;
import java.io.File;
import java.nio.ByteBuffer;

import javax.imageio.ImageIO;

import static  org.lwjgl.opengl.GL11.*;
import org.lwjgl.BufferUtils;

public class Texture {
    private final int id;

    public Texture(final File texturePath) {
        BufferedImage bi = null;

        try {
            bi = ImageIO.read(texturePath);
        } catch (Throwable ex) {
            ex.printStackTrace();
            id = -1;
            return;
        }

        final int width = bi.getWidth();
        final int height = bi.getHeight();

        final int[] raw_pixels = bi.getRGB(0, 0, width, height, null, 0, width);
        final ByteBuffer pixelBuffer = BufferUtils.createByteBuffer(width * height * 4);

        for (int i = 0; i < raw_pixels.length; i++) {
            final int pixel = raw_pixels[i];

            // BufferedImage returns ARGB
            final byte a = (byte)((pixel >> 24) & 0xFF);
            final byte r = (byte)((pixel >> 16) & 0xFF);
            final byte g = (byte)((pixel >> 8) & 0xFF);
            final byte b = (byte)((pixel >> 0) & 0xFF);

            // OpenGL wants RGBA
            pixelBuffer.put(r).put(g).put(b).put(a);
        }

        pixelBuffer.flip();

        this.id = glGenTextures();

        glBindTexture(GL_TEXTURE_2D, this.id);
        glTexParameteri(GL_TEXTURE_2D, GL_TEXTURE_MIN_FILTER, GL_NEAREST);
        glTexParameteri(GL_TEXTURE_2D, GL_TEXTURE_MAG_FILTER, GL_NEAREST);
        glTexImage2D(GL_TEXTURE_2D, 0, GL_RGBA, width, height, 0, GL_RGBA, GL_UNSIGNED_BYTE, pixelBuffer);
    }

    public void bind() {
        glBindTexture(GL_TEXTURE_2D, this.id);
    }
}
