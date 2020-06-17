package sk.pa3kc;

import java.awt.GraphicsConfiguration;
import java.awt.GraphicsDevice;
import java.awt.GraphicsEnvironment;
import java.awt.Rectangle;
import java.io.File;
import java.util.logging.Level;
import java.util.logging.Logger;

import sk.pa3kc.entities.Camera;
import sk.pa3kc.entities.Entity;
import sk.pa3kc.entities.Light;
import sk.pa3kc.pojo.RawModel;
import sk.pa3kc.pojo.Texture;
import sk.pa3kc.pojo.TexturedModel;
import sk.pa3kc.mylibrary.matrix.pojo.Matrix4f;
import sk.pa3kc.mylibrary.matrix.pojo.Vector3f;
import sk.pa3kc.mylibrary.obj.ObjLoader;
import sk.pa3kc.mylibrary.obj.ObjObject;
import sk.pa3kc.shaders.FragmentShader;
import sk.pa3kc.shaders.StaticShaderProgram;
import sk.pa3kc.shaders.VertexShader;
import sk.pa3kc.ui.GLWindow;
import sk.pa3kc.ui.calls.KeyCallback;
import sk.pa3kc.util.Loader;
import sk.pa3kc.util.loaders.TextureLoader;

import static org.lwjgl.glfw.GLFW.*;
import static org.lwjgl.system.MemoryUtil.NULL;

public class App {
    public static final String PATH_SHADERS_VERTEX = "shaders/vertex";
    public static final String PATH_SHADERS_FRAGMENT = "shaders/fragment";

    public static final KeyCallback KEYBOARD = new KeyCallback();
    public static final float FOV = 70f;
    public static final float NEAR_PLANE = 0.1f;
    public static final float FAR_PLANE = 1000f;

    public static final Loader LOADER = new Loader();
    public static final TextureLoader TEXTURE_LOADER = new TextureLoader();
    public static final Camera CAMERA = new Camera();
    public static final Light LIGHT = new Light(
        new Vector3f(0f, 0f, -55f),
        new Vector3f(1f, 1f, 1f)
    );

    public static VertexShader VERTEX_SHADER;
    public static FragmentShader FRAGMENT_SHADER;
    public static StaticShaderProgram SHADER_PROGRAM;

    public final GLWindow window;

    private App(String[] args) {
        final GraphicsEnvironment env = GraphicsEnvironment.getLocalGraphicsEnvironment();
        final GraphicsDevice screen = env.getDefaultScreenDevice();
        final GraphicsConfiguration config = screen.getDefaultConfiguration();
        final Rectangle bounds = config.getBounds();

        this.window = new GLWindow(bounds.width, bounds.height, "My Game") {
            @Override
            public void close() {
                App.LOADER.close();
                App.SHADER_PROGRAM.close();
                super.close();
            }
        };

        this.window.setKeyCallback(App.KEYBOARD);

        glfwMakeContextCurrent(this.window.getWindowId());

        App.VERTEX_SHADER = new VertexShader(new File(PATH_SHADERS_VERTEX, "1.mvs"));
        App.FRAGMENT_SHADER = new FragmentShader(new File(PATH_SHADERS_FRAGMENT, "1.mfs"));
        App.SHADER_PROGRAM = new StaticShaderProgram(VERTEX_SHADER, FRAGMENT_SHADER);

        final Matrix4f projectionMatrix = Matrix4f.projectionMatrix(bounds.width, bounds.height, FOV, NEAR_PLANE, FAR_PLANE);
        App.SHADER_PROGRAM.start();
        App.SHADER_PROGRAM.loadProjectionMatrix(projectionMatrix);
        App.SHADER_PROGRAM.stop();

        RawModel model = null;
        ObjObject object = null;
        try {
            object = ObjLoader.loadObjModel(args[0]);
            // model = App.LOADER.loadModelToVAO(
            //     object.getVerticies(),
            //     object.getVertexTextures(),
            //     object.getVertexNormals(),
            //     object.getFaces()
            // );
        } catch (Exception ex) {
            Logger.getGlobal().log(Level.SEVERE, "Exception occured while loading model to VAO", ex);
        }
        final Texture texture = App.TEXTURE_LOADER.loadBlockTexture("crafting_table_top.png");

        glfwMakeContextCurrent(NULL);

        final TexturedModel texturedModel = new TexturedModel(model, texture);
        final Entity entity = new Entity(texturedModel, new Vector3f(0, 0, -100), 0, 0, 0, 1);

        this.window.setBackgroundColor(1f, 0f, 0f);
        this.window.show();

        this.window.uiThread.add(entity);
    }

    public static void main(String[] args) {
        if (args.length == 0) return;
        new App(args);
    }
}
