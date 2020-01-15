import managers.PathManager
import org.bukkit.Bukkit
import org.locationtech.jts.geom.LineString
import utils.geometryFrom
import java.util.*

/**
 * @param name 區域的名稱
 * @param uuid 區域唯一id
 * @param founderID 創建者
 * @param worldID 所在的世界
 * @param data 定義區域的地理數據
 * @param note 備注，為空則說明沒有備註
 *
 */
data class Path(
        val name: String,
        val uuid: UUID,
        private val founderID: UUID,
        private val worldID: UUID,
        private val data: String,
        private val note: String?
) {
    fun getFounder() = Bukkit.getOfflinePlayer(founderID)

    fun getWorld() = Bukkit.getWorld(worldID)

    fun getData() = geometryFrom(data) as LineString

    fun register() = PathManager.register(this)
}