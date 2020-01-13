import enums.PropertyType
import hazae41.minecraft.kutils.bukkit.BukkitEventPriority
import hazae41.minecraft.kutils.bukkit.BukkitPlugin
import hazae41.minecraft.kutils.bukkit.listen
import hazae41.minecraft.kutils.bukkit.schedule
import org.bukkit.entity.Player
import org.bukkit.event.entity.EntityDamageByEntityEvent
import org.bukkit.event.entity.EntityDamageEvent
import org.bukkit.event.player.PlayerInteractEvent
import org.bukkit.event.player.PlayerMoveEvent

object ZoneManager {

    /**
     * 本manager所轄的區域
     */
    val zoneSet = mutableSetOf<Zone>()

    /**
     * MongoDB的集合
     */
    private val col = database.getCollection("Zone") //存放zone的集合

    /**
     * 從MongoDB中加載數據
     */
    fun loadZoneData() {
        col.find().forEach {
            register(it.toZone())
        }
    }

//    fun getZoneByName(name: String): MongoIterable<AbstractZone> = col.find(Filters.eq("name", name)).map {it.toZone()}

    fun register(zone: Zone) {
        //添加到區域集合中
        zoneSet.add(zone)

        if(zone.type != null){
            //權限映射中加上這個區域
            zone.type.properties.forEach {
                it.zones.putIfAbsent(zone, mutableSetOf())
            }
        }
    }

    /**
     * 對區域屬性的實現
     */
    fun implementProperties(plugin: BukkitPlugin) {
        plugin.listen<PlayerMoveEvent>(BukkitEventPriority.LOWEST) { e ->
            PropertyType.DENY_PLAYER_ENTRY.zones.keys.forEach {
                plugin.schedule(true) {
                    if (it.isEntry(e))
                        e.isCancelled = true
                }
            }

            PropertyType.DENY_PLAYER_LEAVE.zones.keys.forEach {
                plugin.schedule(true) {
                    if (it.isLeave(e))
                        e.isCancelled = true
                }
            }
        }


        /**
         * 禁止PVP
         */
        plugin.listen<EntityDamageByEntityEvent>(BukkitEventPriority.HIGHEST) { e ->
            PropertyType.DENY_PVP.zones.keys.forEach {
                plugin.schedule(true) {
                    if (e.entity is Player && it.has(e.entity as Player) && e.damager is Player && it.has(e.damager as Player))
                        e.isCancelled = true
                }
            }
        }

        /**
         * DENY_PLAYER_INJURE
         */
        plugin.listen<EntityDamageEvent>(BukkitEventPriority.HIGHEST) { e ->
            PropertyType.DENY_PLAYER_INJURE.zones.keys.forEach {
                plugin.schedule(true) {
                    if (e.entity is Player && it.has(e.entity))
                        e.isCancelled = true
                }
            }
        }

        /**
         * 禁止操作方塊
         */
        plugin.listen<PlayerInteractEvent> { e ->
            PropertyType.DENY_BLOCK_OPERATION.zones.keys.forEach {
                plugin.schedule(true) {
                    if (it.has(e.player))
                        e.isCancelled = true
                }
            }
        }


    }


}