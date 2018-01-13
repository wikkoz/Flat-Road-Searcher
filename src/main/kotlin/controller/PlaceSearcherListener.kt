package main.kotlin.controller

import main.kotlin.database.Database
import org.jxmapviewer.viewer.GeoPosition

class PlaceSearcherListener(private val database: Database) {

    fun findPlace(place: String): GeoPosition? {
        return database.getGeoLocationByStreetName(place)
    }
}