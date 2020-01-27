package dev.glycine.geo.handlers

import dev.glycine.geo.enums.PropertyType
import hazae41.minecraft.kutils.bukkit.*
import dev.glycine.geo.managers.SessionManager
import org.bukkit.entity.Player
import org.bukkit.event.entity.EntityDamageByEntityEvent
import org.bukkit.event.entity.EntityDamageEvent
import org.bukkit.event.player.AsyncPlayerChatEvent
import org.bukkit.event.player.PlayerInteractEvent
import org.bukkit.event.player.PlayerMoveEvent

fun registerListeners(plugin: BukkitPlugin) {
    plugin.listen<AsyncPlayerChatEvent>(BukkitEventPriority.LOWEST) { e ->
        //檢查玩家是否有正在進行的zone會話
        if (SessionManager.hasSession(e.player)) {
            SessionManager.getSession(e.player)!!.handleEvent(e)
        }
    }

    plugin.listen<PlayerInteractEvent>(BukkitEventPriority.LOWEST) { e ->

        if (SessionManager.hasSession(e.player)) {
            SessionManager.getSession(e.player)!!.handleEvent(e)
        }

        PropertyType.DENY_BLOCK_OPERATION.cans.forEach {
            if (e.hasBlock() && it.contain(e.clickedBlock!!)) {
                plugin.info("玩家破壞了，但取消了")
                e.isCancelled = true
            }
        }
    }

    plugin.listen<PlayerMoveEvent>(BukkitEventPriority.LOWEST) { e ->
        PropertyType.DENY_PLAYER_ENTRY.cans.forEach {
            if (it.isEntry(e)) {
                e.isCancelled = true
            }
        }

        PropertyType.DENY_PLAYER_LEAVE.cans.forEach {
            if (it.isLeave(e)) {
                e.isCancelled = true
            }
        }
    }

    plugin.listen<EntityDamageByEntityEvent>(BukkitEventPriority.LOWEST) { e ->
        PropertyType.DENY_PVP.cans.forEach {
            if (e.entity is Player && it.contain(e.entity as Player) && e.damager is Player && it.contain(e.damager as Player)) {
                e.isCancelled = true
            }
        }
    }

    plugin.listen<EntityDamageEvent>(BukkitEventPriority.LOWEST) { e ->
        PropertyType.DENY_PLAYER_INJURE.cans.forEach {
            plugin.schedule(true) {
                if (e.entity is Player && it.contain(e.entity)) {
                    e.isCancelled = true
                }
            }
        }
    }
}