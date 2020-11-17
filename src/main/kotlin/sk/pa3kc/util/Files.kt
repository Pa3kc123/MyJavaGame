package sk.pa3kc.util

import java.io.File

fun File.validateAsFile() = when {
    !this.exists() -> throw IllegalStateException("Directory ${this.path} does not exists")
    !this.isFile -> throw IllegalStateException("${this.path} is not a file")
    else -> this
}

fun File.validateAsDir() = when {
    !this.exists() -> throw IllegalStateException("Directory ${this.path} does not exists")
    !this.isDirectory -> throw IllegalStateException("${this.path} is not a directory")
    else -> this
}
