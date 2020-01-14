import builders.PolygonBuilder
import builders.ZoneBuilder
import com.mongodb.MongoClientSettings
import com.mongodb.MongoException
import com.mongodb.client.MongoClients
import com.mongodb.client.MongoCollection
import com.mongodb.client.MongoDatabase
import enums.PropertyType
import handlers.handleSettingZone
import hazae41.minecraft.kutils.bukkit.*
import org.bson.codecs.configuration.CodecRegistries.fromProviders
import org.bson.codecs.configuration.CodecRegistries.fromRegistries
import org.bson.codecs.pojo.PojoCodecProvider
import org.bukkit.entity.Player
import org.bukkit.event.entity.EntityDamageByEntityEvent
import org.bukkit.event.entity.EntityDamageEvent
import org.bukkit.event.player.AsyncPlayerChatEvent
import org.bukkit.event.player.PlayerInteractEvent
import org.bukkit.event.player.PlayerMoveEvent

class GeoMain : BukkitPlugin() {

    private lateinit var database: MongoDatabase

    private lateinit var zoneCollection: MongoCollection<Zone>

    private val createZoneSessionPool = mutableMapOf<Player, ZoneBuilder>()

    /**
     * 新會話
     */
    fun newCreateZoneSession(player: Player) {
        createZoneSessionPool[player] = ZoneBuilder().setFounder(player).setWorld(player.world).setPolygonBuilder(PolygonBuilder())
    }

    fun hasCreateZoneSession(player: Player) = createZoneSessionPool.containsKey(player)

    fun getCreateZoneSession(player: Player) = createZoneSessionPool[player]

    /**
     * 結束一個對話
     */
    fun finishCreateZoneSession(player: Player) {
        createZoneSessionPool.remove(player)
    }


    override fun onEnable() {

        /**
         * 嘗試連接MongoDB
         */
        try {
            val codec = fromRegistries(
                    MongoClientSettings.getDefaultCodecRegistry(),
                    fromProviders(PojoCodecProvider.builder().automatic(true).build())
            )
            database = MongoClients.create(MongoClientSettings.builder().codecRegistry(codec).build()).getDatabase("test")
            zoneCollection = database.getCollection("zone", Zone::class.java)
            info("連接MongoDB數據庫成功")

        } catch (e: MongoException) {
            warning("MongoDB連接失敗 \n$e")
        }


        listen<AsyncPlayerChatEvent>(BukkitEventPriority.LOWEST) { e ->
            if (createZoneSessionPool.containsKey(e.player)) {
                createZoneSessionPool[e.player] = handleSettingZone(createZoneSessionPool[e.player]!!, e)
            }
        }

        listen<PlayerInteractEvent>(BukkitEventPriority.LOWEST) { e ->

            if (createZoneSessionPool.containsKey(e.player)) {
                createZoneSessionPool[e.player] = handleSettingZone(createZoneSessionPool[e.player]!!, e)
            }

            PropertyType.DENY_BLOCK_OPERATION.zones.keys.forEach {
                schedule(true) {
                    if (it.has(e.player))
                        e.isCancelled = true
                }
            }
        }


        listen<PlayerMoveEvent>(BukkitEventPriority.LOWEST) { e ->
            PropertyType.DENY_PLAYER_ENTRY.zones.keys.forEach {
                schedule(true) {
                    if (it.isEntry(e))
                        e.isCancelled = true
                }
            }

            PropertyType.DENY_PLAYER_LEAVE.zones.keys.forEach {
                schedule(true) {
                    if (it.isLeave(e))
                        e.isCancelled = true
                }
            }
        }

        listen<EntityDamageByEntityEvent>(BukkitEventPriority.LOWEST) { e ->
            PropertyType.DENY_PVP.zones.keys.forEach {
                schedule(true) {
                    if (e.entity is Player && it.has(e.entity as Player) && e.damager is Player && it.has(e.damager as Player))
                        e.isCancelled = true
                }
            }
        }

        listen<EntityDamageEvent>(BukkitEventPriority.LOWEST) { e ->
            PropertyType.DENY_PLAYER_INJURE.zones.keys.forEach {
                schedule(true) {
                    if (e.entity is Player && it.has(e.entity))
                        e.isCancelled = true
                }
            }
        }

    }

    override fun onDisable() {
        info("add...")
    }

    override fun onLoad() {
        info("add...")
    }
}
