package handlers

import enums.ContextType
import builders.ZoneBuilder
import enums.ZoneType
import hazae41.minecraft.kutils.bukkit.msg
import org.bukkit.event.player.AsyncPlayerChatEvent

fun onPlayerChat(builder: ZoneBuilder, context: ContextType, chatEvent: AsyncPlayerChatEvent) = when (context) {

    /**
     * 輸入名字
     */
    ContextType.SETTING_NAME -> {
        builder.setName(chatEvent.message)
        chatEvent.player.msg("What you input is: ${builder.getName()} (y/N)?")
        context + 1
    }

    /**
     * 確認名字
     */
    ContextType.CONFIRM_NAME -> {
        when (chatEvent.message.toUpperCase()) {
            "Y", "YES" -> {
                chatEvent.player.msg("0>   NULL")
                enumValues<ZoneType>().forEach { type ->
                    chatEvent.player.msg("${type.ordinal + 1}>   ${type.name}")
                }
                chatEvent.player.msg("OK, What Type of Zone would you like to set? \n Please Choose One Of The Below TYPEs, and input its number")
                context + 1
            }

            "N", "NO" -> {
                chatEvent.player.msg("OK, Do not make the mistake again :), reset the name")
                context - 1
            }

            else -> {
                chatEvent.player.msg("wrong input, please input \'y\' or \'n\'")
                context
            }
        }
    }

    /**
     * 輸入類型
     */
    ContextType.SETTING_TYPE -> {
        val num = chatEvent.message.toIntOrNull()
        if (num != null) {
            if (num == 0) {
                builder.setType(null)
            } else if (num > 0 && num < enumValues<ZoneType>().size) {
                builder.setType(enumValues<ZoneType>().associateBy { it.ordinal }[num])
            }
            chatEvent.player.msg("What you input is: ${builder.getType()} (y/N)?")
            context + 1
        } else {
            chatEvent.player.msg("You must input the number of the above list!")
            context
        }
    }

    /**
     * 確認類型
     */
    ContextType.CONFIRM_TYPE -> when (chatEvent.message.toUpperCase()) {
        "Y", "YES" -> {
            chatEvent.player.msg("We will set some polygon next, Please Use GOLDEN_PICKAX click block in turns")
            context + 1
        }

        "N", "NO" -> {
            chatEvent.player.msg("OK, Do not make the mistake again :), reset the name")
            context - 1
        }

        else -> {
            chatEvent.player.msg("wrong input, please input \'y\' or \'n\'")
            context
        }
    }


    ContextType.SETTING_DATA -> if (chatEvent.message.toUpperCase() == "STOP") {
        chatEvent.player.msg("設置完所有的多邊形了嗎？ (Y/n)")
        context + 1
    } else context

    ContextType.CONFIRM_DATA -> when (chatEvent.message.toUpperCase()) {
        "Y", "YES" -> {
            chatEvent.player.msg("設定完了 ${builder.getName()}")
            context + 1
        }

        "N", "NO" -> {
            chatEvent.player.msg("Now, you can reset the points")
            context - 1
        }

        else -> {
            chatEvent.player.msg("wrong input, please input \'y\' or \'n\'")
            context
        }
    }


}
