package com.filiplike.powermask

import android.content.Context

class Terminator {
    constructor(context: Context){
        TrackingService.stopService(context)
    }
}