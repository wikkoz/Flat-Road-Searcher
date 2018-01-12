package main.kotlin.database

import java.sql.Connection
import java.sql.ResultSet


class Database {

    private var connection: Connection? = null

    init {
//        try {
//            val url = "jdbc:postgresql://localhost:5432/osm"
//            connection = DriverManager.getConnection(url, "postgres", "")
//        } catch (e: Exception) {
//            System.err.println(e.message)
//        }

    }

    fun executeQuery(query: String): ResultSet? {
        val statement = connection!!.createStatement()
        return statement.executeQuery(query)
    }


}