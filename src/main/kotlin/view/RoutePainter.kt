package main.kotlin.view

import org.jxmapviewer.JXMapViewer
import org.jxmapviewer.painter.Painter
import org.jxmapviewer.viewer.GeoPosition
import java.awt.BasicStroke
import java.awt.Color
import java.awt.Graphics2D
import java.awt.RenderingHints


class RoutePainter (private val track: MutableList<GeoPosition>) : Painter<JXMapViewer> {
    private val color = Color.RED
    private val antiAlias = true


    override fun paint(g: Graphics2D, map: JXMapViewer, w: Int, h: Int) {
        var g = g
        g = g.create() as Graphics2D

        // convert from viewport to world bitmap
        val rect = map.viewportBounds
        g.translate(-rect.x, -rect.y)

        if (antiAlias)
            g.setRenderingHint(RenderingHints.KEY_ANTIALIASING, RenderingHints.VALUE_ANTIALIAS_ON)

        // do the drawing
        g.color = Color.BLACK
        g.stroke = BasicStroke(4.0f)

        drawRoute(g, map)

        // do the drawing again
        g.color = color
        g.stroke = BasicStroke(2.0f)

        drawRoute(g, map)

        g.dispose()
    }

    private fun drawRoute(g: Graphics2D, map: JXMapViewer) {
        var lastX = 0
        var lastY = 0

        var first = true

        for (gp in track) {
            // convert geo-coordinate to world bitmap pixel
            val pt = map.tileFactory.geoToPixel(gp, map.zoom)

            if (first) {
                first = false
            } else {
                g.drawLine(lastX, lastY, pt.x.toInt(), pt.y.toInt())
            }

            lastX = pt.x.toInt()
            lastY = pt.y.toInt()
        }
    }
}