package main.kotlin.database

import org.jxmapviewer.viewer.GeoPosition
import java.sql.Connection
import java.sql.DriverManager


class Database {

    private var connection: Connection? = null
    private val ASTAR_QUERY =
            "WITH startPoint AS (SELECT ST_Point(?,?)::geography AS startPoint)," +
                    "endPoint AS (SELECT ST_Point(?,?)::geography AS endPoint) " +
            "SELECT st_x(n.geom_vertex) AS lon, st_y(n.geom_vertex) AS lat FROM pgr_astar(?," +
            "    (SELECT id FROM portland_2po_vertex CROSS JOIN startPoint ORDER By ST_Distance(geom_vertex, startPoint) LIMIT 1)," +
            "    (SELECT id FROM portland_2po_vertex CROSS JOIN endPoint ORDER By ST_Distance(geom_vertex, endPoint) LIMIT 1)," +
            "directed := false) AS road JOIN portland_2po_vertex n ON (road.node=n.id) " +
            "ORDER BY seq"

    private val GEO_LOCATION_BY_STREET_NAME =
            "SELECT x1 AS lat, y1 AS lon " +
            "FROM portland_2po_4pgr " +
            "WHERE osm_name  = ? " +
            "LIMIT 1"

    private val GET_LIST_OF_STREETS =
            "SELECT DISTINCT osm_name " +
            "FROM portland_2po_4pgr " +
            "WHERE osm_name is not NULL " +
                    "AND osm_name LIKE ? " +
            "ORDER BY osm_name"


    init {
        try {
            val url = "jdbc:postgresql://localhost:5432/oregon"
            connection = DriverManager.getConnection(url, "postgres", "")
        } catch (e: Exception) {
            System.err.println(e.message)
        }

    }

    fun aStar(source : GeoPosition, target : GeoPosition, maxIncline : Double): List<GeoPosition> {
        val aStarStatement = connection!!.prepareStatement(ASTAR_QUERY);

        val roadsStatement = "SELECT id, source, target, cost, x1, y1, x2, y2 FROM portland_2po_4pgr WHERE incline <" + maxIncline

        aStarStatement.setDouble(1, source.latitude)
        aStarStatement.setDouble(2, source.longitude)
        aStarStatement.setDouble(3, target.latitude)
        aStarStatement.setDouble(4, target.longitude)
        aStarStatement.setString(5, roadsStatement)

        val resultSet = aStarStatement.executeQuery()
        var resultArray = ArrayList<GeoPosition>()

        while(resultSet.next()) {
            resultArray.add(GeoPosition(resultSet.getDouble("lat"), resultSet.getDouble("lon")))
        }

        return resultArray;
    }

    fun getGeoLocationByStreetName(name : String) : GeoPosition? {
        val statement = connection!!.prepareStatement(GEO_LOCATION_BY_STREET_NAME);

        statement.setString(1, name);

        val resultSet = statement.executeQuery()
        if(resultSet.next()) {
            return GeoPosition(resultSet.getDouble("lat"), resultSet.getDouble("lon"))
        }

        return null
    }

    fun getListOfStreets(name : String) : List<String> {
        var streets = ArrayList<String>()

        val statement = connection!!.prepareStatement(GET_LIST_OF_STREETS);

        statement.setString(1,  name + "%");
        val resultSet = statement.executeQuery()
        while(resultSet.next()) {
            streets.add(resultSet.getString(1))
        }

        return streets
    }
}