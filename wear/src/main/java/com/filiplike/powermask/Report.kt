package com.filiplike.powermask

import com.google.android.gms.wearable.DataMap
import com.google.android.gms.wearable.PutDataMapRequest
import java.time.LocalDateTime

private const val COUNT_KEY = "com.example.key.count"

class Report {
    private val index: Int = 0
    private var items:HashMap<String,LocalDateTime> = HashMap()
    fun addItem(timestamp: LocalDateTime){
        items.put("$index", timestamp)
    }
    fun clear(){
        items = hashMapOf()
    }
    fun pushReport(){
        val putDataRequest = PutDataMapRequest.create("/counted").run {
            val array:Array<String?> = arrayOfNulls(size = items.size)
            items.values.forEach { s -> run { array[index] = s.toString() } }
            dataMap.putStringArray(COUNT_KEY,array)
        }
    }
}