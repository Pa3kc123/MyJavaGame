package sk.pa3kc.pojo;

public class RawModel {
    private int vaoId;
    private int vertexCount;

    public RawModel(int vaoId, int vertexCount) {
        this.vaoId = vaoId;
        this.vertexCount = vertexCount;
    }

    public int getVaoId() {
        return this.vaoId;
    }

    public int getVertexCount() {
        return this.vertexCount;
    }
}
