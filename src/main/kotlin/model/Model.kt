package model

import org.jxmapviewer.viewer.GeoPosition

data class Model(var incline: Double = Double.MAX_VALUE, var startPlace: GeoPosition? = null, var endPlace: GeoPosition? = null,
                 val track: MutableList<GeoPosition> = mutableListOf() )