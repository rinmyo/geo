import enums.CanType
import managers.CanManager
import org.bukkit.Bukkit
import org.bukkit.Location
import org.bukkit.entity.Entity
import org.bukkit.event.player.PlayerMoveEvent
import org.locationtech.jts.geom.MultiPolygon
import utils.geometryFrom
import utils.within
import java.util.*

/**
 * @param name 區域的名稱
 * @param uuid 區域唯一id
 * @param founderID 創建者
 * @param worldID 所在的世界
 * @param data 定義區域的地理數據
 * @param floor 區域的地板
 * @param ceil 區域的天花板
 * @param type 類型，為空則說明沒有類型
 * @param note 備注，為空則說明沒有備註
 *
 */
data class Can(
        val name: String,
        val uuid: UUID,
        private val founderID: UUID,
        private val worldID: UUID,
        private val data: String,
        val floor: Double,
        val ceil: Double,
        val type: CanType?,
        private val note: String?
) {
    /**
     * @return 這個can的構建人
     */
    fun getFounder() = Bukkit.getOfflinePlayer(founderID)

    /**
     * @return can所在的世界
     */
    fun getWorld() = Bukkit.getWorld(worldID)

    /**
     * @return 返回這個can的縱斷面積
     */
    fun getProfile() = geometryFrom(data) as MultiPolygon

    /**
     * @return 獲得高度
     */
    fun getHeight() = ceil - floor

    /**
     * @return 體積
     */
    fun getVolume() = getProfile().area * getHeight()

    /**
     * 註冊到罐頭管理員
     */
    fun register() = CanManager.register(this)

    /**
     * 判定某個玩家移動事件是否進入了罐頭
     */
    fun isEntry(e: PlayerMoveEvent) = !contain(e.from) && contain(e.to)

    /**
     * 判斷某個玩家移動事件是否離開了罐頭
     */
    fun isLeave(e: PlayerMoveEvent) =  contain(e.from) && !contain(e.to)

    /**
     * 檢查某個位置是否包含在罐頭中
     */
    fun contain(l: Location) = l.within(this) &&  l.y > floor && l.y < ceil

    /**
     * 檢查某個實體是否在罐頭中
     */
    fun has(e: Entity) = e.world == getWorld() && contain(e.location)

    /**
     * 獲得罐頭內的所有玩家
     */
    fun getPlayers() = getWorld()?.players?.filter { contain(it.location) }

    /**
     * 獲得罐頭內的所有實體
     */
    fun getEntities() = getWorld()?.entities?.filter { contain(it.location) }
}
