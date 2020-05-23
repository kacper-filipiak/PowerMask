package com.filiplike.powermask

import com.google.android.gms.wearable.DataClient
import com.google.android.gms.wearable.PutDataMapRequest
import java.sql.Timestamp

class Report(dataClient: DataClient) {
    private var items = HashMap<Timestamp,Int>()
    fun addItem(timestamp: Timestamp,count:Int){
        items.put(timestamp,count)
    }
    fun pushReport{
        val putDataRequest = PutDataMapRequest.create("/counted").run {
            dataMap.putInt
        }
    }
}