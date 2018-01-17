package model

import org.jxmapviewer.viewer.GeoPosition

data class Model(var incline: Double = Double.MAX_VALUE,
                 var startPlace: GeoPosition? = GeoPosition(0.0, 0.0),
                 var endPlace: GeoPosition? = GeoPosition(0.0, 0.0),
                 val track: MutableList<GeoPosition> = mutableListOf(),
                 var cost: Double = 0.0,
                 var elevation: Double = 0.0,
                 var distance: Double = 0.0)