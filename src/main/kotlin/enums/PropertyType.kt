package enums

import Can
import org.bukkit.entity.Player

/**
 * 下列權限皆是對原版玩家行為作出的一些限制
 */
enum class PropertyType {
    /**
     * 禁止玩家攻擊
     * 監聽所有攻擊事件，
     */
    DENY_PVP,

    /**
     * 禁止玩家受傷（免疫傷害）
     */
    DENY_PLAYER_INJURE,

    /**
     *禁止玩家進入
     */
    DENY_PLAYER_ENTRY,

    /**
     * 禁止玩家離開
     */
    DENY_PLAYER_LEAVE,

    /**
     * 禁止玩家操作方塊
     */
    DENY_BLOCK_OPERATION;

    /**
     * K 區域
     * V 例外玩家
     */
    val zones = mutableMapOf<Can, MutableSet<Player>>()
}