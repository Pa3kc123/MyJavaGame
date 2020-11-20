package sk.pa3kc.util

open class GLCollection<T : AutoCloseable> : ArrayList<T>(), AutoCloseable {
    open fun onAdd(index: Int, element: T) {}
    open fun onAddAll(index: Int, elements: Collection<T>) {}

    override fun add(element: T): Boolean {
        if (super.add(element)) {
            this.onAdd(super.size - 1, element)
            return true
        }
        return false
    }

    override fun add(index: Int, element: T) {
        super.add(index, element)
        this.onAdd(index, element)
    }

    override fun addAll(elements: Collection<T>): Boolean {
        if (super.addAll(elements)) {
            this.onAddAll(super.size - 1, elements)
            return true
        }
        return false
    }

    override fun addAll(index: Int, elements: Collection<T>): Boolean {
        if (super.addAll(index, elements)) {
            this.onAddAll(index, elements)
            return true
        }
        return false
    }

    override fun close() {
        for (i in 0 until super.size) {
            super.get(i).close()
        }
    }
}
