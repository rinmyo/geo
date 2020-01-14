package handlers

import builders.PolygonBuilder
import builders.ZoneBuilder
import enums.ZoneType
import enums.contexts.SettingZoneContext
import org.bukkit.Material
import org.bukkit.event.block.Action
import org.bukkit.event.player.AsyncPlayerChatEvent
import org.bukkit.event.player.PlayerInteractEvent
import utils.msg

fun handleSettingZone(builder: ZoneBuilder, chatEvent: AsyncPlayerChatEvent): ZoneBuilder {
    chatEvent.isCancelled = true

    when (builder.getContext()) {
        /**
         * 輸入名字
         */
        SettingZoneContext.SETTING_ZONE_NAME -> {
            builder.setName(chatEvent.message)
            chatEvent.player.msg("What you input is: ${builder.getName()} (Y/N)?")
            return builder.setContext(builder.getContext() + 1)
        }

        /**
         * 確認名字
         */
        SettingZoneContext.CONFIRM_ZONE_NAME -> return when (chatEvent.message.toUpperCase()) {
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

        /**
         * 輸入類型
         */
        SettingZoneContext.SETTING_ZONE_TYPE -> {
            val num = chatEvent.message.toIntOrNull()
            return if (num != null) {
                if (num == 0) {
                    builder.setType(null)
                } else if (num > 0 && num < enumValues<ZoneType>().size) {
                    builder.setType(enumValues<ZoneType>().associateBy { it.ordinal }[num])
                }
                chatEvent.player.msg("What you input is: ${builder.getType()} (Y/N)?")
                builder.setContext(builder.getContext() + 1)
            } else {
                chatEvent.player.msg("You must input the number of the above list!")
                builder
            }
        }

        /**
         * 確認類型
         */
        SettingZoneContext.CONFIRM_ZONE_TYPE -> return when (chatEvent.message.toUpperCase()) {
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


        /**
         * 設置數據
         */
        SettingZoneContext.SETTING_ZONE_DATA -> return if (chatEvent.message.toUpperCase() == "STOP") {
            var warning = ""
            val cacheNum = builder.getPolygonBuilder().number()
            if (cacheNum != 0) warning = "注意！還有 $cacheNum 個點尚未構建成新的多邊形，將被丟棄，"
            chatEvent.player.msg("$warning 確認已經完成嗎？ (Y/N)")
            builder.setContext(builder.getContext() + 1)
        } else builder


        /**
         * 確認數據
         */
        SettingZoneContext.CONFIRM_ZONE_DATA -> return when (chatEvent.message.toUpperCase()) {
            "Y", "YES" -> {
                chatEvent.player.msg("現在請設定區域的高度, 用金鎬點擊天花板，或者輸入一個整數")
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

        /*
        設定區域下界
         */
        SettingZoneContext.SETTING_HEIGHT -> return if (chatEvent.message.toIntOrNull() != null) {
            builder.ceil = builder.floor + chatEvent.message.toInt()
            chatEvent.player.msg("Zone is from ${builder.floor} to ${builder.ceil} (y/N)?")
            builder.setContext(builder.getContext() + 1)
        } else {
            chatEvent.player.msg("You must input a INTEGER!")
            builder
        }


        /*
         確認高度
         */

        SettingZoneContext.CONFIRM_HEIGHT -> return when (chatEvent.message.toUpperCase()) {
            "Y", "YES" -> {
                chatEvent.player.msg("現在你可以為剛剛新建的區域設定一些描述")
                builder.setContext(builder.getContext() + 1)
            }

            "N", "NO" -> {
                chatEvent.player.msg("你可以重新設置高度了")
                builder.setContext(builder.getContext() - 1)
            }

            else -> {
                chatEvent.player.msg("wrong input, please input \'y\' or \'n\'")
                builder
            }
        }

        /**
         * 設置備註
         */
        SettingZoneContext.SETTING_ZONE_NOTE -> {
            builder.setNote(chatEvent.message)
            chatEvent.player.msg("What you input is: ${builder.getNote()} (y/N)?")
            return builder.setContext(builder.getContext() + 1)
        }

        /**
         * 確認備註
         */
        SettingZoneContext.CONFIRM_ZONE_NOTE -> return when (chatEvent.message.toUpperCase()) {
            "Y", "YES" -> {
                chatEvent.player.msg("全部設定完了, 現在輸入'/geo done'來結束這一切")
                builder.setDone()
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
    }
}

fun handleSettingZone(builder: ZoneBuilder, event: PlayerInteractEvent): ZoneBuilder {
    when (builder.getContext()) {

        SettingZoneContext.SETTING_ZONE_DATA -> {
            if (event.hasBlock() && event.action == Action.LEFT_CLICK_BLOCK && event.material == Material.GOLDEN_PICKAXE) {
                event.isCancelled = true
                builder.getPolygonBuilder().addLocation(event.clickedBlock!!.location)  //這裡只用到了二維座標
                builder.floor = event.clickedBlock!!.location.y  //設定地板
                event.player.msg("你為第 ${builder.getPolygons().size + 1} 個多邊形添加了第 ${builder.getPolygonBuilder().number()} 個點")

                if (builder.getPolygonBuilder().isBuildable()) {
                    builder.addPolygon(builder.getPolygonBuilder().build())
                    builder.setPolygonBuilder(PolygonBuilder())     //每新建一個多邊形就要換一個新的builder
                    event.player.msg("你已經添加了 ${builder.getPolygons().size} 個多邊形")
                }
            }
            return builder
        }

        SettingZoneContext.SETTING_HEIGHT -> {
            if (event.hasBlock() && event.action == Action.LEFT_CLICK_BLOCK && event.material == Material.GOLDEN_PICKAXE) {
                event.isCancelled = true
                builder.ceil = event.clickedBlock!!.location.y
                event.player.msg("區域是從 ${builder.floor} 到 ${builder.ceil} (y/N)?")
                return builder.setContext(builder.getContext() + 1)
            }
            return builder
        }

        else -> return builder
    }
}
