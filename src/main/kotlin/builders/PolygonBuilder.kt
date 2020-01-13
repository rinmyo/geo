package builders

import org.bukkit.Location
import org.locationtech.jts.geom.Coordinate
import org.locationtech.jts.geom.GeometryFactory
import org.locationtech.jts.geom.Polygon
import utils.coordinate

class PolygonBuilder {
    private val coordinates = arrayListOf<Coordinate>()

    fun number() = coordinates.size

    fun addLocation(location: Location): PolygonBuilder {
        coordinates.add(location.coordinate())
        return this
    }

    fun isBuildable() = coordinates[0] == coordinates[coordinates.size - 1]

    fun build(): Polygon = GeometryFactory().createPolygon(coordinates.toTypedArray())
}