package sk.pa3kc.util

open class GLCollection<T : AutoCloseable> : ArrayList<T>(), AutoCloseable {
    override fun close() {
        for (i in 0 until super.size) {
            super.get(i).close()
        }
    }
}
