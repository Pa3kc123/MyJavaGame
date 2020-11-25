package sk.pa3kc.util

abstract class GLCollection<T> : ArrayList<T>(), AutoCloseable where T : GLBindable, T : AutoCloseable {
    override fun close() {
        for (i in 0 until super.size) {
            super.get(i).close()
        }
    }

    abstract fun unbind()
}
