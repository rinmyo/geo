package dev.glycine.geo

import hazae41.minecraft.kutils.bukkit.PluginConfigFile

object Config: PluginConfigFile("config"){
    const val currentVer = "0.0.2"

    val ver by string("version")
    val debug by boolean("plugin.debug")
    val host by string("MongoDB.host")
    val port by string("MongoDB.port")
    val db by string("MongoDB.database")
}