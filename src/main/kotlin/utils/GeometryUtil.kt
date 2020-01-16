package utils

import org.locationtech.jts.geom.*
import org.locationtech.jts.io.geojson.GeoJsonReader
import org.locationtech.jts.io.geojson.GeoJsonWriter
import kotlin.math.abs
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

fun getDiscreteCoordinates(p1: Coordinate, p2: Coordinate, dl: Double): Array<Coordinate>{
    val l = p1.distance3D(p2)
    val num = (l/dl).toInt()
    val dx = dl * (p2.x - p1.x) / l
    val dy = dl * (p2.y - p1.y) / l
    val dz = dl * (p2.getZ() - p1.getZ()) / l
    return (0..num).map { Coordinate(p1.x + dx, p1.y + dy, p1.getZ() + dz) }.toTypedArray()
}