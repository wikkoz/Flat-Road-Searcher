package main.kotlin.controller

import main.kotlin.database.Database
import org.jxmapviewer.viewer.GeoPosition
import java.util.*

class RoadSearcher(private val database: Database) {

    init {

    }

    fun searchRoad(startPoint: GeoPosition, endPoint: GeoPosition) : List<GeoPosition> {
        return Arrays.asList<GeoPosition>(startPoint, endPoint)
    }
}