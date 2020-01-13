package utils

import builders.PolygonBuilder
import builders.ZoneBuilder
import enums.ContextType
import handlers.onPlayerChat
import handlers.onPlayerInteract
import hazae41.minecraft.kutils.bukkit.BukkitEventPriority
import hazae41.minecraft.kutils.bukkit.BukkitPlugin
import hazae41.minecraft.kutils.bukkit.listen
import hazae41.minecraft.kutils.bukkit.msg
import org.bukkit.entity.Player
import org.bukkit.event.player.AsyncPlayerChatEvent
import org.bukkit.event.player.PlayerInteractEvent

fun Player.createZone(plugin: BukkitPlugin) {
    val builder = ZoneBuilder().setPolygonBuilder(PolygonBuilder()).setFounder(this).setWorld(this.world)
    var context = ContextType.SETTING_NAME

    msg("Please input the name of the Zone that you will found: ")
    plugin.listen<AsyncPlayerChatEvent>(BukkitEventPriority.LOW) {
        if (it.player == this) context = onPlayerChat(builder, context, it)
    }

    plugin.listen<PlayerInteractEvent> {
        if (it.player == this) context = onPlayerInteract(builder, context, it)
    }
}
