package sk.pa3kc.holder

import org.lwjgl.opengl.GL20
import sk.pa3kc.poko.program.ShaderProgram
import sk.pa3kc.util.GLCollection

object ShaderPrograms : GLCollection<ShaderProgram>() {
    var hasActiveProgram = false
        private set
    var activeProgramId = -1

    fun useProgram(index: Int): ShaderProgram {
        return super.get(index).also {
            GL20.glUseProgram(it.programId)
            this.hasActiveProgram = true
            this.activeProgramId = it.programId
        }
    }

    fun deactivatePrograms() {
        GL20.glUseProgram(0)
        this.hasActiveProgram = false
        this.activeProgramId = -1
    }
}
