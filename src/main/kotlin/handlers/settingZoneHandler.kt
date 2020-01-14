package handlers

import builders.PolygonBuilder
import builders.ZoneBuilder
import enums.ZoneType
import enums.contexts.SettingZoneContext
import hazae41.minecraft.kutils.bukkit.msg
import org.bukkit.Material
import org.bukkit.event.block.Action
import org.bukkit.event.player.AsyncPlayerChatEvent
import org.bukkit.event.player.PlayerInteractEvent

fun handleSettingZone(builder: ZoneBuilder, chatEvent: AsyncPlayerChatEvent) = when (builder.getContext()) {

    /**
     * 輸入名字
     */
    SettingZoneContext.SETTING_ZONE_NAME -> {
        builder.setName(chatEvent.message)
        chatEvent.player.msg("What you input is: ${builder.getName()} (y/N)?")
        builder.setContext(builder.getContext() + 1)
    }

    /**
     * 確認名字
     */
    SettingZoneContext.CONFIRM_ZONE_NAME -> {
        when (chatEvent.message.toUpperCase()) {
            "Y", "YES" -> {
                chatEvent.player.msg("0>   NULL")
                enumValues<ZoneType>().forEach { type ->
                    chatEvent.player.msg("${type.ordinal + 1}>   ${type.name}")
                }
                chatEvent.player.msg("OK, What Type of Zone would you like to set? \n Please Choose One Of The Below TYPEs, and input its number")
                builder.setContext(builder.getContext() + 1)
            }

            "N", "NO" -> {
                chatEvent.player.msg("OK, Do not make the mistake again :), reset the name")
                builder.setContext(builder.getContext() - 1)
            }

            else -> {
                chatEvent.player.msg("wrong input, please input \'y\' or \'n\'")
                builder
            }
        }
    }

    /**
     * 輸入類型
     */
    SettingZoneContext.SETTING_ZONE_TYPE -> {
        val num = chatEvent.message.toIntOrNull()
        if (num != null) {
            if (num == 0) {
                builder.setType(null)
            } else if (num > 0 && num < enumValues<ZoneType>().size) {
                builder.setType(enumValues<ZoneType>().associateBy { it.ordinal }[num])
            }
            chatEvent.player.msg("What you input is: ${builder.getType()} (y/N)?")
            builder.setContext(builder.getContext() + 1)
        } else {
            chatEvent.player.msg("You must input the number of the above list!")
            builder
        }
    }

    /**
     * 確認類型
     */
    SettingZoneContext.CONFIRM_ZONE_TYPE -> when (chatEvent.message.toUpperCase()) {
        "Y", "YES" -> {
            chatEvent.player.msg("We will set some polygon next, Please Use GOLDEN_PICKAX click block in turns")
            builder.setContext(builder.getContext() + 1)
        }

        "N", "NO" -> {
            chatEvent.player.msg("OK, Do not make the mistake again :), reset the name")
            builder.setContext(builder.getContext() - 1)
        }

        else -> {
            chatEvent.player.msg("wrong input, please input \'y\' or \'n\'")
            builder
        }
    }


    SettingZoneContext.SETTING_ZONE_DATA -> if (chatEvent.message.toUpperCase() == "STOP") {
        chatEvent.player.msg("設置完所有的多邊形了嗎？ (Y/n)")
        builder.setContext(builder.getContext() + 1)
    } else builder


    SettingZoneContext.CONFIRM_ZONE_DATA -> when (chatEvent.message.toUpperCase()) {
        "Y", "YES" -> {
            chatEvent.player.msg("現在你可以為新增的 ${builder.getName()} 設定一些描述或備註")
            builder.setContext(builder.getContext() + 1)
        }

        "N", "NO" -> {
            chatEvent.player.msg("Now, you can reset the points")
            builder.setContext(builder.getContext() - 1)
        }

        else -> {
            chatEvent.player.msg("wrong input, please input \'y\' or \'n\'")
            builder
        }
    }

    SettingZoneContext.SETTING_ZONE_NOTE -> {
        builder.setNote(chatEvent.message)
        chatEvent.player.msg("What you input is: ${builder.getNote()} (y/N)?")
        builder.setContext(builder.getContext() + 1)
    }

    SettingZoneContext.CONFIRM_ZONE_NOTE -> when (chatEvent.message.toUpperCase()) {
        "Y", "YES" -> {
            chatEvent.player.msg("全部設定完了, ")
            builder.setContext(builder.getContext() + 1)
        }

        "N", "NO" -> {
            chatEvent.player.msg("現在你可以重新設定註釋/描述")
            builder.setContext(builder.getContext() - 1)
        }

        else -> {
            chatEvent.player.msg("wrong input, please input \'y\' or \'n\'")
            builder
        }
    }

    SettingZoneContext.SETTING_ZONE_DONE -> {
        builder.setDone()
    }
}

fun handleSettingZone(builder: ZoneBuilder, event: PlayerInteractEvent) = when (builder.getContext()) {

    SettingZoneContext.SETTING_ZONE_DATA ->

        if (event.hasBlock() && event.action == Action.LEFT_CLICK_BLOCK && event.material == Material.GOLDEN_PICKAXE) {
            event.isCancelled = true
            builder.getPolygonBuilder().addLocation(event.clickedBlock!!.location)
            event.player.msg("你添加了第${builder.getPolygonBuilder().number() - 1}個點")

            if (builder.getPolygonBuilder().isBuildable()) {
                builder.addPolygon(builder.getPolygonBuilder().build())
                builder.setPolygonBuilder(PolygonBuilder())     //每新建一個多邊形就要換一個新的builder
                event.player.msg("你添加了第 ${builder.getPolygons().size - 1} 個多邊形")
            }
            builder
        } else {
            builder
        }


    /**
     * 不是clickHandler處理的上下文
     */
    SettingZoneContext.SETTING_ZONE_NAME,
    SettingZoneContext.CONFIRM_ZONE_NAME,
    SettingZoneContext.SETTING_ZONE_TYPE,
    SettingZoneContext.CONFIRM_ZONE_TYPE,
    SettingZoneContext.CONFIRM_ZONE_DATA,
    SettingZoneContext.SETTING_ZONE_NOTE,
    SettingZoneContext.CONFIRM_ZONE_NOTE,
    SettingZoneContext.SETTING_ZONE_DONE
    -> builder
}