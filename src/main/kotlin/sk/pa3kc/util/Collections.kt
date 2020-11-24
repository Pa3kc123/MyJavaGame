package sk.pa3kc.util

inline fun <T> Array<out T>.forEachApply(action: T.() -> Unit) {
    for (element in this) element.action()
}
