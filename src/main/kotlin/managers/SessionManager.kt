package managers

import Path
import Can
import builders.PathBuilder
import builders.PolygonBuilder
import builders.ZoneBuilder
import enums.contexts.SettingZoneContext
import org.bukkit.entity.Player

object SessionManager {

    val zoneSessionPool = mutableMapOf<Player, ZoneBuilder>()
    val pathSessionPool = mutableMapOf<Player, PathBuilder>()

    inline fun <reified T> newSession(player: Player) {
        when (T::class){
            Can::class -> zoneSessionPool[player] = ZoneBuilder().setContext(SettingZoneContext.SETTING_ZONE_NAME).setFounder(player).setWorld(player.world).setPolygonBuilder(PolygonBuilder())
            Path::class -> pathSessionPool[player] = PathBuilder()
        }
    }

    fun updateSession(player: Player, zoneBuilder: ZoneBuilder){
        zoneSessionPool[player] = zoneBuilder
    }

    fun hasSession(player: Player) = zoneSessionPool.containsKey(player)

    fun getSession(player: Player) = zoneSessionPool[player]

    fun finishSession(player: Player) {
        zoneSessionPool.remove(player)
    }
}