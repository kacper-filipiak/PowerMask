package com.filiplike.powermask

import android.app.Activity
import android.app.Service
import android.content.Context
import android.content.Intent
import android.os.Bundle
import android.os.IBinder
import kotlinx.coroutines.launch
import kotlinx.coroutines.runBlocking

class Terminator : Activity(){

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        //TrackingService.stopService(this)
        finish()
    }


}