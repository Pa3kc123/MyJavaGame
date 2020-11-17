package sk.pa3kc.poko.model

import sk.pa3kc.poko.Texture

data class TexturedModel(
    val rawModel: RawModel,
    val texture: Texture
)
