package main.kotlin.controller

import main.kotlin.database.Database
import org.jxmapviewer.viewer.GeoPosition
import java.util.*

class RoadSearcher(private val database: Database) {

    val db = database

    fun searchRoad(startPoint: GeoPosition, endPoint: GeoPosition, incline : Double) : List<GeoPosition> {
        return database.aStar(startPoint, endPoint, incline)
    }
}