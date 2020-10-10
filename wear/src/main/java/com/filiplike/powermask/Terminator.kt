package com.filiplike.powermask

import android.content.Context
import android.content.Intent
import kotlinx.coroutines.launch
import kotlinx.coroutines.runBlocking

class Terminator
    (context: Context) {
    init {
        runBlocking {
            launch {
                val stopIntent = Intent(context, TrackingService::class.java)
                context.stopService(stopIntent)
            }
        }

    }
}