package com.filiplike.powermask

import android.os.Bundle
import androidx.appcompat.app.AppCompatActivity
import com.google.android.gms.wearable.*


private const val COUNT_KEY = "com.example.key.count"

class MainActivity : AppCompatActivity() {

    private val dataClient: DataClient = Wearable.getDataClient(this)

    override fun onCreate(savedInstanceState: Bundle?) {
       super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_main)
        //dataClient.addListener { dataEventBuffer ->  onDataChanged(dataEventBuffer) }
    }
    private fun onDataChanged(dataEvents: DataEventBuffer) {
        dataEvents.forEach { event ->
            // DataItem changed
            if (event.type == DataEvent.TYPE_CHANGED) {
                event.dataItem.also { item ->
                    if (item.uri.path?.compareTo("/count") == 0) {
                        DataMapItem.fromDataItem(item).dataMap.apply {
                            updateData(getStringArray(COUNT_KEY))
                        }
                    }
                }
            } else if (event.type == DataEvent.TYPE_DELETED) {
                // DataItem deleted
            }
        }
    }
    fun updateData(array: Array<String>){

    }
}



