package handlers

import enums.ContextType
import builders.PolygonBuilder
import builders.ZoneBuilder
import hazae41.minecraft.kutils.bukkit.msg
import org.bukkit.Material
import org.bukkit.event.player.PlayerInteractEvent

fun onPlayerInteract(builder: ZoneBuilder, context: ContextType, event: PlayerInteractEvent) = when (context) {

    ContextType.SETTING_DATA ->
        if (event.hasBlock() && event.material == Material.GOLDEN_PICKAXE) {

            builder.getPolygonBuilder().addLocation(event.clickedBlock!!.location)
            event.player.msg("你添加了第${builder.getPolygonBuilder().number() - 1}個點")

            if (builder.getPolygonBuilder().isBuildable()) {
                builder.addPolygon(builder.getPolygonBuilder().build())
                builder.setPolygonBuilder(PolygonBuilder())     //每新建一個多邊形就要換一個新的builder
                event.player.msg("你添加了第 ${builder.getPolygons().size - 1} 個多邊形")
            }
            context
        } else
            context

    /**
     * 不是clickHandler處理的上下文
     */
    ContextType.SETTING_NAME,
    ContextType.CONFIRM_NAME,
    ContextType.SETTING_TYPE,
    ContextType.CONFIRM_TYPE,
    ContextType.CONFIRM_DATA -> context
}
