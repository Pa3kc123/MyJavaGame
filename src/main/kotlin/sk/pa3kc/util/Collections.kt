package sk.pa3kc.util

inline fun <T> Array<out T>.forEachApply(action: T.() -> Unit) {
    for (i in this.indices) this[i].action()
}
