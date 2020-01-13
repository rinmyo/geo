import com.mongodb.MongoException
import com.mongodb.client.MongoClients
import hazae41.minecraft.kutils.bukkit.*
import org.bukkit.entity.Player

class GeoMain : BukkitPlugin() {

    override fun onEnable() {

        /**
         * 嘗試連接MongoDB
         */
        try {
            database = MongoClients.create().getDatabase("test")
            info("連接MongoDB數據庫成功")

        } catch (e: MongoException) {
            warning("MongoDB連接失敗 \n$e")
        }

        /**
         * 註冊主指令
         */
        command("geo") { sender, args ->
            when {
                args.isEmpty() -> sender.msg("see usage for input /geo ?")

                else -> when (args[0]) {
                    //to list some geometry
                    "list" -> {
                        if (args.size < 2) sender.msg("see usage for input /geo ?")
                        else when (args[1]) {
                            "zone" -> ZoneManager.zoneSet.forEach { sender.msg(" ${it.name}  ${it.world}  ${it.type}  ${it.founder}") }
                        }
                    }

                    "new" -> {
                        when {
                            sender !is Player -> sender.msg("You must be a player")
                            args.size < 2 -> sender.msg("see usage for input /geo ?")
                            else -> when (args[1]) {
                                "zone" -> {
                                    sender.createZone(this@GeoMain)
                                }
                            }
                        }
                    }
                }
            }
        }

        ZoneManager.implementProperties(this)
    }

    override fun onDisable() {
        info("add...")
    }

    override fun onLoad() {
        info("add...")
    }
}
