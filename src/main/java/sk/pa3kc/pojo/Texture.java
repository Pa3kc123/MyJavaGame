package sk.pa3kc.pojo;

import static  org.lwjgl.opengl.GL11.*;

public class Texture {
    private final int textureId;

    public Texture(int textureId) {
        this.textureId = textureId;
    }

    public int getTextureId() {
        return this.textureId;
    }

    public void bind() {
        glBindTexture(GL_TEXTURE_2D, this.textureId);
    }
}
