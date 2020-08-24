package sk.pa3kc.util

abstract class GLObjectCollection<T> : ArrayList<T>(), AutoCloseable {
    override fun close() = cleanup()

    abstract fun cleanup()
}
