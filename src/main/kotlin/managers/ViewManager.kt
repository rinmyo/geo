package managers

import Can
import GeoMain
import Path
import builders.CanBuilder
import builders.PathBuilder
import exceptions.GeoException
import hazae41.minecraft.kutils.bukkit.schedule
import org.bukkit.Particle
import org.bukkit.entity.Player
import org.locationtech.jts.geom.Coordinate
import org.locationtech.jts.geom.CoordinateList
import org.locationtech.jts.geom.MultiPolygon
import utils.getDiscretePoints

/**
 * 視圖管理者
 */
object ViewManager {

    private val dynamicHeightCoordinates = mutableMapOf<Player, CoordinateList>()
    private val fixedHeightCoordinates = mutableMapOf<Player, CoordinateList>()

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
                if (canPool.containsKey(player)){
                    canPool[player]!!.add(what)
                }else{
                    canPool[player] = mutableSetOf(what)
                }
            }

            is PathBuilder -> {
                pathBuilderPool[player] = what
            }

            is Path -> {
                if (pathPool.containsKey(player)){
                    pathPool[player]!!.add(what)
                }else{
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
        view dynamicHeightCoordinates
         */
        plugin.schedule(period = 0.2.toLong()) {
            dynamicHeightCoordinates.forEach { (t, u) ->
                var p1: Coordinate = u[0]
                u.forEach { p2 ->
                    getDiscretePoints(p1, p2, 0.2).forEach {
                        if (p1 != p2) t.spawnParticle(Particle.LANDING_LAVA, it.x, t.location.y, it.y, 2)
                        p1 = p2
                    }
                }
            }
        }

        /*
        view fixedHeightCoordinates
         */
        plugin.schedule(period = 0.2.toLong()) {

        }
    }


}