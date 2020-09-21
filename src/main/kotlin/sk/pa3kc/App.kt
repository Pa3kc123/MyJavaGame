package sk.pa3kc

import java.awt.GraphicsEnvironment
import kotlin.system.exitProcess

import sk.pa3kc.entity.Camera
import sk.pa3kc.entity.Entity
import sk.pa3kc.entity.Light
import sk.pa3kc.mylibrary.matrix.pojo.Matrix4f
import sk.pa3kc.mylibrary.matrix.pojo.Vector3f
import sk.pa3kc.ui.GLWindow
import sk.pa3kc.ui.call.KeyCallback

import org.lwjgl.glfw.GLFW.*
import org.lwjgl.system.MemoryUtil.NULL
import sk.pa3kc.holder.*
import sk.pa3kc.poko.*
import sk.pa3kc.ui.OutputLog
import sk.pa3kc.util.ObjModel
import sk.pa3kc.util.loadObjModel

const val PATH_SHADERS_VERTEX = "shaders/vertex"
const val PATH_SHADERS_FRAGMENT = "shaders/fragment"

@JvmField val KEYBOARD = KeyCallback()
const val FOV = 70f
const val NEAR_PLANE = 0.1f
const val FAR_PLANE = 1000f

var WINDOW_WIDTH = -1
var WINDOW_HEIGHT = -1

@JvmField val CAMERA = Camera()
@JvmField val LIGHT = Light(
    0f, 0f, -55f,
    1f, 1f, 1f
)

lateinit var SHADER_PROGRAM: StaticShaderProgram
@JvmField val CLASS_LOADER: ClassLoader = App::class.java.classLoader

class App(args: Array<out String>) {
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
            addVertexShaders(
                newVertexShaderFromRes("${PATH_SHADERS_VERTEX}/1.mvs")
            )
            addFragmentShaders(
                newFragmentShaderFromRes("${PATH_SHADERS_FRAGMENT}/1.mfs")
            )
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
            obj = loadObjModel(args[0])
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

fun main(args: Array<out String>) {
    OutputLog()
//    App(args)
}
