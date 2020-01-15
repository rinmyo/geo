package builders

import Can
import enums.CanType
import enums.contexts.SettingZoneContext
import org.bukkit.Material
import org.bukkit.World
import org.bukkit.entity.Player
import org.bukkit.event.block.Action
import org.bukkit.event.player.AsyncPlayerChatEvent
import org.bukkit.event.player.PlayerInteractEvent
import org.locationtech.jts.geom.GeometryFactory
import org.locationtech.jts.geom.MultiPolygon
import org.locationtech.jts.geom.Polygon
import utils.msg
import utils.toGeoJSON
import java.util.*

class CanBuilder {

    private lateinit var context: SettingZoneContext

    fun setContext(context: SettingZoneContext): CanBuilder {
        this.context = context
        return this
    }

    private fun getContext() = context


    private lateinit var polygonBuilder: PolygonBuilder

    fun setPolygonBuilder(polygonBuilder: PolygonBuilder): CanBuilder {
        this.polygonBuilder = polygonBuilder
        return this
    }

    private fun getPolygonBuilder() = polygonBuilder


    private lateinit var name: String

    private fun setName(name: String): CanBuilder {
        this.name = name
        return this
    }

    private fun getName() = name


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

    private fun setType(type: CanType?): CanBuilder {
        this.type = type
        return this
    }


    private var note: String? = null

    private fun setNote(note: String): CanBuilder {
        this.note = note
        return this
    }

    private fun getNote() = note


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

    fun build(): Can = Can(name, UUID.randomUUID(), founder.uniqueId, world.uid, data.toGeoJSON(), floor, ceil, type, note)

