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

    override fun onMessageReceived(p0: MessageEvent?) {
        super.onMessageReceived(p0)
        if (Log.isLoggable(TAG, Log.DEBUG)) {
            Log.d(TAG, "onMessageReceived: $p0")
        }
    }
    override fun onDataChanged(dataEvents: DataEventBuffer) {
        if (Log.isLoggable(TAG, Log.DEBUG)) {
            Log.d(TAG, "onMyDataChanged: $dataEvents")
        }

    }
}