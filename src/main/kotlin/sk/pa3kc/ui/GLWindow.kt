package sk.pa3kc.ui

import org.lwjgl.opengl.GL
import org.lwjgl.opengl.GLCapabilities
import org.lwjgl.glfw.GLFWErrorCallback
import org.lwjgl.glfw.GLFWErrorCallbackI
import org.lwjgl.glfw.GLFWKeyCallback
import org.lwjgl.glfw.GLFWKeyCallbackI
import org.lwjgl.glfw.GLFWWindowCloseCallback
import org.lwjgl.glfw.GLFWWindowCloseCallbackI

import sk.pa3kc.util.UIThread

import org.lwjgl.glfw.GLFW.*
import org.lwjgl.opengl.GL11.*
import org.lwjgl.system.MemoryUtil.NULL

open class GLWindow(
    width: Int,
    height: Int,
    title: CharSequence,
    private val closeBlock: () -> Unit
) : AutoCloseable {
    private val capabilities: GLCapabilities
    private val errCallback: GLFWErrorCallbackI
    private val windowCloseCallback: GLFWWindowCloseCallbackI

    val windowId: Long
    var keyCallback: GLFWKeyCallbackI? = null
        set(value) {
            field = value
            glfwSetKeyCallback(this.windowId, field)
        }
    val uiThread: UIThread

    init {
        // Create callbacks
        this.errCallback = GLFWErrorCallback.createPrint(System.err)
        this.windowCloseCallback = GLFWWindowCloseCallback.create {
            this.close()
        }

        // Initialize GLFW
        if (!glfwInit()) {
            glfwTerminate()
            throw IllegalStateException("Unable to initialize GLFW")
        }

        // Setup window properties
        glfwDefaultWindowHints()
        glfwWindowHint(GLFW_RESIZABLE, GLFW_FALSE)
        glfwWindowHint(GLFW_VISIBLE, GLFW_FALSE)
        glfwWindowHint(GLFW_DECORATED, GLFW_FALSE)

        // Create OpenGL window
        this.windowId = glfwCreateWindow(width, height, title, NULL, NULL)
        if (this.windowId == NULL) {
            glfwTerminate()
            throw IllegalStateException("Unable to create GLFW window")
        }

        // Setup window position to upper left corner (Windowed fullscreen)
        glfwSetWindowPos(this.windowId, 0, 0)

        // Setup callbacks
        glfwSetErrorCallback(this.errCallback)
        glfwSetWindowCloseCallback(this.windowId, this.windowCloseCallback)

        glfwMakeContextCurrent(this.windowId)

        glfwSwapInterval(0)

        this.capabilities = GL.createCapabilities()

        glEnable(GL_TEXTURE_2D)
        glEnable(GL_CULL_FACE)
        glCullFace(GL_BACK)

        glfwMakeContextCurrent(NULL)

        // Create UIThread for refreshing game logic and rendering loop
        this.uiThread = UIThread(this.windowId, this.capabilities)
    }

    fun setBackgroundColor(r: Float, g: Float, b: Float) = this.setBackgroundColor(r, g, b, 1f)
    fun setBackgroundColor(r: Float, g: Float, b: Float, a: Float) = glClearColor(r, g, b, a)

    fun show() {
        glfwShowWindow(this.windowId)
        this.uiThread.start()
    }

    fun hide() {
        glfwHideWindow(this.windowId)
    }

    override fun close() {
        this.closeBlock()
        this.uiThread.stop()
        GLFWErrorCallback.free(this.errCallback.address())
        GLFWWindowCloseCallback.free(this.windowCloseCallback.address())
        this.keyCallback?.let {
            GLFWKeyCallback.free(it.address())
        }
        glfwDestroyWindow(this.windowId)
        glfwTerminate()
    }
}
