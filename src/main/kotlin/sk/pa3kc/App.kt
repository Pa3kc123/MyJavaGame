package sk.pa3kc

import org.lwjgl.glfw.GLFW
import org.lwjgl.glfw.GLFWErrorCallback
import org.lwjgl.glfw.GLFWErrorCallbackI
import org.lwjgl.glfw.GLFWKeyCallback
import org.lwjgl.opengl.GL
import org.lwjgl.opengl.GL11
import org.lwjgl.opengl.GL20
import org.lwjgl.opengl.GL30
import sk.pa3kc.entity.Camera
import sk.pa3kc.entity.Light
import sk.pa3kc.ex.GLModelException
import sk.pa3kc.ui.call.KeyCallback

import sk.pa3kc.ex.GLShaderException
import sk.pa3kc.holder.ShaderPrograms
import sk.pa3kc.holder.loadModelToVAO
import sk.pa3kc.mylibrary.utils.ArgsParser
import sk.pa3kc.mylibrary.utils.get
import sk.pa3kc.poko.program.StaticShaderProgram
import sk.pa3kc.util.newFloatBuffer
import sk.pa3kc.util.newIntBuffer
import java.io.File
import sk.pa3kc.util.obj.loadObjModel
import sk.pa3kc.util.validateAsDir
import kotlin.system.exitProcess

object App2 {
    @JvmField val KEYBOARD = KeyCallback()

    @JvmField val CAMERA = Camera()
    @JvmField val LIGHT = Light(
        0f, 0f, -55f,
        1f, 1f, 1f
    )

    @JvmField val PARAMS = ArgsParser()

    private lateinit var errCallback: GLFWErrorCallbackI

    @JvmStatic
    fun main(args: Array<out String>) {
        PARAMS.parse(*args)

        this.errCallback = GLFWErrorCallback.createPrint(System.err)
        GLFW.glfwSetErrorCallback(errCallback)

        if (!GLFW.glfwInit()) {
            throw IllegalStateException("Cannot initialize GLFW")
        }

        GLFW.glfwWindowHint(GLFW.GLFW_CONTEXT_VERSION_MAJOR, 3)
        GLFW.glfwWindowHint(GLFW.GLFW_CONTEXT_VERSION_MINOR, 3)
        GLFW.glfwWindowHint(GLFW.GLFW_OPENGL_PROFILE, GLFW.GLFW_OPENGL_CORE_PROFILE)
        GLFW.glfwWindowHint(GLFW.GLFW_RESIZABLE, GLFW.GLFW_FALSE)
        GLFW.glfwWindowHint(GLFW.GLFW_DECORATED, GLFW.GLFW_TRUE)
        GLFW.glfwWindowHint(GLFW.GLFW_TRANSPARENT_FRAMEBUFFER, GLFW.GLFW_TRUE)

        val windowId = GLFW.glfwCreateWindow(500, 500, "", GL_NULL, GL_NULL)

        if (windowId == GL_NULL) {
            GLFW.glfwTerminate()
            throw IllegalStateException("Cannot create new GL window")
        }

        GLFW.glfwMakeContextCurrent(windowId)
        val glCapabilities = GL.createCapabilities()

        val glVersion = GL11.glGetString(GL11.GL_VERSION)
        val glslVersion = GL11.glGetString(GL20.GL_SHADING_LANGUAGE_VERSION)
        println("OpenGL version: $glVersion")
        println("GLSL version: $glslVersion")

//        loadModels(PARAMS["models", "models"])

        try {
            generateShaderProgram(PARAMS["shaders", "shaders"])
        } catch (e: GLShaderException) {
            e.printStackTrace()
            GLFW.glfwTerminate()
            exitProcess(1)
        }

        val vertices = newFloatBuffer(-.5f, -.5f, .5f, -.5f, .5f, .5f, -.5f, .5f)
        val indicesData = intArrayOf(0, 1, 2, 2, 3, 0)
        val indices = newIntBuffer(*indicesData)

        val vaoId = GL30.glGenVertexArrays().also {
            GL30.glBindVertexArray(it)
        }

        val vertexBufferId = GL20.glGenBuffers().also {
            GL20.glBindBuffer(GL20.GL_ARRAY_BUFFER, it)
            GL20.glBufferData(GL20.GL_ARRAY_BUFFER, vertices, GL20.GL_STATIC_DRAW)
        }

        val indexBufferId = GL20.glGenBuffers().also {
            GL20.glBindBuffer(GL20.GL_ELEMENT_ARRAY_BUFFER, it)
            GL20.glBufferData(GL20.GL_ELEMENT_ARRAY_BUFFER, indices, GL20.GL_STATIC_DRAW)
        }

        GL20.glEnableVertexAttribArray(0)
        GL20.glVertexAttribPointer(0, 2, GL11.GL_FLOAT, false, Float.SIZE_BYTES * 2, 0)

        ShaderPrograms.useProgram(0)

        val u_ColorLoc = GL20.glGetUniformLocation(ShaderPrograms.activeProgramId, "u_Color")
        GL20.glUniform4f(u_ColorLoc, 0f, 1f, 1f, 1f)

        while (!GLFW.glfwWindowShouldClose(windowId)) {
            GL11.glClear(GL11.GL_COLOR_BUFFER_BIT)

            GL20.glDrawElements(GL20.GL_TRIANGLES, indicesData.size, GL20.GL_UNSIGNED_INT, 0)

            GLFW.glfwSwapBuffers(windowId)

            GLFW.glfwPollEvents()
        }

        if (ShaderPrograms.hasActiveProgram) {
            ShaderPrograms.deactivatePrograms()
        }

        ShaderPrograms.close()

        GL20.glBindBuffer(GL20.GL_ARRAY_BUFFER, GL_NULL.toInt())
        GL20.glDeleteBuffers(vertexBufferId)

        GLFW.glfwMakeContextCurrent(GL_NULL)

        GLFW.glfwDestroyWindow(windowId)

        GLFW.glfwTerminate()

        if (App2::errCallback.isInitialized) {
            GLFWErrorCallback.free(errCallback.address())
        }
    }

