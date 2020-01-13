import builders.PolygonBuilder
import builders.ZoneBuilder
import com.mongodb.client.MongoDatabase
import enums.ContextType
import handlers.onPlayerInteract
import handlers.onPlayerChat
import hazae41.minecraft.kutils.bukkit.BukkitEventPriority
import hazae41.minecraft.kutils.bukkit.BukkitPlugin
import hazae41.minecraft.kutils.bukkit.listen
import hazae41.minecraft.kutils.bukkit.msg
import org.bson.Document
import org.bukkit.Location
import org.bukkit.entity.Player
import org.bukkit.event.player.AsyncPlayerChatEvent
import org.bukkit.event.player.PlayerInteractEvent
import org.locationtech.jts.geom.Coordinate
import org.locationtech.jts.geom.Geometry
import org.locationtech.jts.geom.GeometryFactory
import org.locationtech.jts.geom.Point
import org.locationtech.jts.io.WKBReader
import org.locationtech.jts.io.WKBWriter

lateinit var database: MongoDatabase

fun Location.coordinate(): Coordinate = Coordinate(x, z, y)

fun Location.toJTSPoint(): Point = GeometryFactory().createPoint(coordinate())

fun Geometry.toWKB(): ByteArray = WKBWriter().write(this)

fun ByteArray.toGeometry(): Geometry = WKBReader().read(this)

fun Location.within(zone: Zone): Boolean = this.toJTSPoint().within(zone.data)

fun Document.toZone(): Zone = TODO()

fun Player.createZone(plugin: BukkitPlugin) {
    val builder = ZoneBuilder().setPolygonBuilder(PolygonBuilder()).setFounder(this).setWorld(this.world)
    var context = ContextType.SETTING_NAME

    msg("Please input the name of the Zone that you will found: ")
    plugin.listen<AsyncPlayerChatEvent>(BukkitEventPriority.LOW) {
        if (it.player == this) context = onPlayerChat(builder, context, it)
    }

    plugin.listen<PlayerInteractEvent> {
        if (it.player == this) context = onPlayerInteract(builder, context, it)
    }
}




