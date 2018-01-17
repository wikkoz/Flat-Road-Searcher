package main.kotlin.view

import model.Model
import org.jxmapviewer.JXMapKit
import java.awt.Point
import java.awt.event.MouseAdapter
import java.awt.event.MouseEvent
import javax.swing.JMenuItem
import javax.swing.JPopupMenu
import javax.swing.SwingUtilities

class MainMapMouseListener(private val mapKit: JXMapKit, private val model: Model, private val repaintF: () -> Unit) : MouseAdapter() {
    private val menuBar: JPopupMenu = JPopupMenu()
    private var clickPoint: Point? = null

    init {
        val mapView = mapKit.mainMap
        val beginPlace = JMenuItem("Miejsce początkowe")
        beginPlace.addActionListener({
            model.startPlace = mapView.convertPointToGeoPosition(clickPoint)
            repaintF()
        })
        val endPlace = JMenuItem("Miejsce końcowe")
        endPlace.addActionListener({
            model.endPlace = mapView.convertPointToGeoPosition(clickPoint)
            repaintF()
            model.track.clear()
        })
        menuBar.add(beginPlace)
        menuBar.add(endPlace)

    }

    override fun mousePressed(e: MouseEvent) {
        clickPoint = e.point
        if (SwingUtilities.isRightMouseButton(e)) { //if the event shows the menu
            menuBar.show(mapKit.mainMap, e.x, e.y) //and show the menu
        }
    }


}