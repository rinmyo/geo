package builders

import Zone
import enums.ZoneType
import enums.contexts.SettingZoneContext
import org.bukkit.World
import org.bukkit.entity.Player
import org.locationtech.jts.geom.GeometryFactory
import org.locationtech.jts.geom.MultiPolygon
import org.locationtech.jts.geom.Polygon
import utils.toGeoJSON
import java.util.*
import java.util.zip.DeflaterOutputStream

class ZoneBuilder {

    private lateinit var context: SettingZoneContext

    fun setContext(context: SettingZoneContext): ZoneBuilder {
        this.context = context
        return this
    }

    fun getContext() = context


    private lateinit var polygonBuilder: PolygonBuilder

    fun setPolygonBuilder(polygonBuilder: PolygonBuilder): ZoneBuilder {
        this.polygonBuilder = polygonBuilder
        return this
    }

    fun getPolygonBuilder() = polygonBuilder


    private lateinit var name: String

    fun setName(name: String): ZoneBuilder {
        this.name = name
        return this
    }

    fun getName() = name


    private lateinit var founder: Player

    fun setFounder(founder: Player): ZoneBuilder {
        this.founder = founder
        return this
    }


    private lateinit var world: World

    fun setWorld(world: World): ZoneBuilder {
        this.world = world
        return this
    }


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


    private lateinit var data: MultiPolygon
    private val polygons = arrayListOf<Polygon>()

    fun addPolygon(polygon: Polygon): ZoneBuilder {
        polygons.add(polygon)
        this.data = GeometryFactory().createMultiPolygon(polygons.toTypedArray())
        return this
    }

    fun getPolygons() = polygons

    var floor = 0.0

    var ceil = 0.0

    private var settingDone = false

    fun setDone(): ZoneBuilder {
        settingDone = true
        return this
    }

    fun isDone() = settingDone

    fun build(): Zone = Zone(name, UUID.randomUUID(), founder.uniqueId, world.uid, data.toGeoJSON(), floor, ceil, type, note)
}