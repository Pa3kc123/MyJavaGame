@file:JvmName("Constants")

package sk.pa3kc

import org.lwjgl.opengl.GL15

const val GL_NULL = 0L

const val PATH_SHADERS_VERTEX = "shadersX/vertex"
const val PATH_SHADERS_FRAGMENT = "shadersX/fragment"

const val TEXTURES: String = "./textures"
const val BLOCKS: String = "$TEXTURES/blocks"

const val FOV = 70f
const val NEAR_PLANE = 0.1f
const val FAR_PLANE = 1000f

const val GL_DEFAULT_VBO_USAGE = GL15.GL_STATIC_DRAW
