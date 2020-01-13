import com.mongodb.MongoClientSettings
import com.mongodb.MongoException
import com.mongodb.client.MongoClients
import com.mongodb.client.MongoCollection
import com.mongodb.client.MongoDatabase
import hazae41.minecraft.kutils.bukkit.*
import org.bson.codecs.configuration.CodecRegistries.fromProviders
import org.bson.codecs.configuration.CodecRegistries.fromRegistries
import org.bson.codecs.pojo.PojoCodecProvider
import org.bukkit.entity.Player
import utils.createZone

class GeoMain : BukkitPlugin() {

    private lateinit var database: MongoDatabase

    private lateinit var zoneCollection: MongoCollection<Zone>


    override fun onEnable() {

        /**
         * 嘗試連接MongoDB
         */
        try {
            val codec = fromRegistries(
                    MongoClientSettings.getDefaultCodecRegistry(),
                    fromProviders(PojoCodecProvider.builder().automatic(true).build())
            )
            database = MongoClients.create(MongoClientSettings.builder().codecRegistry(codec).build()).getDatabase("test")
            zoneCollection = database.getCollection("zone", Zone::class.java)
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
                            "zone" -> ZoneManager.zoneSet.forEach { sender.msg(" ${it.name}  ${it.getWorld()?.name}  ${it.type}  ${it.getFounder().name}") }
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
