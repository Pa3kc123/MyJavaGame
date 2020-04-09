package sk.pa3kc;

import static org.lwjgl.system.MemoryUtil.NULL;

import java.awt.GraphicsConfiguration;
import java.awt.GraphicsDevice;
import java.awt.GraphicsEnvironment;
import java.awt.Rectangle;
import java.io.File;
import java.io.InputStream;
import java.util.ArrayList;
import java.util.Arrays;

import org.lwjgl.glfw.GLFWErrorCallback;
import org.lwjgl.opengl.GL;

import sk.pa3kc.pojo.Model;
import sk.pa3kc.pojo.Texture;
import sk.pa3kc.ui.GLWindow;

import static org.lwjgl.glfw.GLFW.*;
import static org.lwjgl.opengl.GL21.*;

public class App {
    public static final String TEXTURES_BLOCKS = "textures/blocks";

    public Texture[] textures = null;

    public int textureIndex = 0;

    public final GLWindow window;

    private App(String... args) {
        final float[] vertecies = new float[] {
            -0.5f, -0.5f, 0f,
            0.5f, -0.5f, 0f,
            -0.5f, 0.5f, 0f,

            -0.5f, 0.5f, 0f,
            0.5f, -0.5f, 0f,
            0.5f, 0.5f, 0f
        };

        final int[] texture = new int[] {
            0, 1,
            1, 1,
            0, 0,

            0, 0,
            1, 1,
            1, 0
        };

        final GraphicsEnvironment env = GraphicsEnvironment.getLocalGraphicsEnvironment();
        final GraphicsDevice screen = env.getDefaultScreenDevice();
        final GraphicsConfiguration config = screen.getDefaultConfiguration();
        final Rectangle bounds = config.getBounds();

        this.window = new GLWindow(bounds.width, bounds.height, "My Game");

        this.window.setKeyCallback((window, key, scancode, action, mods) -> {
            if (action == GLFW_RELEASE) {
                switch (key) {
                    case GLFW_KEY_ESCAPE: glfwSetWindowShouldClose(window, true); break;
                    case GLFW_KEY_A:
                        if (this.textures == null) break;
                        final int tmp = this.textureIndex+1;
                        this.textureIndex = tmp > this.textures.length ? 0 : tmp;
                    break;
                }
            }
        });

        this.window.show();

        final Model model = new Model(vertecies, texture);

        this.textures = loadTextures();

        this.window.setBackgroundColor(0f, 0f, 0f);

    }

    private Texture[] loadTextures() {
        final InputStream in = ClassLoader.getSystemClassLoader().getResourceAsStream(TEXTURES_BLOCKS);

        if (in == null) {
            throw new RuntimeException("Unable to open " + TEXTURES_BLOCKS);
        }

        byte[] b = null;
        try {
            b = new byte[in.available()];
            in.read(b);
        } catch (Throwable ex) {
            ex.printStackTrace();
            return null;
        }

        final String[] s = new String(b).split("\n");

        ArrayList<String> paths = new ArrayList<String>(Arrays.asList(s));
        ArrayList<File> textureFiles = new ArrayList<>();
        Texture[] textures = null;

        for (String path : paths) {
            final File file = new File(TEXTURES_BLOCKS, path);

            if (file != null && file.exists() && file.getName().endsWith(".png")) {
                textureFiles.add(file);
            }
        }

        textures = new Texture[textureFiles.size()];

        for (int i = 0; i < textureFiles.size(); i++) {
            textures[i] = new Texture(textureFiles.get(i));
        }

        return textures;
    }

    public static void main(String[] args) {
        new App();
    }
}
