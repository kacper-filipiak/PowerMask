package com.filiplike.powermask

import android.app.NotificationChannel
import android.app.NotificationManager
import android.app.PendingIntent
import android.app.Service
import android.content.Context
import android.content.Intent
import android.os.Build
import android.os.IBinder
import android.widget.Toast
import androidx.core.app.NotificationCompat
import androidx.core.content.ContextCompat
import kotlinx.coroutines.*

class TrackingService : Service() {

    private val CHANNEL_ID = "PowerMaskService"
    private lateinit var corut: Job
    private lateinit var lMaskOnManager : MaskOnManager
    companion object {
        fun startService(context: Context, message: String) {
            val startIntent = Intent(context, TrackingService::class.java)
            startIntent.putExtra("inputExtra", message)
            ContextCompat.startForegroundService(context, startIntent)
        }

        fun stopService(context: Context) {
            val stopIntent = Intent(context, TrackingService::class.java)
            context.stopService(stopIntent)
        }
    }

    override fun onStartCommand(intent: Intent?, flags: Int, startId: Int): Int {
        //do heavy work on a background thread
        val context = this
        corut = runBlocking { launch {
            val maskOnManager : MaskOnManager = MaskOnManager(context)
            lMaskOnManager = maskOnManager
        } }

        val input = intent?.getStringExtra ("inputExtra")
        createNotificationChannel()
        val notificationIntent = Intent(this, Terminator::class.java)
        val pendingIntent = PendingIntent.getActivity(
            this,
            0, notificationIntent, 0
        )

        val notification = NotificationCompat.Builder(this, CHANNEL_ID)
            .setContentTitle("PowerMask service")
            .setContentText(input)
            .setSmallIcon(R.drawable.mask_on)
            .setContentIntent(pendingIntent)
            .build()
        startForeground(1, notification)
        return START_NOT_STICKY
    }
    override fun onBind(intent: Intent): IBinder? {
        return null
    }

    private fun createNotificationChannel() {
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.O) {
            val serviceChannel = NotificationChannel(
                CHANNEL_ID, "PowerMaskService",
                NotificationManager.IMPORTANCE_DEFAULT
            )
            val manager = getSystemService(NotificationManager::class.java)
            manager!!.createNotificationChannel(serviceChannel)
        }
    }

    override fun onDestroy() {

        lMaskOnManager.destroy()
        Toast.makeText(this, "Service Destroyed", Toast.LENGTH_SHORT).show();
        super.onDestroy()
    }
}
