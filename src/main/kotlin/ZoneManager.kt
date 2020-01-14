import com.mongodb.client.MongoCollection
import com.mongodb.client.MongoDatabase
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

    fun loadAllFrom(col: MongoCollection<Zone>){
        col.find().forEach { it.register() }
    }

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


}