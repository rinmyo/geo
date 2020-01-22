package builders

import Can
import enums.CanType
import org.bukkit.Material
import org.bukkit.World
import org.bukkit.entity.Player
import org.bukkit.event.block.Action
import org.bukkit.event.player.AsyncPlayerChatEvent
import org.bukkit.event.player.PlayerInteractEvent
import org.bukkit.inventory.ItemStack
import org.locationtech.jts.geom.GeometryFactory
import org.locationtech.jts.geom.MultiPolygon
import org.locationtech.jts.geom.Polygon
import utils.msg
import utils.toGeoJSON
import java.util.*
import enums.contexts.SettingCanContext as Context

class CanBuilder {

    private lateinit var context: Context

    fun setContext(context: Context): CanBuilder {
        this.context = context
        return this
    }

    private lateinit var polygonBuilder: PolygonBuilder

    fun setPolygonBuilder(polygonBuilder: PolygonBuilder): CanBuilder {
        this.polygonBuilder = polygonBuilder
        return this
    }

    private fun getPolygonBuilder() = polygonBuilder


    private lateinit var name: String

    private lateinit var founder: Player

    fun setFounder(founder: Player): CanBuilder {
        this.founder = founder
        return this
    }


    private lateinit var world: World

    fun setWorld(world: World): CanBuilder {
        this.world = world
        return this
    }


    private var type: CanType? = null

    private var description: String? = null

    private lateinit var data: MultiPolygon
    private val polygons = arrayListOf<Polygon>()

    private fun addPolygon(polygon: Polygon): CanBuilder {
        polygons.add(polygon)
        this.data = GeometryFactory().createMultiPolygon(polygons.toTypedArray())
        return this
    }

    private fun getPolygons() = polygons

    private var floor = 0.0

    private var ceil = 0.0

    private var settingDone = false

    private fun setDone(): CanBuilder {
        settingDone = true
        return this
    }

    fun isDone() = settingDone

    fun build(): Can =
        Can(name, UUID.randomUUID(), founder.uniqueId, world.uid, data.toGeoJSON(), floor, ceil, type, description)

