package com.filiplike.powermask

import android.app.Service
import android.content.Intent
import android.os.IBinder
import android.util.Log
import com.google.android.gms.wearable.*

private const val TAG = "DataLayerService"
private const val START_ACTIVITY_PATH = "/count"
private const val DATA_ITEM_RECEIVED_PATH = "/data-item-received"
class DataLayerService : WearableListenerService() {

    override fun onCreate() {
        super.onCreate()
    }
    override fun onCapabilityChanged(p0: CapabilityInfo?) {
        super.onCapabilityChanged(p0)
        Log.d(TAG, "onMyDataChanged: $p0")

    }
    override fun onDataChanged(dataEvents: DataEventBuffer) {
        if (Log.isLoggable(TAG, Log.DEBUG)) {
            Log.d(TAG, "onMyDataChanged: $dataEvents")
        }
        val cloudControler = CloudControler()
        cloudControler.pushList(listOf("adadadadada"))
        // Loop through the events and send a message
        // to the node that created the data item.
        dataEvents.map { it.dataItem.uri }
            .forEach { uri ->
                // Get the node id from the host value of the URI
                val nodeId: String = uri.host.toString()
                // Set the data of the message to be the bytes of the URI
                val payload = uri.toString()



                cloudControler.pushList(payload.split('|'))

                // Send the RPC
                Wearable.getMessageClient(this)
                    .sendMessage(nodeId, DATA_ITEM_RECEIVED_PATH, payload.toByteArray())
            }
    }

    override fun onMessageReceived(p0: MessageEvent?) {
        super.onMessageReceived(p0)
        if (Log.isLoggable(TAG, Log.DEBUG)) {
            Log.d(TAG, "onMessageReceived: $p0")
        }
    }
}