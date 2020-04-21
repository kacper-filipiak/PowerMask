package com.filiplike.powermask

import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import com.google.android.gms.wearable.DataClient
import com.google.android.gms.wearable.Wearable

class MainActivity : AppCompatActivity() {

    val dataClient: DataClient = Wearable.getDataClient(this)


    override fun onCreate(savedInstanceState: Bundle?) {
       super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_main)
    }
}



