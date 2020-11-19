package sk.pa3kc.poko.program

import org.lwjgl.opengl.GL11
import org.lwjgl.opengl.GL20
import sk.pa3kc.entity.Camera
import sk.pa3kc.entity.Light
import sk.pa3kc.ex.GLShaderException
import sk.pa3kc.holder.ShaderPrograms
import sk.pa3kc.mylibrary.matrix.math.MatrixMath
import sk.pa3kc.mylibrary.matrix.pojo.Matrix4f
import sk.pa3kc.poko.shader.FragmentShader
import sk.pa3kc.poko.shader.VertexShader
import sk.pa3kc.ui.Logger

open class StaticShaderProgram(
    programId: Int
) : ShaderProgram(
    programId
) {
    companion object {
        @JvmStatic
        @Throws(GLShaderException::class)
        inline fun newStaticShaderProgram(block: Builder.() -> Unit): StaticShaderProgram = Builder().apply(block).build()
    }

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
        light.position.let { pos ->
            super.loadVector(UniformLocation.LIGHT_POSITION.location, pos.x, pos.y, pos.z)
        }
        light.color.let { color ->
            super.loadVector(UniformLocation.LIGHT_COLOR.location, color.x, color.y, color.z)
        }
    }

    class Builder : ShaderProgram.Builder() {
        fun addVertexShader(path: String) {
            super.vertexShaders.add(VertexShader.newVertexShader(path))
        }
        fun addFragmentShader(path: String) {
            super.fragmentShaders.add(FragmentShader.newFragmentShader(path))
        }

        override fun build(): StaticShaderProgram {
            val programId = GL20.glCreateProgram().also { programId ->
                val shaders = super.vertexShaders + super.fragmentShaders

                for (shader in shaders) {
                    GL20.glAttachShader(programId, shader.shaderId)
                }

                for (attr in Attribute.values()) {
                    GL20.glBindAttribLocation(programId, attr.index, attr.attrName)
                }

                GL20.glLinkProgram(programId)

                if (GL20.glGetProgrami(programId, GL20.GL_LINK_STATUS) != GL11.GL_TRUE) {
                    System.err.println(GL20.glGetProgramInfoLog(programId))
                    System.err.println("Could not compile shader program")
                    GL20.glDeleteProgram(programId)
                    throw GLShaderException("Could not compile shader program")
                }

                GL20.glValidateProgram(programId)

                if (GL20.glGetProgrami(programId, GL20.GL_VALIDATE_STATUS) != GL11.GL_TRUE) {
                    System.err.println(GL20.glGetProgramInfoLog(programId))
                    System.err.println("Validation was not successful for shader program")
                    GL20.glDeleteProgram(programId)
                    throw GLShaderException("Validation was not successful for shader program")
                }

                for (ul in UniformLocation.values()) {
                    ul.location = GL20.glGetUniformLocation(programId, ul.ulName)
                }

                for (shader in shaders) {
                    shader.detach(programId)
                    shader.close()
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
