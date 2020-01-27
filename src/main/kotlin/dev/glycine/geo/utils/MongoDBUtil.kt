package dev.glycine.geo.utils

import org.locationtech.jts.geom.Geometry as JGeometry
import com.mongodb.client.model.geojson.Geometry as MGeometry
import org.locationtech.jts.io.geojson.GeoJsonReader

fun MGeometry.toJGeometry(): JGeometry = GeoJsonReader().read(this.toJson())