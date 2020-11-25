package sk.pa3kc.util

import org.lwjgl.BufferUtils
import java.nio.ByteBuffer
import java.nio.FloatBuffer
import java.nio.IntBuffer

fun newByteBuffer(vararg values: Byte): ByteBuffer {
    if (values.isEmpty()) {
        throw IllegalArgumentException("values cannot be empty")
    }

    return BufferUtils.createByteBuffer(values.size).apply {
        put(values)
        rewind()
    }
}

fun newIntBuffer(vararg values: Int): IntBuffer {
    if (values.isEmpty()) {
        throw IllegalArgumentException("values cannot be empty")
    }

    return BufferUtils.createIntBuffer(values.size).apply {
        put(values)
        rewind()
    }
}

fun newFloatBuffer(vararg values: Float): FloatBuffer {
    if (values.isEmpty()) {
        throw IllegalArgumentException("values cannot be empty")
    }

    return BufferUtils.createFloatBuffer(values.size).apply {
        put(values)
        rewind()        //? Saving while 1 line of code #Optimisation
    }
}
