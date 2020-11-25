package sk.pa3kc.util

import org.lwjgl.glfw.GLFW.*
import org.lwjgl.opengl.*
import org.lwjgl.system.MemoryUtil.NULL
import sk.pa3kc.App
import sk.pa3kc.App.CAMERA
import sk.pa3kc.App.KEYBOARD
import sk.pa3kc.App.LIGHT
import sk.pa3kc.entity.Entity
import sk.pa3kc.holder.NORMALS
import sk.pa3kc.holder.TEXTURE_COORDS
import sk.pa3kc.holder.VERTICES
import sk.pa3kc.mylibrary.matrix.math.MatrixMath
import sk.pa3kc.poko.program.StaticShaderProgram

class UIThread(
    private val windowId: Long,
    private val capabilities: GLCapabilities
) : Runnable {
    private enum class ThreadState {
        STARTING,
        RUNNING,
        PAUSING,
        PAUSED,
        STOPPING,
        STOPPED
    }

    private val models = mutableListOf<Entity>()

    private val thread = Thread(this)

    private var state = ThreadState.STOPPED

    operator fun plusAssign(model: Entity) {
        this.add(model)
    }
    operator fun minusAssign(model: Entity) {
        this.remove(model)
    }

    fun add(model: Entity): Boolean = this.models.add(model)
    fun addAll(vararg models: Entity) = this.models.addAll(models)

    fun remove(model: Entity) = this.models.remove(model)
    fun removeAll(vararg models: Entity) = this.models.removeAll(models)

    fun start() {
        this.state = ThreadState.STARTING
        this.thread.start()
    }

    fun stop() {
        this.state = ThreadState.STOPPING
    }

    fun pause() {
        this.state = ThreadState.PAUSING
    }

    override fun run() {
        glfwMakeContextCurrent(this.windowId)

        GL.setCapabilities(this.capabilities)

        this.state = ThreadState.RUNNING
        while (!glfwWindowShouldClose(this.windowId) && this.state != ThreadState.STOPPING) {
            GL11.glEnable(GL11.GL_DEPTH_TEST)
            GL11.glClear(GL11.GL_COLOR_BUFFER_BIT or GL11.GL_DEPTH_BUFFER_BIT)

            glfwPollEvents()

            if (KEYBOARD.isKeyPressed(GLFW_KEY_ESCAPE)) {
                glfwSetWindowShouldClose(this.windowId, true)
            }

            CAMERA.move()
            CAMERA.rotate()
            App.context.shaderPrograms[0].bind()
            val shaderProgram = App.context.shaderPrograms.activeProgram as StaticShaderProgram

            this.models.forEach { entity ->
                val model = entity.model
                val rawModel = model.rawModel

                GL30.glBindVertexArray(rawModel.vaoId)
                GL20.glEnableVertexAttribArray(VERTICES)
                GL20.glEnableVertexAttribArray(TEXTURE_COORDS)
                GL20.glEnableVertexAttribArray(NORMALS)

                shaderProgram.loadLight(LIGHT)
                entity.rotate(0f, 0.5f, 0f)
                // entity.move(0f, 0f, -0.2f)

                val transformationMatrix = MatrixMath.createTransformationMatrix(
                    entity.position,
                    entity.rotX,
                    entity.rotY,
                    entity.rotZ,
                    entity.scale
                )
                shaderProgram.loadTransformationMatrix(transformationMatrix)
                shaderProgram.loadViewMatrix(CAMERA)

                GL13.glActiveTexture(GL13.GL_TEXTURE0)
                GL11.glBindTexture(GL11.GL_TEXTURE_2D, model.texture.textureId)
                GL11.glDrawElements(GL11.GL_TRIANGLES, rawModel.vertexCount, GL11.GL_UNSIGNED_INT, 0)

                GL20.glDisableVertexAttribArray(VERTICES)
                GL20.glDisableVertexAttribArray(TEXTURE_COORDS)
                GL20.glDisableVertexAttribArray(NORMALS)
                GL30.glBindVertexArray(0)
            }

            App.context.shaderPrograms.unbind()

            glfwSwapBuffers(this.windowId)
        }

        glfwMakeContextCurrent(NULL)
    }
}
