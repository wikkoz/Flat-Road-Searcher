package view

import main.kotlin.controller.PlaceSearcherListener
import main.kotlin.view.MainMapMouseListener
import model.Model
import org.jxmapviewer.JXMapKit
import org.jxmapviewer.JXMapViewer
import org.jxmapviewer.painter.CompoundPainter
import org.jxmapviewer.viewer.DefaultWaypoint
import org.jxmapviewer.viewer.GeoPosition
import org.jxmapviewer.viewer.Waypoint
import org.jxmapviewer.viewer.WaypointPainter
import java.awt.GridBagConstraints
import java.awt.GridBagConstraints.BOTH
import java.awt.GridBagLayout
import java.awt.GridLayout
import java.util.HashSet
import javax.swing.JFrame
import javax.swing.JLabel
import javax.swing.JPanel


class MainWindow(private val model: Model) {
    companion object {
        private val TITLE = "Flat Road Searcher"
        private val WARSAW_POSITION = GeoPosition(52.23, 21.01)

        fun createAndInit(model: Model, placeSearcherListener: PlaceSearcherListener): MainWindow {
            val window = MainWindow(model)
            window.init(placeSearcherListener)
            return window
        }
    }

    private val mapKit: JXMapKit = JXMapKit()
    private val frame: JFrame = JFrame(MainWindow.TITLE)
    private val panel: JPanel = JPanel(GridBagLayout())

    private lateinit var startPlaceLabel: JLabel
    private lateinit var endPlaceLabel: JLabel

    private fun init(placeSearcherListener: PlaceSearcherListener) {
        val mainMapListener = MainMapMouseListener(mapKit, model, {repaint()})

        mapKit.addressLocation = WARSAW_POSITION
        mapKit.defaultProvider = JXMapKit.DefaultProviders.OpenStreetMaps
        mapKit.centerPosition = WARSAW_POSITION
        mapKit.setZoom(30000)
        mapKit.mainMap.addMouseListener(mainMapListener)
        addPainter(mapKit.mainMap)

        val mapKitConstraints = GridBagConstraints()
        mapKitConstraints.fill = BOTH;
        mapKitConstraints.weightx = 0.8
        mapKitConstraints.weighty = 1.0
        panel.add(mapKit, mapKitConstraints)

        val optionsConstraints = GridBagConstraints()
        optionsConstraints.weightx = 0.4;
        optionsConstraints.weighty = 1.0;
        val factory = OptionsPanelFactory()
        panel.add(factory.create(model, placeSearcherListener), optionsConstraints)
        startPlaceLabel = factory.startPlaceLabel
        endPlaceLabel = factory.endPlaceLabel

        frame.contentPane.add(panel)
        frame.setSize(900, 600)
        frame.defaultCloseOperation = JFrame.EXIT_ON_CLOSE
        frame.isVisible = true
    }

    private fun addPainter(mainMap: JXMapViewer) {
        val cp = CompoundPainter<JXMapViewer>()
        cp.isCacheable = false
        cp.setPainters(selectedLocations)
        mainMap.overlayPainter = cp
    }

    private val selectedLocations = object : WaypointPainter<Waypoint>() {
        override fun getWaypoints(): Set<Waypoint> {
            val set = HashSet<Waypoint>()
            if (model.startPlace != null){
                set.add(DefaultWaypoint(model.startPlace))
            }
            if (model.endPlace != null){
                set.add(DefaultWaypoint(model.endPlace))
            }
            return set
        }
    }

    private fun repaint(){
        panel.repaint()
        startPlaceLabel.text = model.startPlace.toString()
        endPlaceLabel.text = model.endPlace.toString()
    }
}