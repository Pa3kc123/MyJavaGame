package sk.pa3kc.pojo;

public class TexturedModel {
    private final RawModel rawModel;
    private final Texture texture;

    public TexturedModel(RawModel rawModel, Texture texture) {
        this.rawModel = rawModel;
        this.texture = texture;
    }

    public RawModel getRawModel() {
        return this.rawModel;
    }
    public Texture getTexture() {
        return this.texture;
    }
}
