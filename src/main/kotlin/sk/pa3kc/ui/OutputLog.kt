package sk.pa3kc.ui

import java.awt.Color
import java.awt.Dimension
import javax.swing.*
import javax.swing.text.StyleConstants

class OutputLog @JvmOverloads constructor(title: String? = null) : JFrame(title) {
    private val textPane = JTextPane().apply {
        this.minimumSize = Dimension(800, 100)
    }

    private var switch = false
        get() {
            val result = field
            field = !field
            return result
        }

    private val btn = JButton("Press me ;)").apply {
        this.minimumSize = Dimension(75, 50)
        this.addActionListener {
            val doc = textPane.styledDocument
            val style = textPane.addStyle("Mah Style", null)
            StyleConstants.setForeground(style, if (switch) Color.BLUE else Color.RED)
            doc.insertString(doc.length, "You pressed me <3\n", style)
        }
        this.alignmentY = RIGHT_ALIGNMENT
    }

    init {
        (super.getContentPane() as JPanel).also {
            val layout = GroupLayout(it)
            it.layout = layout

            it.add(this.textPane)
            it.add(this.btn)

            layout.autoCreateContainerGaps = false
            layout.autoCreateGaps = false

            layout.setHorizontalGroup(layout.createParallelGroup().addComponent(this.textPane).addComponent(this.btn))
            layout.setVerticalGroup(layout.createSequentialGroup().addComponent(this.textPane).addComponent(this.btn))
        }

        super.pack()

        super.setLocationRelativeTo(null)
        super.setDefaultCloseOperation(EXIT_ON_CLOSE)
        super.setVisible(true)
    }
}
