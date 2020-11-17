package sk.pa3kc.entity

import sk.pa3kc.poko.model.TexturedModel
import sk.pa3kc.mylibrary.matrix.pojo.Vector3f

data class Entity(
    val model: TexturedModel,
    val position: Vector3f,
    var rotX: Float,
    var rotY: Float,
    var rotZ: Float,
    var scale: Float
) {
    fun move(dx: Float, dy: Float, dz: Float) {
        this.position.x += dx
        this.position.y += dy
        this.position.z += dz
    }

    fun rotate(dx: Float, dy: Float, dz: Float) {
        this.rotX += dx
        this.rotY += dy
        this.rotZ += dz
    }
}
