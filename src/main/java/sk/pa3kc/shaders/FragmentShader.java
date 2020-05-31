package sk.pa3kc.shaders;

import java.io.File;

public class FragmentShader extends Shader {
    public FragmentShader(File file) {
        loadShader(file);
    }

    @Override
    public void loadShader(File file) {
        try {
            super.loadShader(file, Shader.Type.GL_FRAGMENT_SHADER);
        } catch (Throwable ex) {
            ex.printStackTrace();
            System.exit(-1);
        }
    }
}
