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

    private var switch = false
        get() {
            val result = field
            field = !field
            return result
        }

    private val btn = JButton("Press me :)").apply {
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
