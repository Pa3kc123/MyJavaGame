package sk.pa3kc.ex

import java.lang.RuntimeException

class GLTextureException @JvmOverloads constructor(msg: String, e: Throwable? = null) : RuntimeException(msg, e)
