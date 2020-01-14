package managers

import Zone
import com.mongodb.client.MongoCollection

object ZoneManager {

    val zoneSet = mutableSetOf<Zone>()

    lateinit var zoneCollection: MongoCollection<Zone>

    fun loadAllFrom(col: MongoCollection<Zone>){
        col.find().forEach { it.register() }
    }

    fun writeOne(): Nothing = TODO()

    fun register(zone: Zone) {
        //添加到區域集合中
        zoneSet.add(zone)

        if(zone.type != null){
            //權限映射中加上這個區域
            zone.type.properties.forEach {
                it.zones.putIfAbsent(zone, mutableSetOf())
            }
        }
    }


}