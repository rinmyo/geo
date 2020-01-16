package managers

import Can
import GeoMain
import Path
import builders.CanBuilder
import builders.PathBuilder
import hazae41.minecraft.kutils.bukkit.schedule
import org.bukkit.Particle
import org.bukkit.entity.Player
import org.locationtech.jts.geom.Coordinate
import utils.getDiscreteCoordinates

/**
 * 視圖管理者
 */
object ViewManager {

    private val canPool = mutableMapOf<Player, MutableSet<Can>>()
    private val canBuilderPool = mutableMapOf<Player, CanBuilder>()
    private val pathPool = mutableMapOf<Player, MutableSet<Path>>()
    private val pathBuilderPool = mutableMapOf<Player, PathBuilder>()

    /**
     * 新增一個顯示
     */
    fun newView(player: Player, what: Any) {
        when (what) {
            is CanBuilder -> {
                canBuilderPool[player] = what
            }

            is Can -> {
                if (canPool.containsKey(player)) {
                    canPool[player]!!.add(what)
                } else {
                    canPool[player] = mutableSetOf(what)
                }
            }

            is PathBuilder -> {
                pathBuilderPool[player] = what
            }

            is Path -> {
                if (pathPool.containsKey(player)) {
                    pathPool[player]!!.add(what)
                } else {
                    pathPool[player] = mutableSetOf(what)
                }
            }
        }
    }

    /**
     * 高度隨玩家而動
     */
    fun view(plugin: GeoMain) {
        /*
        view CAN
         */
        plugin.schedule(period = 0.1.toLong()) {
            canPool.forEach { (p, c) ->
                c.forEach { can ->
                    var p1: Coordinate = can.getProfile().coordinates[0]
                    can.getProfile().coordinates.forEach { p2 ->
                        getDiscreteCoordinates(p1, p2, 0.2).forEach {
                            if (p1 != p2) {
                                val y = if (p.location.y < can.floor) can.floor else if (p.location.y > can.ceil) can.ceil else p.location.y
                                p.spawnParticle(Particle.LANDING_LAVA, it.x, y, it.y, 2)
                            }
                            p1 = p2
                        }
                    }
                }
            }
        }

        plugin.schedule(period = 0.1.toLong()) {
            canPool.forEach { (p, c) ->
                c.forEach { can ->
                    
                }
            }
        }
    }


}
