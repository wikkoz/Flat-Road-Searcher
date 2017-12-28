package main.kotlin.view

import model.Model
import org.jxmapviewer.JXMapViewer
import java.awt.Point
import java.awt.event.MouseAdapter
import java.awt.event.MouseEvent
import javax.swing.JMenuItem
import javax.swing.JPopupMenu
import javax.swing.SwingUtilities

class MainMapMouseListener(private val mapView: JXMapViewer,private val model: Model) : MouseAdapter() {
    private val menuBar: JPopupMenu = JPopupMenu()
    private var clickPoint: Point? = null
    init {
        val beginPlace = JMenuItem("Miejsce początkowe")
        beginPlace.addActionListener({_ ->model.startPlace = mapView.convertPointToGeoPosition(clickPoint)})
        val endPlace = JMenuItem("Miejsce końcowe")
        endPlace.addActionListener({_ ->model.startPlace = mapView.convertPointToGeoPosition(clickPoint)})
        menuBar.add(beginPlace)
        menuBar.add(endPlace)

    }

    override fun mousePressed(e: MouseEvent) {
        clickPoint = e.point
        if (e.isPopupTrigger && SwingUtilities.isRightMouseButton(e)) { //if the event shows the menu
            menuBar.show(mapView, e.x, e.y); //and show the menu
        }
    }
}