    fun handleEvent(chatEvent: AsyncPlayerChatEvent): CanBuilder {
        chatEvent.isCancelled = true

        when (getContext()) {
            /**
             * 輸入名字
             */
            SettingZoneContext.SETTING_ZONE_NAME -> {
                setName(chatEvent.message)
                chatEvent.player.msg("What you input is: ${getName()} (Y/N)?")
                return setContext(getContext() + 1)
            }

            /**
             * 確認名字
             */
            SettingZoneContext.CONFIRM_ZONE_NAME -> return when (chatEvent.message.toUpperCase()) {
                "Y", "YES" -> {
                    chatEvent.player.msg("0>   NULL")
                    enumValues<CanType>().forEach { type ->
                        chatEvent.player.msg("${type.ordinal + 1}>   ${type.name}")
                    }
                    chatEvent.player.msg("OK, What Type of Zone would you like to set? \n Please Choose One Of The Below TYPEs, and input its number")
                    setContext(getContext() + 1)
                }

                "N", "NO" -> {
                    chatEvent.player.msg("OK, Do not make the mistake again :), reset the name")
                    setContext(getContext() - 1)
                }

                else -> {
                    chatEvent.player.msg("wrong input, please input \'y\' or \'n\'")
                    this
                }

            }

            /**
             * 輸入類型
             */
            SettingZoneContext.SETTING_ZONE_TYPE -> {
                val num = chatEvent.message.toIntOrNull()
                return if (num != null) {
                    if (num == 0) {
                        setType(null)
                    } else if (num > 0 && num < enumValues<CanType>().size) {
                        setType(enumValues<CanType>().associateBy { it.ordinal }[num])
                    }
                    chatEvent.player.msg("What you input is: $type (Y/N)?")
                    setContext(getContext() + 1)
                } else {
                    chatEvent.player.msg("You must input the number of the above list!")
                    this
                }
            }

            /**
             * 確認類型
             */
            SettingZoneContext.CONFIRM_ZONE_TYPE -> return when (chatEvent.message.toUpperCase()) {
                "Y", "YES" -> {
                    chatEvent.player.msg("We will set some polygon next, Please Use GOLDEN_PICKAX click block in turns")
                    setContext(getContext() + 1)
                }

                "N", "NO" -> {
                    chatEvent.player.msg("OK, Do not make the mistake again :), reset the name")
                    setContext(getContext() - 1)
                }

                else -> {
                    chatEvent.player.msg("wrong input, please input \'y\' or \'n\'")
                    this
                }
            }


            /**
             * 設置數據
             */
            SettingZoneContext.SETTING_ZONE_DATA -> return if (chatEvent.message.toUpperCase() == "STOP") {
                var warning = ""
                val cacheNum = getPolygonBuilder().number()
                if (cacheNum != 0) warning = "注意！還有 $cacheNum 個點尚未構建成新的多邊形，將被丟棄，"
                chatEvent.player.msg("$warning 確認已經完成嗎？ (Y/N)")
                setContext(getContext() + 1)
            } else this


            /**
             * 確認數據
             */
            SettingZoneContext.CONFIRM_ZONE_DATA -> return when (chatEvent.message.toUpperCase()) {
                "Y", "YES" -> {
                    chatEvent.player.msg("現在請設定區域的高度, 用金鎬點擊天花板，或者輸入一個整數")
                    setContext(getContext() + 1)
                }

                "N", "NO" -> {
                    chatEvent.player.msg("Now, you can reset the points")
                    setContext(getContext() - 1)
                }

                else -> {
                    chatEvent.player.msg("wrong input, please input \'y\' or \'n\'")
                    this
                }
            }

            /*
            設定區域下界
             */
            SettingZoneContext.SETTING_HEIGHT -> return if (chatEvent.message.toIntOrNull() != null) {
                ceil = floor + chatEvent.message.toInt()
                chatEvent.player.msg("Zone is from $floor to $ceil (y/N)?")
                setContext(getContext() + 1)
            } else {
                chatEvent.player.msg("You must input a INTEGER!")
                this
            }


            /*
             確認高度
             */

            SettingZoneContext.CONFIRM_HEIGHT -> return when (chatEvent.message.toUpperCase()) {
                "Y", "YES" -> {
                    chatEvent.player.msg("現在你可以為剛剛新建的區域設定一些描述")
                    setContext(getContext() + 1)
                }

                "N", "NO" -> {
                    chatEvent.player.msg("你可以重新設置高度了")
                    setContext(getContext() - 1)
                }

                else -> {
                    chatEvent.player.msg("wrong input, please input \'y\' or \'n\'")
                    this
                }
            }

            /**
             * 設置備註
             */
            SettingZoneContext.SETTING_ZONE_NOTE -> {
                setNote(chatEvent.message)
                chatEvent.player.msg("What you input is: ${getNote()} (y/N)?")
                return setContext(getContext() + 1)
            }

            /**
             * 確認備註
             */
            SettingZoneContext.CONFIRM_ZONE_NOTE -> return when (chatEvent.message.toUpperCase()) {
                "Y", "YES" -> {
                    chatEvent.player.msg("全部設定完了, 現在輸入'/geo done'來結束這一切")
                    setDone()
                }

                "N", "NO" -> {
                    chatEvent.player.msg("現在你可以重新設定註釋/描述")
                    setContext(getContext() - 1)
                }

                else -> {
                    chatEvent.player.msg("wrong input, please input \'y\' or \'n\'")
                    this
                }
            }
        }
    }

    fun handleEvent(event: PlayerInteractEvent): CanBuilder {
        when (getContext()) {

            SettingZoneContext.SETTING_ZONE_DATA -> {
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
                return this
            }

            SettingZoneContext.SETTING_HEIGHT -> {
                if (event.hasBlock() && event.action == Action.LEFT_CLICK_BLOCK && event.material == Material.GOLDEN_PICKAXE) {
                    event.isCancelled = true
                    ceil = event.clickedBlock!!.location.y
                    event.player.msg("區域是從 $floor 到 $ceil (y/N)?")
                    return setContext(getContext() + 1)
                }
                return this
            }

            else -> return this
        }
    }
}