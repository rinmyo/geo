package utils

import Zone
import org.bukkit.Location
import org.locationtech.jts.geom.Coordinate
import org.locationtech.jts.geom.GeometryFactory
import org.locationtech.jts.geom.Point

fun Location.coordinate(): Coordinate = Coordinate(x, z, y)

fun Location.toJTSPoint(): Point = GeometryFactory().createPoint(coordinate())

fun Location.within(zone: Zone): Boolean = this.toJTSPoint().within(zone.getData())