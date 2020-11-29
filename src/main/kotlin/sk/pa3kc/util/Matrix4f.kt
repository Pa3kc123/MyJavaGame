package sk.pa3kc.util

import sk.pa3kc.mylibrary.matrix.pojo.Matrix4f
import java.nio.FloatBuffer

fun ortho(l: Float, r: Float, b: Float, t: Float, n: Float, f: Float): FloatBuffer {
    val mat = Matrix4f()

    mat[0, 0] = 2f / (r - l)
    mat[1, 1] = 2f / (t - b)
    mat[2, 2] = -2f / (f - n)

    mat[3, 0] = -(r + l) / (r - l)
    mat[3, 1] = -(t + b) / (t - b)
    mat[3, 2] = -(f + n) / (f - n)
    mat[3, 3] = 1f

    return newFloatBuffer(*mat.matrix)
}
