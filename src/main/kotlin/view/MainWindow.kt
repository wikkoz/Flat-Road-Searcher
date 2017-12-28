package view

import main.kotlin.controller.PlaceSearcherListener
import main.kotlin.view.MainMapMouseListener
import model.Model
import org.jxmapviewer.JXMapKit
import org.jxmapviewer.viewer.GeoPosition
import java.awt.GridBagConstraints
import java.awt.GridBagConstraints.BOTH
import java.awt.GridBagLayout
import java.awt.GridLayout
import javax.swing.JFrame
import javax.swing.JPanel


class MainWindow {
    companion object {
        private val TITLE = "Flat Road Searcher"
        private val WARSAW_POSITION = GeoPosition(52.23, 21.01)

        fun createAndInit(model: Model, placeSearcherListener: PlaceSearcherListener): MainWindow {
            val window = MainWindow()
            window.init(model, placeSearcherListener)
            return window
        }
    }

    private val mapKit: JXMapKit = JXMapKit()
    private val frame: JFrame = JFrame(MainWindow.TITLE)
    private val panel: JPanel = JPanel(GridBagLayout())


    private fun init(model: Model, placeSearcherListener: PlaceSearcherListener) {
        val mainMapListener = MainMapMouseListener(mapKit.mainMap, model)

        mapKit.addressLocation = WARSAW_POSITION
        mapKit.defaultProvider = JXMapKit.DefaultProviders.OpenStreetMaps
        mapKit.centerPosition = WARSAW_POSITION
        mapKit.setZoom(30000)
        mapKit.mainMap.addMouseListener(mainMapListener)


        val mapKitConstraints = GridBagConstraints()
        mapKitConstraints.fill = BOTH;
        mapKitConstraints.weightx = 0.8
        mapKitConstraints.weighty = 1.0
        panel.add(mapKit, mapKitConstraints)

        val optionsConstraints = GridBagConstraints()
        optionsConstraints.weightx = 0.4;
        optionsConstraints.weighty = 1.0;
        panel.add(OptionsPanelFactory().create(model, placeSearcherListener), optionsConstraints)

        frame.contentPane.add(panel)
        frame.setSize(900, 600)
        frame.defaultCloseOperation = JFrame.EXIT_ON_CLOSE
        frame.isVisible = true
    }

}