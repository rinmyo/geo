package managers

import GeoMain
import Path
import Zone
import hazae41.minecraft.kutils.bukkit.schedule
import org.bukkit.Particle
import org.bukkit.entity.Player

/**
 * 視圖管理者
 */
object ViewManager {

    val zone2DViewsPool = mutableMapOf<Player, Zone>()
    val zone3DViewsPool = mutableMapOf<Player, Zone>()
    val pathViewsPool = mutableMapOf<Player, Path>()

    fun viewZone2D(plugin: GeoMain){
        plugin.schedule(period = 0.2.toLong())  {
            TODO()
        }
    }

    fun viewZone3D(plugin: GeoMain){
        TODO()

    }

    fun viewPath(plugin: GeoMain){
        TODO()
    }

}