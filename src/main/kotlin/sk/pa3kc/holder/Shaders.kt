package sk.pa3kc.holder

import sk.pa3kc.poko.Shader
import sk.pa3kc.util.GLObjectCollection

object Shaders : GLObjectCollection<Shader>() {
    override fun cleanup() = super.forEach {
        it.close()
    }
}
