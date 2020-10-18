package com.filiplike.powermask

import android.content.ContentValues.TAG
import android.content.Context
import android.util.Log
import android.widget.Toast
import com.google.android.gms.tasks.Task
import com.google.android.gms.wearable.DataClient
import com.google.android.gms.wearable.DataItem
import com.google.android.gms.wearable.PutDataMapRequest
import com.google.android.gms.wearable.Wearable
import java.time.LocalDateTime


//key to identify transferred object
private const val COUNT_KEY = "com.powermask.key.count"

class Report {
    private val index: Int = 0
    private var items:HashMap<String, LocalDateTime> = HashMap()
    private var sessionData = String()
    lateinit var dataClient: DataClient
    //adds LocaleDateTime to prepare it for sending
    fun addItem(timestamp: LocalDateTime){
        items.put("$index", timestamp)
        sessionData += "$timestamp|"
    }
    //clearing map
    fun clear(){
        sessionData = ""
        items = hashMapOf()
    }
    //sending data to phone via DataLayer
    fun pushReport(context: Context){
        dataClient = Wearable.getDataClient(context)
         val  putDataMapRequest = PutDataMapRequest.create("/count").run {
            val array:Array<String?> = arrayOfNulls(size = items.size)
            items.values.forEach { s -> run { array[index] = s.toString() } }
            dataMap.putStringArray(COUNT_KEY, arrayOf(sessionData))
             setUrgent()
             asPutDataRequest()
        }
        val putDataTask: Task<DataItem> = dataClient.putDataItem(putDataMapRequest)
        putDataTask.addOnSuccessListener { dataItem ->
            Log.d(
                TAG,
                "Sending text was successful: $dataItem"
            )
        }
        putDataTask.addOnFailureListener { dataItem ->
            Log.d(
                TAG,
                "Sending text was unsuccessful: $dataItem"
            )
        }

    }
}