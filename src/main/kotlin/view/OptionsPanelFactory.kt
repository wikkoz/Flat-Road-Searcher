package main.kotlin.view

import main.kotlin.controller.PlaceSearcherListener
import main.kotlin.controller.RoadSearcher
import model.Model
import java.awt.BorderLayout
import java.awt.Component
import java.awt.Dimension
import java.awt.GridLayout
import java.awt.event.FocusEvent
import java.awt.event.FocusListener
import java.text.NumberFormat
import javax.swing.*
import javax.swing.text.NumberFormatter


class OptionsPanelFactory(private val placeSearcherListener: PlaceSearcherListener, private val roadSearcher: RoadSearcher, private val repaintF: () -> Unit) {

    lateinit var startPlaceLabel: JLabel
        private set
    lateinit var endPlaceLabel: JLabel
        private set

    fun create(model: Model): JPanel {
        val panel = JPanel(GridLayout(2, 1))
        panel.add(upperPanel(model))
        panel.add(lowerPanel(model))

        return panel
    }

    private fun upperPanel(model: Model): JPanel {
        val panel = JPanel(GridLayout(4, 1))
        panel.preferredSize = Dimension(100, 1000)
        val startPlace = JTextField()
        startPlace.addFocusListener(createFocusListener({
            model.startPlace = placeSearcherListener.findPlace(startPlace.text)
            repaintF()
        }))
        val endPlace = JTextField()
        endPlace.addFocusListener(createFocusListener({
            model.endPlace = placeSearcherListener.findPlace(endPlace.text)
            repaintF()
        }))
        val maxIncline = numberJTextField()
        maxIncline.text = "0"
        maxIncline.addFocusListener(createFocusListener({ model.incline = maxIncline.text.toDouble() }))
        val calculate = JButton()
        calculate.text = "Wyznacz trase"
        calculate.addActionListener({ calculateRoad(model) })
        panel.add(createPanelWithElement(startPlace, "Miejsce początkowe"))
        panel.add(createPanelWithElement(endPlace, "Miejsce końcowe"))
        panel.add(createPanelWithElement(maxIncline, "Maksymalne przewyższenie"))
        panel.add(createPanelWithElement(calculate, null))
        return panel
    }

    private fun createPanelWithElement(component: Component, text: String?): JPanel {
        val noRows = if (text?.isBlank() == true) 1 else 2
        val panel = JPanel(GridLayout(noRows, 1))

        if (noRows == 2) {
            val textPanel = JPanel(BorderLayout())
            val textArea = JLabel(text)
            textPanel.add(textArea, BorderLayout.CENTER)
            panel.add(textPanel)
        }

        panel.add(component)
        return panel
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

    private fun lowerPanel(model: Model): JPanel {
        val panel = JPanel(GridLayout(4, 1))
        startPlaceLabel = JLabel(model.startPlace.toString())
        endPlaceLabel = JLabel(model.endPlace.toString())
        panel.add(createPanelWithElement(startPlaceLabel, "Miejsce początkowe"))
        panel.add(createPanelWithElement(endPlaceLabel, "Miejsce końcowe"))
        return panel
    }

    private fun calculateRoad(model: Model) {
        if (model.startPlace == null || model.endPlace == null) {
            return
        }

        val track = roadSearcher.searchRoad(model.startPlace!!, model.endPlace!!)
        model.track.clear()
        model.track.addAll(track)
        repaintF()
    }

    private fun createFocusListener(lostFocus: () -> Unit): FocusListener {
        return object : FocusListener {
            override fun focusLost(e: FocusEvent?) {
                lostFocus()
            }

            override fun focusGained(e: FocusEvent?) {
            }
        }
    }
}