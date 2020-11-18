package sk.pa3kc.util

import org.lwjgl.BufferUtils
import java.nio.FloatBuffer
import java.nio.IntBuffer

fun newFloatBuffer(vararg values: Float): FloatBuffer {
    if (values.isEmpty()) {
        throw IllegalArgumentException("values cannot be empty")
    }

    return BufferUtils.createFloatBuffer(values.size).apply {
        put(values)
        flip()
    }
}

fun newIntBuffer(vararg values: Int): IntBuffer {
    if (values.isEmpty()) {
        throw IllegalArgumentException("values cannot be empty")
    }

    return BufferUtils.createIntBuffer(values.size).apply {
        put(values)
        flip()
    }
}
