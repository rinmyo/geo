package builders

import exceptions.GeoException
import org.bukkit.Location
import org.locationtech.jts.geom.Coordinate
import org.locationtech.jts.geom.GeometryFactory
import org.locationtech.jts.geom.Polygon
import utils.coordinate

/**
 * 多邊形構造者
 */
class PolygonBuilder {
    private val coordinates = mutableListOf<Coordinate>()

    fun number() = coordinates.size

    fun addLocation(location: Location) = coordinates.add(location.coordinate())

    fun isBuildable() = (coordinates[0] == coordinates[coordinates.size - 1]) && number() > 2

    fun build(): Polygon = GeometryFactory().createPolygon(coordinates.toTypedArray())

}