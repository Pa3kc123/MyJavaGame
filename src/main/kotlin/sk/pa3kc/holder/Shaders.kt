package sk.pa3kc.holder

import sk.pa3kc.poko.Shader

object Shaders : ArrayList<Shader>(), AutoCloseable {
    override fun close() = super.forEach {
        it.close()
    }
}
