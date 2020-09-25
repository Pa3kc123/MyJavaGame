package sk.pa3kc.poko

import org.lwjgl.opengl.GL11
import org.lwjgl.opengl.GL20
import sk.pa3kc.entity.Camera
import sk.pa3kc.entity.Light
import sk.pa3kc.mylibrary.matrix.math.MatrixMath
import sk.pa3kc.mylibrary.matrix.pojo.Matrix4f

object InvalidStaticShaderProgram : StaticShaderProgram(-1)

open class StaticShaderProgram(
    programId: Int
) : ShaderProgram(
    programId
) {
    fun loadTransformationMatrix(matrix: Matrix4f) {
        super.loadMatrix(UniformLocation.TRANSFORMATION_MATRIX.location, matrix)
    }

    fun loadProjectionMatrix(matrix: Matrix4f) {
        super.loadMatrix(UniformLocation.PROJECTION_MATRIX.location, matrix)
    }

    fun loadViewMatrix(camera: Camera) {
        val matrix = MatrixMath.createViewMatrix(camera.position, camera.pitch, camera.yaw, camera.roll)
        super.loadMatrix(UniformLocation.VIEW_MATRIX.location, matrix)
    }

    fun loadLight(light: Light) {
        val pos = light.position
        val color = light.color

        super.loadVector(UniformLocation.LIGHT_POSITION.location, pos.x, pos.y, pos.z)
        super.loadVector(UniformLocation.LIGHT_COLOR.location, color.x, color.y, color.z)
    }

    class Builder : ShaderProgram.Builder<StaticShaderProgram>() {
        fun addVertexShaders(vararg paths: String) {
            for (path in paths) {
                super.vertexShaders += newVertexShaderFromRes(path)
            }
        }
        fun addFragmentShaders(vararg paths: String) {
            for (path in paths) {
                super.fragmentShaders += newFragmentShaderFromRes(path)
            }
        }

        override fun build(): StaticShaderProgram {
            val programId = GL20.glCreateProgram().also {
                val shaders = super.vertexShaders + super.fragmentShaders

                for (shader in shaders) {
                    GL20.glAttachShader(it, shader.shaderId)
                }

                for (attr in Attribute.values()) {
                    GL20.glBindAttribLocation(it, attr.index, attr.attrName)
                }

                GL20.glLinkProgram(it)

                if (GL20.glGetProgrami(it, GL20.GL_LINK_STATUS) != GL11.GL_TRUE) {
                    System.err.println(GL20.glGetProgramInfoLog(it))
                    System.err.println("Could not compile shader program")
                    GL20.glDeleteProgram(it)
                    return InvalidStaticShaderProgram
                }

                GL20.glValidateProgram(it)

                if (GL20.glGetProgrami(it, GL20.GL_VALIDATE_STATUS) != GL11.GL_TRUE) {
                    System.err.println(GL20.glGetProgramInfoLog(it))
                    System.err.println("Validation was not successful for shader program")
                    GL20.glDeleteProgram(it)
                    return InvalidStaticShaderProgram
                }

                for (ul in UniformLocation.values()) {
                    ul.location = GL20.glGetUniformLocation(it, ul.ulName)
                }

                for (shader in shaders) {
                    GL20.glDetachShader(it, shader.shaderId)
                    GL20.glDeleteShader(shader.shaderId)
                }
            }

            return StaticShaderProgram(programId)
        }
    }
}

enum class Attribute(val index: Int, val attrName: String) {
    POSITION(0, "position"),
    TEXTURE_COORD(1, "textureCoord"),
    NORMAL(2, "normal")
}

enum class UniformLocation(val ulName: String) {
    TRANSFORMATION_MATRIX("transformationMatrix"),
    PROJECTION_MATRIX("projectionMatrix"),
    VIEW_MATRIX("viewMatrix"),
    LIGHT_POSITION("lightPosition"),
    LIGHT_COLOR("lightColor");

    var location: Int = -1
}

fun newStaticShaderProgram(block: StaticShaderProgram.Builder.() -> Unit) = StaticShaderProgram.Builder().apply(block).build()
