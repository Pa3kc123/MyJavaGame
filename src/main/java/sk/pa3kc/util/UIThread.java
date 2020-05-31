package sk.pa3kc.util;

import org.lwjgl.glfw.GLFW;
import org.lwjgl.opengl.GL;
import org.lwjgl.opengl.GL11;
import org.lwjgl.opengl.GL13;
import org.lwjgl.opengl.GL20;
import org.lwjgl.opengl.GL30;
import org.lwjgl.opengl.GLCapabilities;

import sk.pa3kc.App;
import sk.pa3kc.entities.Entity;
import sk.pa3kc.pojo.RawModel;
import sk.pa3kc.pojo.TexturedModel;
import sk.pa3kc.pojo.matrix.Matrix4f;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

import static org.lwjgl.glfw.GLFW.*;
import static org.lwjgl.system.MemoryUtil.NULL;

public class UIThread implements Runnable {
    private enum ThreadState {
        STARTING,
        RUNNING,
        PAUSING,
        PAUSED,
        STOPPING,
        STOPPED
    }

    private final ArrayList<Entity> models = new ArrayList<>();

    private final GLCapabilities capabilities;
    private final Thread thread;
    private final long windowId;

    private ThreadState state = ThreadState.STOPPED;

    public UIThread(final long windowId, final GLCapabilities capabilities) {
        this.thread = new Thread(this);
        this.windowId = windowId;
        this.capabilities = capabilities;
    }

    public boolean add(Entity model) {
        return this.models.add(model);
    }
    public boolean addAll(Entity... models) {
        return this.models.addAll((List<Entity>)Arrays.asList(models));
    }

    public boolean remove(Entity model) {
        return this.models.remove(model);
    }
    public boolean remoteAll(Entity... models) {
        return this.models.removeAll((List<Entity>)Arrays.asList(models));
    }

    public void start() {
        this.state = ThreadState.STARTING;
        this.thread.start();
    }

    public void stop() {
        this.state = ThreadState.STOPPING;
    }

    public void pause() {
        this.state = ThreadState.PAUSING;
    }

    @Override
    public void run() {
        glfwMakeContextCurrent(this.windowId);

        GL.setCapabilities(this.capabilities);

        while (!glfwWindowShouldClose(this.windowId) && this.state != ThreadState.STOPPING) {
            GL11.glEnable(GL11.GL_DEPTH_TEST);
            GL11.glClear(GL11.GL_COLOR_BUFFER_BIT | GL11.GL_DEPTH_BUFFER_BIT);

            glfwPollEvents();

            if (App.KEYBOARD.isKeyPressed(GLFW.GLFW_KEY_ESCAPE)) {
                glfwSetWindowShouldClose(this.windowId, true);
            }

            App.CAMERA.move();
            App.CAMERA.rotate();

            App.SHADER_PROGRAM.start();

            for (final Entity entity : this.models) {
                final TexturedModel model = entity.getModel();
                final RawModel rawModel = model.getRawModel();

                GL30.glBindVertexArray(rawModel.getVaoId());
                GL20.glEnableVertexAttribArray(Loader.VERTICIES_ATTR_ID);
                GL20.glEnableVertexAttribArray(Loader.TEXTURE_COORDS_ATTR_ID);
                GL20.glEnableVertexAttribArray(Loader.NORMALS_ATTR_ID);

                App.SHADER_PROGRAM.loadLight(App.LIGHT);
                entity.rotate(0f, 0.5f, 0f);
                // entity.move(0f, 0f, -0.2f);

                final Matrix4f transformationMatrix = MatrixMath.createTransformationMatrix(
                    entity.getPosition(),
                    entity.getRotX(),
                    entity.getRotY(),
                    entity.getRotZ(),
                    entity.getScale()
                );
                App.SHADER_PROGRAM.loadTransformationMatrix(transformationMatrix);
                App.SHADER_PROGRAM.loadViewMatrix(App.CAMERA);

                GL13.glActiveTexture(GL13.GL_TEXTURE0);
                GL11.glBindTexture(GL11.GL_TEXTURE_2D, model.getTexture().getTextureId());
                GL11.glDrawElements(GL11.GL_TRIANGLES, rawModel.getVertexCount(), GL11.GL_UNSIGNED_INT, 0);

                GL20.glDisableVertexAttribArray(Loader.VERTICIES_ATTR_ID);
                GL20.glDisableVertexAttribArray(Loader.TEXTURE_COORDS_ATTR_ID);
                GL20.glDisableVertexAttribArray(Loader.NORMALS_ATTR_ID);
                GL30.glBindVertexArray(0);
            }

            App.SHADER_PROGRAM.stop();

            glfwSwapBuffers(this.windowId);
        }

        glfwMakeContextCurrent(NULL);
    }
}
