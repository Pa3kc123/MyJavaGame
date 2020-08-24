package sk.pa3kc.entity

import sk.pa3kc.mylibrary.matrix.pojo.Vector3f

data class Light(
    val position: Vector3f,
    val color: Vector3f
) {
    constructor(pX: Float, pY: Float, pZ: Float, cR: Float, cG: Float, cB: Float) : this(
        Vector3f(pX, pY, pZ),
        Vector3f(cR, cG, cB)
    )
}