    @JvmStatic
    fun loadModels(rootPath: String) {
        val rootDir = File(rootPath).validateAsDir()

        for (entry in rootDir.list()!!) {
            try {
                loadObjModel("$rootPath/$entry").run {
                    loadModelToVAO(vertices, textureCoords, normals, indices)
                }
            } catch (e: GLModelException) {
                e.printStackTrace()
            }
        }
    }

    @JvmStatic
    @Throws(GLShaderException::class)
    fun generateShaderProgram(rootPath: String) {
        ShaderPrograms.add(
            StaticShaderProgram.newStaticShaderProgram {
                val rootDir = File(rootPath).validateAsDir()

                for (entry in rootDir.list()!!) {
                    val dir = File(rootDir, entry).validateAsDir()

                    when(entry) {
                        "vertex" -> {
                            for (shaderSourcePath in dir.list()!!) {
                                addVertexShader("$rootPath/$entry/$shaderSourcePath")
                            }
                        }
                        "fragment" -> {
                            for (shaderSourcePath in dir.list()!!) {
                                addFragmentShader("$rootPath/$entry/$shaderSourcePath")
                            }
                        }
                    }
                }
            }
        )
    }
}

/*
object App {
    @JvmStatic
    fun main(args: Array<out String>) {
        Logger.isVisible = true
//    loadShaders("shaders")

//    loadModels()

//    loadTextures()

//    loadSounds()

//    App(args)
    }

    var textureIndex = 0

    val window: GLWindow

    init {
        GraphicsEnvironment.getLocalGraphicsEnvironment().defaultScreenDevice.defaultConfiguration.bounds.also {
            WINDOW_WIDTH = it.width
            WINDOW_HEIGHT = it.height
        }

        this.window = GLWindow(WINDOW_WIDTH, WINDOW_HEIGHT, "My Game") {
            VertexArrayObjects.close()
            VertexBufferObjects.close()
            SHADER_PROGRAM.close()
        }

        this.window.keyCallback = KEYBOARD

        glfwMakeContextCurrent(this.window.windowId)

        val shaderProgram = newStaticShaderProgram {
            addVertexShaders("${PATH_SHADERS_VERTEX}/1.mvs")
            addFragmentShaders("${PATH_SHADERS_FRAGMENT}/1.mfs")
        }

        if (shaderProgram is InvalidStaticShaderProgram) {
            glfwMakeContextCurrent(NULL)
            this.window.close()
        } else {
            SHADER_PROGRAM = shaderProgram
        }

        val projectionMatrix = Matrix4f.projectionMatrix(WINDOW_WIDTH, WINDOW_HEIGHT, FOV, NEAR_PLANE, FAR_PLANE)
        ShaderPrograms.useProgram(SHADER_PROGRAM)
        SHADER_PROGRAM.loadProjectionMatrix(projectionMatrix)
        ShaderPrograms.deactivatePrograms()

        val model: RawModel
        val obj: ObjModel

        try {
            obj = EmptyObjModel // loadObjModel(args[0])
        } catch (ex: Exception) {
            ex.printStackTrace()

            glfwMakeContextCurrent(NULL)
            this.window.close()

            exitProcess(0)
        }

        model = loadModelToVAO(
            obj.vertices,
            obj.textureCoords,
            obj.normals,
            obj.indices
        )

        val texture = loadTextureFromRes("crafting_table_top.png")

        glfwMakeContextCurrent(NULL)

        val texturedModel = TexturedModel(model, texture)
        val entity = Entity(texturedModel, Vector3f(0f, 0f, -100f), 0f, 0f, 0f, 1f)

        this.window.setBackgroundColor(1f, 0f, 0f)
        this.window.show()

        this.window.uiThread.add(entity)
    }
}
*/
