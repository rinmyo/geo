package managers

import Path
import Can
import builders.PathBuilder
import builders.PolygonBuilder
import builders.CanBuilder
import enums.contexts.SettingCanContext
import org.bukkit.entity.Player

object SessionManager {

    val zoneSessionPool = mutableMapOf<Player, CanBuilder>()
    val pathSessionPool = mutableMapOf<Player, PathBuilder>()

    inline fun <reified T> newSession(player: Player) {
        when (T::class){
            Can::class -> zoneSessionPool[player] = CanBuilder().setContext(SettingCanContext.SETTING_NAME).setFounder(player).setWorld(player.world).setPolygonBuilder(PolygonBuilder())
            Path::class -> pathSessionPool[player] = PathBuilder()
        }
    }

    fun updateSession(player: Player, canBuilder: CanBuilder){
        zoneSessionPool[player] = canBuilder
    }

    fun hasSession(player: Player) = zoneSessionPool.containsKey(player)

    fun getSession(player: Player) = zoneSessionPool[player]

    fun finishSession(player: Player) {
        zoneSessionPool.remove(player)
    }
}