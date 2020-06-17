package sk.pa3kc.shaders;

import java.io.File;
import java.util.logging.Level;
import java.util.logging.Logger;

public class FragmentShader extends Shader {
    public FragmentShader(File file) {
        loadShader(file);
    }

    @Override
    public void loadShader(File file) {
        try {
            super.loadShader(file, Shader.Type.GL_FRAGMENT_SHADER);
        } catch (Exception ex) {
            Logger.getGlobal().log(Level.SEVERE, "Exception occured while loading Fragment shader", ex);
            System.exit(-1);
        }
    }
}
