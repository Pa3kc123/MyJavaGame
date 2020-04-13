package sk.pa3kc.shaders;

import java.io.File;

public class VertexShader extends AShader {
    public VertexShader(File file) {
        loadShader(file);
    }

    @Override
    public void loadShader(File file) {
        try {
            super.loadShader(file, AShader.Type.GL_VERTEX_SHADER);
        } catch (Throwable ex) {
            ex.printStackTrace();
            System.exit(-1);
        }
    }
}
