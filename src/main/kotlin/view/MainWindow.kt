package view

import main.kotlin.controller.PlaceSearcherListener
import main.kotlin.controller.RoadSearcher
import main.kotlin.view.MainMapMouseListener
import main.kotlin.view.OptionsPanelFactory
import main.kotlin.view.RoutePainter
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
import java.util.*
import javax.swing.JFrame
import javax.swing.JLabel
import javax.swing.JPanel


class MainWindow(private val model: Model) {
    companion object {
        private val TITLE = "Flat Road Searcher"
        private val WARSAW_POSITION = GeoPosition(52.23, 21.01)
        private val PORTLAND_POSITION = GeoPosition(45.51, -122.66)

        fun createAndInit(model: Model, placeSearcherListener: PlaceSearcherListener, roadSearcher: RoadSearcher): MainWindow {
            val window = MainWindow(model)
            window.init(placeSearcherListener, roadSearcher)
            return window
        }
    }

    private val mapKit: JXMapKit = JXMapKit()
    private val frame: JFrame = JFrame(MainWindow.TITLE)
    private val panel: JPanel = JPanel(GridBagLayout())

    private lateinit var startPlaceLabel: JLabel
    private lateinit var endPlaceLabel: JLabel
    private lateinit var distance: JLabel
    private lateinit var elevation: JLabel
    private lateinit var cost: JLabel

    private fun init(placeSearcherListener: PlaceSearcherListener, roadSearcher: RoadSearcher) {
        val mainMapListener = MainMapMouseListener(mapKit, model, {repaint()})

        mapKit.addressLocation = PORTLAND_POSITION
        mapKit.defaultProvider = JXMapKit.DefaultProviders.OpenStreetMaps
        mapKit.centerPosition = PORTLAND_POSITION
        mapKit.setZoom(30000)
        mapKit.mainMap.addMouseListener(mainMapListener)
        addPainter(mapKit.mainMap)

        val mapKitConstraints = GridBagConstraints()
        mapKitConstraints.fill = BOTH
        mapKitConstraints.weightx = 0.8
        mapKitConstraints.weighty = 1.0
        panel.add(mapKit, mapKitConstraints)

        val optionsConstraints = GridBagConstraints()
        optionsConstraints.weightx = 0.4
        optionsConstraints.weighty = 1.0
        val factory = OptionsPanelFactory(placeSearcherListener, roadSearcher, {repaint()})
        panel.add(factory.create(model), optionsConstraints)
        startPlaceLabel = factory.startPlaceLabel
        endPlaceLabel = factory.endPlaceLabel
        distance = factory.distance
        elevation = factory.elevation
        cost = factory.cost

        frame.contentPane.add(panel)
        frame.setSize(900, 600)
        frame.defaultCloseOperation = JFrame.EXIT_ON_CLOSE
        frame.isVisible = true
    }

    private fun addPainter(mainMap: JXMapViewer) {
        val cp = CompoundPainter<JXMapViewer>()
        cp.isCacheable = false
        cp.addPainter(selectedLocations)
        cp.addPainter(RoutePainter(model.track))
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
        elevation.text = model.elevation.toString()
        cost.text = model.cost.toString()
        distance.text = model.distance.toString()
    }
}