    fun handleEvent(chatEvent: AsyncPlayerChatEvent) {
        chatEvent.isCancelled = true

        when (context) {
            /*
             輸入名字
             */
            Context.SETTING_NAME -> {
                name = chatEvent.message
                chatEvent.player.msg("What you input is: $name (Y/N)?")
                context = Context.CONFIRM_NAME
            }

            /*
             確認名字
             */
            Context.CONFIRM_NAME -> when (chatEvent.message.toUpperCase()) {
                "Y", "YES" -> {
                    chatEvent.player.msg("0>   NULL")
                    enumValues<CanType>().forEach { type ->
                        chatEvent.player.msg("${type.ordinal + 1}>   ${type.name}")
                    }
                    chatEvent.player.msg("OK, What Type of Zone would you like to set? \n Please Choose One Of The Below TYPEs, and input its number")
                    context = Context.SETTING_TYPE
                }

                "N", "NO" -> {
                    chatEvent.player.msg("OK, Do not make the mistake again :), reset the name")
                    context = Context.SETTING_NAME
                }

                else -> {
                    chatEvent.player.msg("wrong input, please input \'y\' or \'n\'")
                }

            }

            /*
             輸入類型
             */
            Context.SETTING_TYPE -> {
                val num = chatEvent.message.toIntOrNull()
                if (num != null && num > 0 && num <= enumValues<CanType>().size) {
                    type = enumValues<CanType>().associateBy { it.ordinal }[num - 1]
                    chatEvent.player.msg("What you input is: $type (Y/N)?")
                    context = Context.CONFIRM_TYPE
                } else {
                    chatEvent.player.msg("Invalid input!")
                }
            }

            /*
             確認類型
             */
            Context.CONFIRM_TYPE -> when (chatEvent.message.toUpperCase()) {
                "Y", "YES" -> {
                    chatEvent.player.msg("We will set some polygon next, Please Use 經量儀 click block in turns")
                    chatEvent.player.inventory.addItem(chatEvent.player.inventory.itemInMainHand)
                    chatEvent.player.inventory.setItemInMainHand(ItemStack(Material.GOLDEN_PICKAXE))
                    context = Context.SETTING_DATA
                }

                "N", "NO" -> {
                    chatEvent.player.msg("You can reset the type")
                    context = Context.SETTING_TYPE
                }

                else -> {
                    chatEvent.player.msg("wrong input, please input \'y\' or \'n\'")
                }
            }


            /**
             * 設置數據
             */
            Context.SETTING_DATA -> if (chatEvent.message.toUpperCase() == "STOP") {
                var warning = ""
                val cacheNum = getPolygonBuilder().number()
                if (cacheNum != 0) warning = "注意！還有 $cacheNum 個點尚未構建成新的多邊形，將被丟棄，"
                chatEvent.player.msg("$warning 確認已經完成嗎？ (Y/N)")
                context = Context.CONFIRM_DATA
            }


            /**
             * 確認數據
             */
            Context.CONFIRM_DATA -> when (chatEvent.message.toUpperCase()) {
                "Y", "YES" -> {
                    chatEvent.player.msg("現在請設定區域的高度, 用金鎬點擊天花板，或者輸入一個整數")
                    context = Context.SETTING_HEIGHT
                }

                "N", "NO" -> {
                    chatEvent.player.msg("Now, you can reset the points")
                    context = Context.SETTING_DATA
                }

                else -> {
                    chatEvent.player.msg("wrong input, please input \'y\' or \'n\'")
                }
            }

            /*
            設定區域下界
             */
            Context.SETTING_HEIGHT -> if (chatEvent.message.toIntOrNull() != null) {
                ceil = floor + chatEvent.message.toInt()
                chatEvent.player.msg("Zone is from $floor to $ceil (y/N)?")
                context = Context.CONFIRM_HEIGHT
            } else {
                chatEvent.player.msg("You must input a INTEGER!")
            }


            /*
             確認高度
             */
            Context.CONFIRM_HEIGHT -> when (chatEvent.message.toUpperCase()) {
                "Y", "YES" -> {
                    chatEvent.player.msg("現在你可以為剛剛新建的區域設定一些描述")
                    context = Context.SETTING_DESCRIPTION
                }

                "N", "NO" -> {
                    chatEvent.player.msg("你可以重新設置高度了")
                    context = Context.SETTING_HEIGHT
                }

                else -> {
                    chatEvent.player.msg("wrong input, please input \'y\' or \'n\'")
                }
            }

            /**
             * 設置備註
             */
            Context.SETTING_DESCRIPTION -> {
                description = chatEvent.message
                chatEvent.player.msg("What you input is: $description (y/N)?")
                context = Context.CONFIRM_DESCRIPTION
            }

            /**
             * 確認備註
             */
            Context.CONFIRM_DESCRIPTION -> when (chatEvent.message.toUpperCase()) {
                "Y", "YES" -> {
                    chatEvent.player.msg("全部設定完了, 現在輸入'/geo done'來結束這一切")
                    setDone()
                }

                "N", "NO" -> {
                    chatEvent.player.msg("現在你可以重新設定註釋/描述")
                    context = Context.SETTING_DESCRIPTION
                }

                else -> {
                    chatEvent.player.msg("wrong input, please input \'y\' or \'n\'")
                }
            }
        }
    }

    fun handleEvent(event: PlayerInteractEvent) {
        when (context) {

            Context.SETTING_DATA -> {
                if (event.hasBlock() && event.action == Action.LEFT_CLICK_BLOCK && event.material == Material.GOLDEN_PICKAXE) {
                    event.isCancelled = true
                    getPolygonBuilder().addLocation(event.clickedBlock!!.location)  //這裡只用到了二維座標
                    floor = event.clickedBlock!!.location.y  //設定地板
                    event.player.msg("你為第 ${getPolygons().size + 1} 個多邊形添加了第 ${getPolygonBuilder().number()} 個點")

                    if (getPolygonBuilder().isBuildable()) {
                        addPolygon(getPolygonBuilder().build())
                        setPolygonBuilder(PolygonBuilder())     //每新建一個多邊形就要換一個新的builder
                        event.player.msg("你已經添加了 ${getPolygons().size} 個多邊形")
                    }
                }
            }

            Context.SETTING_HEIGHT -> {
                if (event.hasBlock() && event.action == Action.LEFT_CLICK_BLOCK && event.material == Material.GOLDEN_PICKAXE) {
                    event.isCancelled = true
                    ceil = event.clickedBlock!!.location.y
                    event.player.msg("區域是從 $floor 到 $ceil (y/N)?")
                    context = Context.CONFIRM_HEIGHT
                }
            }

            else -> return
        }
    }
}