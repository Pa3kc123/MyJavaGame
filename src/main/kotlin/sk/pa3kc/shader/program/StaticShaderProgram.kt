package sk.pa3kc.shader.program

import sk.pa3kc.entity.Camera
import sk.pa3kc.entity.Light
import sk.pa3kc.mylibrary.matrix.math.MatrixMath
import sk.pa3kc.mylibrary.matrix.pojo.Matrix4f
import sk.pa3kc.poko.Shader

class StaticShaderProgram(vs: Shader, fs: Shader) : ShaderProgram(vs, fs) {
    override fun bindAttributes() {
        Attribute.values().forEach {
            super.bindAttribute(it.index, it.attrName)
        }
    }

    protected override fun getAllUniformLocations() {
        UniformLocation.values().forEach {
            it.location = super.getUniformLocation(it.ulName)
        }
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
        val pos = light.position
        val color = light.color

        super.loadVector(UniformLocation.LIGHT_POSITION.location, pos.x, pos.y, pos.z)
        super.loadVector(UniformLocation.LIGHT_COLOR.location, color.x, color.y, color.z)
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
