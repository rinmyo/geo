import managers.ZoneManager.zoneCollection
import com.mongodb.MongoClientSettings
import com.mongodb.MongoException
import com.mongodb.client.MongoClients
import com.mongodb.client.MongoDatabase
import handlers.handleCommand
import hazae41.minecraft.kutils.bukkit.*
import org.bson.codecs.configuration.CodecRegistries.fromProviders
import org.bson.codecs.configuration.CodecRegistries.fromRegistries
import org.bson.codecs.pojo.PojoCodecProvider

class GeoMain : BukkitPlugin() {

    private lateinit var database: MongoDatabase

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
            info("&a連接MongoDB數據庫成功")

        } catch (e: MongoException) {
            warning("&cMongoDB連接失敗 \n$e")
        }

        handleCommand(this)

        registerListeners(this)
    }

    override fun onDisable() {
        info("add...")
    }

    override fun onLoad() {
        info("add...")
    }
}
