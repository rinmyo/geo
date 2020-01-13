package utils

import org.locationtech.jts.geom.Coordinate
import org.locationtech.jts.geom.Geometry
import org.locationtech.jts.geom.GeometryFactory
import org.locationtech.jts.geom.Polygon
import org.locationtech.jts.io.geojson.GeoJsonReader
import org.locationtech.jts.io.geojson.GeoJsonWriter
import kotlin.math.cos
import kotlin.math.sin

fun Geometry.toGeoJSON(): String = GeoJsonWriter().write(this)

fun geometryFrom(data: String): Geometry = GeoJsonReader().read(data)

fun GeometryFactory.createCircle(centre: Coordinate, radius : Double, pointNum: Int): Polygon {
    val coords = arrayOfNulls<Coordinate>(pointNum + 1)
    for (i  in 0..pointNum){
        val angle =(i.toDouble() / pointNum.toDouble()) * Math.PI * 2.0
        val dx = cos (angle) * radius
        val dy = sin (angle) * radius
        coords[i] = Coordinate (centre.x+dx, centre.y+dy)
    }
    coords[pointNum] = coords[0]
    return createPolygon(coords)
}
