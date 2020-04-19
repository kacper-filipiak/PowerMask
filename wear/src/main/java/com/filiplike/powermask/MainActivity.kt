package com.filiplike.powermask

import android.content.Context
import android.hardware.Sensor
import android.hardware.SensorEvent
import android.hardware.SensorManager
import android.media.MediaPlayer
import android.os.Bundle
import android.support.wearable.activity.WearableActivity
import kotlinx.android.synthetic.main.activity_main.*
import androidx.core.content.ContextCompat.getSystemService
import android.icu.lang.UCharacter.GraphemeClusterBreak.T
import android.hardware.SensorEventListener
import androidx.core.content.ContextCompat.getSystemService
import android.icu.lang.UCharacter.GraphemeClusterBreak.T





class MainActivity : WearableActivity() {

    private var initState = FloatArray(5)
    private var currentState = FloatArray(5)

    private var maskOn = false
    private var contact = false
    private var lockMedia = false

    private lateinit var sensorMenager : SensorManager
    private lateinit var rotationSensor: Sensor

    private lateinit var mediaPlayer: MediaPlayer

    private val mLightSensorListener = object : SensorEventListener {
        override fun onSensorChanged(event: SensorEvent) {
            textView2.text=event.timestamp.toString()
            if (maskOn) {
                currentState = event.values
                contactCheck()
            }
        }

        override fun onAccuracyChanged(sensor: Sensor, accuracy: Int) {
        }
    }
    private fun contactCheck(){
        textView2.text = currentState[0].toString()
        contact = true
        for (index in currentState.indices){
            if (currentState[index]<initState[index]-0.05&&currentState[index]>initState[index]+0.05){
                contact = false
            }
            textView3.text = lockMedia.toString()
        }
        if (contact && !lockMedia){
            textView4.text = currentState[0].toString()
            lockMedia = true
            mediaPlayer.start()
        }else if(!contact&&lockMedia){
            lockMedia = false
        }
    }

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_main)

        button1.setOnClickListener { maskWear() }

        this.sensorMenager = getSystemService(Context.SENSOR_SERVICE) as SensorManager
        rotationSensor = sensorMenager.getDefaultSensor(Sensor.TYPE_ROTATION_VECTOR)

        mediaPlayer = MediaPlayer.create(this, R.raw.sound)
        mediaPlayer.start()

        // Enables Always-on
        setAmbientEnabled()
    }
    private fun maskWear(){
        if (!maskOn){
            button1.text = "Mask on"
            mediaPlayer.start()
            maskOn = true
            initState = currentState
            textView4.text = initState[0].toString()
        }
        else{
            button1.text = "Mask of"
            maskOn = false
            contact = false
            lockMedia = false
        }
    }

    override fun onResume() {
        super.onResume()
        sensorMenager.registerListener(mLightSensorListener, rotationSensor, SensorManager.SENSOR_DELAY_NORMAL);

        }
    override fun onPause() {
        super.onPause()
        sensorMenager.unregisterListener(mLightSensorListener)
    }

}


