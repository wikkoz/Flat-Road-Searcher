package view

import main.kotlin.controller.PlaceSearcherListener
import model.Model
import java.awt.BorderLayout
import java.awt.Component
import java.awt.Dimension
import java.awt.GridLayout
import java.awt.event.ActionListener
import javax.swing.*
import javax.swing.text.NumberFormatter
import java.text.NumberFormat



class OptionsPanelFactory {
    fun create(model: Model, placeSearcherListener: PlaceSearcherListener): JPanel {
        val panel = JPanel(GridLayout(2, 1))
        panel.add(upperPanel(model, placeSearcherListener))
        panel.add(JPanel())

        return panel
    }

    private fun upperPanel(model: Model, placeSearcherListener: PlaceSearcherListener): JPanel {
        val panel = JPanel(GridLayout(4, 1))
        panel.preferredSize = Dimension(100, 1000)
        val startPlace = JTextField()
        startPlace.addActionListener({ _ -> model.startPlace = placeSearcherListener.findPlace(startPlace.text) })
        val endPlace = JTextField()
        startPlace.addActionListener({ _ -> model.endPlace = placeSearcherListener.findPlace(endPlace.text) })
        val maxIncline = numberJTextField()
        startPlace.addActionListener({ _ -> model.incline = maxIncline.text.toDouble() })
        val calculate = JButton()
        calculate.text = "Wyznacz trase"
        panel.add(createPanelWithElement(startPlace, "Miejsce początkowe"))
        panel.add(createPanelWithElement(endPlace, "Miejsce końcowe"))
        panel.add(createPanelWithElement(maxIncline, "Maksymalne przewyższenie"))
        panel.add(createPanelWithElement(calculate, null))
        return panel
    }

    private fun createPanelWithElement(component: Component, text: String?): JPanel {
        val noRows = if (text?.isBlank() == true) 1 else 2;
        val panel = JPanel(GridLayout(noRows, 1))

        if (noRows == 2) {
            val textPanel = JPanel(BorderLayout())
            val textArea = JLabel(text)
            textPanel.add(textArea, BorderLayout.CENTER)
            panel.add(textPanel)
        }

        panel.add(component)
        return panel;
    }

    private fun numberJTextField(): JFormattedTextField {
        val format = NumberFormat.getInstance()
        val formatter = NumberFormatter(format)
        formatter.valueClass = Double::class.java
        formatter.minimum = 0
        formatter.maximum = Double.MAX_VALUE
        formatter.allowsInvalid = false
        // If you want the value to be committed on each keystroke instead of focus lost
        formatter.commitsOnValidEdit = true
        return JFormattedTextField(format)
    }
}