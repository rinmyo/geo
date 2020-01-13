import com.mongodb.client.model.geojson.MultiPolygon
import enums.ZoneType
import org.bson.Document
import org.bukkit.Bukkit
import org.bukkit.entity.Entity
import org.bukkit.event.player.PlayerMoveEvent
import org.locationtech.jts.geom.Geometry
import utils.geometryFrom
import utils.toJGeometry
import utils.toJTSPoint
import utils.within
import java.util.*

/**
 * @param name 區域的名稱
 * @param uuid 區域唯一id
 * @param founderID 創建者
 * @param worldID 所在的世界
 * @param data 定義區域的地理數據
 * @param type 類型，為空則說明沒有類型
 * @param note 備注，為空則說明沒有備註
 *
 */
data class Zone(
        val name: String,
        val uuid: UUID,
        private val founderID: UUID,
        private val worldID: UUID,
        private val data: String,
        val type: ZoneType?,
        private val note: String?
) {
    fun getFounder() = Bukkit.getOfflinePlayer(founderID)

    fun getWorld() = Bukkit.getWorld(worldID)

    fun getData() = geometryFrom(data)

    fun register() = ZoneManager.register(this)

    fun isEntry(e: PlayerMoveEvent) = e.to?.toJTSPoint()?.within(getData()) == true && !e.from.toJTSPoint().within(getData())

    fun isLeave(e: PlayerMoveEvent) = e.from.toJTSPoint().within(getData()) && e.to?.toJTSPoint()?.within(getData()) != false

    fun has(e: Entity) = e.location.within(this)

    fun getPlayers() = getWorld()?.players?.filter { has(it) }

    fun getEntities() = getWorld()?.entities?.filter { has(it) }
}
