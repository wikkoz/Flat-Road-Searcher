package view

import org.jxmapviewer.JXMapViewer
import org.jxmapviewer.viewer.GeoPosition
import java.awt.BasicStroke
import java.awt.Color
import java.awt.Graphics2D
import java.awt.RenderingHints
import javax.swing.Painter

/**
 * Paints a route
 * @author Martin Steiger
 */
class RoutePainter
/**
 * @param track the track
 */
(track: List<GeoPosition>) : Painter<JXMapViewer> {
    private val color = Color.RED
    private val antiAlias = true

    private val track: List<GeoPosition>

    init {
        // copy the list so that changes in the
        // original list do not have an effect here
        this.track = ArrayList(track)
    }

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

    /**
     * @param g the graphics object
     * @param map the map
     */
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
                g.drawLine(lastX, lastY, pt.x as Int, pt.y as Int)
            }

            lastX = pt.x as Int
            lastY = pt.y as Int
        }
    }
}