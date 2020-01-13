import enums.ZoneType
import org.bson.Document
import org.bukkit.World
import org.bukkit.entity.Entity
import org.bukkit.entity.Player
import org.bukkit.event.player.PlayerMoveEvent
import org.locationtech.jts.geom.MultiPolygon
import java.util.*

/**
 * @param name 區域的名稱
 * @param founder 創建者
 * @param world 所在的世界
 * @param data 定義區域的地理數據
 * @param type 類型，為空則說明沒有類型
 * @param note 備注，為空則說明沒有備註
 *
 */
class Zone(
        val name: String,
        val founder: Player,
        val world: World,
        val data: MultiPolygon,
        val type: ZoneType?,
        val note: String?
) {
    /**
     * uuid 是唯一之識別碼
     */
    val uuid = UUID.randomUUID().toString()

    /**
     * 獲取
     */
    fun toDocument(): Document = Document("uuid", uuid)
            .append("name", name)
            .append("owner", founder)
            .append("data", data.toWKB())
            .append("note", note)
            .append("type", type)

    /**
     * 將一個新增的類注冊到插件中
     */
    fun register() = ZoneManager.register(this)

    fun isEntry(e: PlayerMoveEvent) = e.to?.toJTSPoint()?.within(data) == true && !e.from.toJTSPoint().within(data)

    fun isLeave(e: PlayerMoveEvent) = e.from.toJTSPoint().within(data) && e.to?.toJTSPoint()?.within(data) != false

    fun has(e: Entity) = e.location.within(this)

    fun getPlayers() = world.players.filter { has(it) }

    fun getEntities() = world.entities.filter { has(it) }
}
