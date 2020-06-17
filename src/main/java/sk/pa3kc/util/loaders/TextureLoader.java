package sk.pa3kc.util.loaders;

import java.awt.image.BufferedImage;
import java.io.InputStream;
import java.nio.ByteBuffer;
import java.util.ArrayList;
import java.util.Objects;
import java.util.logging.Level;
import java.util.logging.Logger;

import javax.imageio.ImageIO;

import org.lwjgl.BufferUtils;
import org.lwjgl.opengl.GL11;

import sk.pa3kc.mylibrary.utils.StringUtils;
import sk.pa3kc.pojo.Texture;

public class TextureLoader implements AutoCloseable {
    public static final String TEXTURES = "./textures";
    public static final String BLOCKS = TEXTURES.concat("/blocks");

    private ArrayList<Integer> textureIds = new ArrayList<>();

    public Texture loadBlockTexture(String name) {
        if (name == null || name.length() == 0) return null;

        final String path = StringUtils.build(BLOCKS, '/', name);

        final InputStream is = ClassLoader.getSystemClassLoader().getResourceAsStream(path);

        Objects.requireNonNull(is, "Unable to open " + path);

        final BufferedImage bufferedImage;
        try {
            bufferedImage = ImageIO.read(is);
        } catch (Exception ex) {
            Logger.getGlobal().log(Level.SEVERE, "Exception occured while reading through texture file", ex);
            return null;
        }

        final int width = bufferedImage.getWidth();
        final int height = bufferedImage.getHeight();

        final int[] raw_pixels = bufferedImage.getRGB(0, 0, width, height, null, 0, width);
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

        final int textureId = GL11.glGenTextures();
        this.textureIds.add(textureId);

        GL11.glBindTexture(GL11.GL_TEXTURE_2D, textureId);
        GL11.glTexParameteri(GL11.GL_TEXTURE_2D, GL11.GL_TEXTURE_MIN_FILTER, GL11.GL_NEAREST);
        GL11.glTexParameteri(GL11.GL_TEXTURE_2D, GL11.GL_TEXTURE_MAG_FILTER, GL11.GL_NEAREST);
        GL11.glTexImage2D(GL11.GL_TEXTURE_2D, 0, GL11.GL_RGBA, width, height, 0, GL11.GL_RGBA, GL11.GL_UNSIGNED_BYTE, pixelBuffer);

        return new Texture(textureId);
    }

    @Override
    public void close() {
        for (final int textureid : this.textureIds) {
            GL11.glDeleteTextures(textureid);
        }
    }
}
