package dev.glycine.geo.managers

import dev.glycine.geo.Can
import com.mongodb.client.MongoCollection

object CanManager {

    val canSet = mutableSetOf<Can>()

    lateinit var canCollection: MongoCollection<Can>

    fun loadAllFrom(col: MongoCollection<Can>){
        col.find().forEach { it.register() }
    }

    fun writeOne(): Nothing = TODO()

    fun register(can: Can) {
        //添加到區域集合中
        canSet.add(can)

        if(can.type != null){
            //權限映射中加上這個區域
            can.type.properties.forEach {
                it.cans.add(can)
            }
        }
    }


}