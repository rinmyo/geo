package utils

import Can
import org.bukkit.Location
import org.locationtech.jts.geom.Coordinate
import org.locationtech.jts.geom.GeometryFactory
import org.locationtech.jts.geom.Point

fun Location.coordinate(): Coordinate = Coordinate(x, z)

fun Location.toJTSPoint(): Point = GeometryFactory().createPoint(coordinate())

fun Location.within(can: Can): Boolean = this.toJTSPoint().within(can.getData())