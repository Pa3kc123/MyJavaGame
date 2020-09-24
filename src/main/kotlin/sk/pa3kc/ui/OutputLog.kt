package sk.pa3kc.ui

import java.awt.Dimension
import java.awt.EventQueue
import java.io.OutputStream
import java.io.PrintStream
import java.io.UnsupportedEncodingException
import java.util.*
import javax.swing.*
import kotlin.collections.ArrayList

class OutputLog @JvmOverloads constructor(title: String? = null) : JFrame(title) {
    private val textArea = JTextArea().apply {
        this.minimumSize = Dimension(800, 100)
    }
    private val textAreaOS = TextAreaOutputStream(this.textArea, 60).also {
        val printStream = PrintStream(it)
        System.setOut(printStream)
        System.setErr(printStream)
    }

    private var switch = false
        get() {
            val result = field
            field = !field
            return result
        }

    private val btn = JButton("Press me )").apply {
        this.minimumSize = Dimension(75, 50)
        this.addActionListener {
            println("Jou")
//            val doc = textPane.styledDocument
//            val style = textPane.addStyle("Mah Style", null)
//            StyleConstants.setForeground(style, if (switch) Color.BLUE else Color.RED)
//            doc.insertString(doc.length, "You pressed me <3\n", style)
        }
        this.alignmentY = RIGHT_ALIGNMENT
    }

    init {
        (super.getContentPane() as JPanel).also {
            val layout = GroupLayout(it)
            it.layout = layout

            it.add(this.textArea)
            it.add(this.btn)

            layout.autoCreateContainerGaps = false
            layout.autoCreateGaps = false

            layout.setHorizontalGroup(layout.createParallelGroup().addComponent(this.textArea).addComponent(this.btn))
            layout.setVerticalGroup(layout.createSequentialGroup().addComponent(this.textArea).addComponent(this.btn))
        }

        super.pack()

        super.setLocationRelativeTo(null)
        super.setDefaultCloseOperation(EXIT_ON_CLOSE)
        super.setVisible(true)
    }
}

class TextAreaOutputStream(txtara: JTextArea, maxlin: Int = 1000) : OutputStream() {
    // array for write(int val)
    private var oneByte: ByteArray
    // most recent action
    private var appender: Appender? = null

    init {
        if(maxlin < 1) {
            throw IllegalArgumentException("TextAreaOutputStream maximum lines must be positive (value=$maxlin)")
        }
        oneByte = ByteArray(1)
        appender = Appender(txtara, maxlin)
    }

    /** Clear the current console text area. */
    fun clear() {
        synchronized(this) {
            appender?.clear()
        }
    }

    override fun close() {
        synchronized(this) {
            appender = null
        }
    }

    override fun flush() {
        synchronized(this) {

        }
    }

    override fun write(value: Int) {
        this.oneByte[0] = value.toByte()
        write(oneByte, 0, 1)
    }

    override fun write(ba: ByteArray) {
        synchronized(this) {
            write(ba, 0, ba.size)
        }
    }

    override fun write(ba: ByteArray, str: Int, len: Int) {
        synchronized(this) {
            appender?.append(bytesToString(ba,str,len))
        }
    }

    companion object {
        private fun bytesToString(ba: ByteArray, str: Int, len: Int): String = try {
            String(ba, str, len, Charsets.UTF_8)
        } catch (thr: UnsupportedEncodingException) {
            String(ba, str, len)
        } // all JVMs are required to support UTF-8

        class Appender(txtara: JTextArea, maxlin: Int) : Runnable {
            private val textArea = txtara

            // maximum lines allowed in text area
            private val maxLines = maxlin

            // length of lines within text area
            private val lengths = LinkedList<Int>()

            // values waiting to be appended
            private val values = ArrayList<String>()

            // length of current line
            private var curLength: Int = 0
            private var clear: Boolean = true
            private var queue: Boolean = false

            fun append(value: String) {
                synchronized(this) {
                    this.values.add(value)
                    if(queue) {
                        queue = false
                        EventQueue.invokeLater(this)
                    }
                }
            }

            fun clear() {
                synchronized(this) {
                    clear = true
                    curLength = 0
                    lengths.clear()
                    values.clear()
                    if(queue) {
                        queue = false
                        EventQueue.invokeLater(this)
                    }
                }
            }

            // MUST BE THE ONLY METHOD THAT TOUCHES textArea!
            override fun run() {
                synchronized(this) {
                    if (clear) {
                        textArea.text = ""
                    }
                    for (value in values) {
                        curLength += value.length
                        if (value.endsWith("\n") || value.endsWith(System.getProperty("line.separator", "\n"))) {
                            if(lengths.size >= maxLines) {
                                textArea.replaceRange("", 0, lengths.removeFirst())
                            }
                            lengths.addLast(curLength)
                            curLength = 0
                        }
                        textArea.append(value)
                    }
                    values.clear()
                    clear =false
                    queue =true
                }
            }
        }
    }
}

