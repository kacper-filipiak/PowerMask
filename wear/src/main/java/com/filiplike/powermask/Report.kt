package com.filiplike.powermask

import com.google.android.gms.wearable.PutDataMapRequest

private const val COUNT_KEY = "com.example.key.count"

class Report {
    private val index: Int = 0
    private var items:HashMap<String,String> = HashMap()
    fun addItem(timestamp: String){
        items.put("$index", timestamp)
    }
    fun clear(){
        items = hashMapOf()
    }
    fun pushReport(){
        val putDataRequest = PutDataMapRequest.create("/counted").run {
            val array:Array<String?> = arrayOfNulls(size = items.size)
            items.values.forEach { s: String -> run { array[index] = s } }
            dataMap.putStringArray(COUNT_KEY,array)
        }
    }
}