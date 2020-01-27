package dev.glycine.geo.utils

import hazae41.minecraft.kutils.bukkit.BukkitSender
import hazae41.minecraft.kutils.bukkit.msg

fun BukkitSender.msg(msg: String?){
    this.msg("&6&n[GEO] $msg")
}