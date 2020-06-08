package com.filiplike.powermask

import com.google.android.gms.wearable.PutDataMapRequest
import java.time.LocalDateTime

//key to identify transferred object
private const val COUNT_KEY = "com.powermask.key.count"

class Report {
    private val index: Int = 0
    private var items:HashMap<String,LocalDateTime> = HashMap()
    //adds LocaleDateTime to prepare it for sending
    fun addItem(timestamp: LocalDateTime){
        items.put("$index", timestamp)
    }
    //clearing map
    fun clear(){
        items = hashMapOf()
    }
    //sending data to phone via DataLayer
    fun pushReport(){
        PutDataMapRequest.create("/counted").run {
            val array:Array<String?> = arrayOfNulls(size = items.size)
            items.values.forEach { s -> run { array[index] = s.toString() } }
            dataMap.putStringArray(COUNT_KEY,array)
        }
    }
}