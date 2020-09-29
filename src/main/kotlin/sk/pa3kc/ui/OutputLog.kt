package sk.pa3kc.ui

import java.awt.Color
import java.awt.Dimension
import java.util.*
import javax.swing.*
import javax.swing.text.StyleConstants
import kotlin.system.exitProcess

object OutputLog : JFrame() {
    private val textPane = MyTextPane().apply {
        this.background = Color(0xD0D0D0)
    }

    init {
        (super.getContentPane() as JPanel).also {
            val layout = GroupLayout(it)
            it.layout = layout

            val scrollPane = JScrollPane(
                this.textPane,
                JScrollPane.VERTICAL_SCROLLBAR_AS_NEEDED,
                JScrollPane.HORIZONTAL_SCROLLBAR_NEVER
            ).apply {
                this.minimumSize = Dimension(800, 100)
            }

            it.add(scrollPane)

            layout.autoCreateContainerGaps = false
            layout.autoCreateGaps = false

            layout.setHorizontalGroup(layout.createParallelGroup().addComponent(scrollPane))
            layout.setVerticalGroup(layout.createSequentialGroup().addComponent(scrollPane))
        }

        super.pack()

        super.setLocationRelativeTo(null)
        super.setDefaultCloseOperation(EXIT_ON_CLOSE)
    }

    @JvmOverloads
    fun logd(msg: String, e: Throwable? = null) = this.textPane.log(MyTextPane.LogLevel.DEBUG, msg, e)
    @JvmOverloads
    fun logi(msg: String, e: Throwable? = null) = this.textPane.log(MyTextPane.LogLevel.INFO, msg, e)
    @JvmOverloads
    fun logw(msg: String, e: Throwable? = null) = this.textPane.log(MyTextPane.LogLevel.WARNING, msg, e)
    @JvmOverloads
    fun loge(msg: String, e: Throwable? = null) = this.textPane.log(MyTextPane.LogLevel.ERROR, msg, e)
}

class MyTextPane : JTextPane() {
    enum class LogLevel{
        DEBUG,
        INFO,
        WARNING,
        ERROR;

        override fun toString() = super.name
    }

    private val doc = super.getStyledDocument()
    private val style = this.doc.addStyle("log", null)

    init {
        StyleConstants.setFontSize(this.style, (this.doc.getFont(this.style).size2D * 1.15f).toInt())
    }

    internal fun log(level: LogLevel, msg: String, e: Throwable? = null) {
        val color = when(level) {
            LogLevel.DEBUG -> Color.DARK_GRAY
            LogLevel.INFO -> Color.WHITE
            LogLevel.WARNING -> Color.YELLOW
            LogLevel.ERROR -> Color.RED
            else -> {
                System.err.println("How did we get here???")
                exitProcess(-1)
            }
        }

        StyleConstants.setForeground(this.style, color)
        val message = buildString {
            append("[${Date(System.currentTimeMillis())}] $level: ")

            append(msg)

            if (!msg.endsWith(System.lineSeparator())) {
                append(System.lineSeparator())
            }
        }
        this.doc.insertString(this.doc.length, message, this.style)
    }
}
