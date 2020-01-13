package builders

import Zone
import enums.ZoneType
import org.bukkit.World
import org.bukkit.entity.Player
import org.locationtech.jts.geom.GeometryFactory
import org.locationtech.jts.geom.MultiPolygon
import org.locationtech.jts.geom.Polygon

class ZoneBuilder() {

    private lateinit var polygonBuilder : PolygonBuilder

    fun setPolygonBuilder(polygonBuilder: PolygonBuilder): ZoneBuilder {
        this.polygonBuilder = polygonBuilder
        return this
    }

    fun getPolygonBuilder() = polygonBuilder


    private var name: String? = null

    fun setName(name: String): ZoneBuilder {
        this.name = name
        return this
    }

    fun getName() = name


    private var founder: Player? = null

    fun setFounder(founder: Player): ZoneBuilder {
        this.founder = founder
        return this
    }

    fun getFounder() = founder


    private var world: World? = null

    fun setWorld(world: World): ZoneBuilder {
        this.world = world
        return this
    }

    fun getWorld() = world


    private var type: ZoneType? = null

    fun setType(type: ZoneType?): ZoneBuilder {
        this.type = type
        return this
    }

    fun getType() = type


    private var note: String? = null

    fun setNote(note: String): ZoneBuilder {
        this.note = note
        return this
    }

    fun getNote() = note


    private var data: MultiPolygon? = null
    private val polygons = arrayListOf<Polygon>()

    fun addPolygon(polygon: Polygon): ZoneBuilder {
        polygons.add(polygon)
        this.data = GeometryFactory().createMultiPolygon(polygons.toTypedArray())
        return this
    }

    fun getPolygons() = polygons



    fun build(): Zone = Zone(name!!, founder!!, world!!, data!!, type, note)
}