package com.filiplike.powermask

import android.hardware.SensorEventListener
import android.os.Bundle
import androidx.appcompat.app.AppCompatActivity
import com.google.android.gms.wearable.*
import com.filiplike.powermask.CloudControler


private val COUNT_KEY = "com.example.key.count"

class MainActivity : AppCompatActivity() {

    private lateinit var cloudControler:CloudControler
    private lateinit var dataClient: DataClient


    override fun onCreate(savedInstanceState: Bundle?) {
       super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_main)
        cloudControler = CloudControler(this)
        dataClient = Wearable.getDataClient(this)
        val a = arrayOf("123","321","tak")
        cloudControler.pushArray(a)
       // cloudControler.firebase.addValueEventListener(cloudControler.databaseListiner)
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
        cloudControler.pushArray(array)
    }
}



