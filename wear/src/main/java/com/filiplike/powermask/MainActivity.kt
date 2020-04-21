package com.filiplike.powermask

import com.filiplike.powermask.ContactDetector
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
import com.google.android.gms.wearable.DataItem


class MainActivity : WearableActivity() {

    private  lateinit var contectDetector: ContactDetector
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
            textView3.text=currentState[0].toString()
            currentState = event.values
            if (maskOn) {
                if (contectDetector.isContact(currentState)){
                    makeBeep()
                }else{
                    lockMedia = false
                }
            }
        }

        override fun onAccuracyChanged(sensor: Sensor, accuracy: Int) {
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
    private fun makeBeep(){
        if (!lockMedia){
            lockMedia = true
            mediaPlayer.start()
        }
    }
    private fun maskWear(){
        if (!maskOn){
            contectDetector = ContactDetector(currentState)
            button1.text = "Mask on"
            maskOn = true
        }
        else{
            button1.text = "Mask of"
            maskOn = false
            lockMedia=false
        }
    }

    override fun onResume() {
        super.onResume()
        sensorMenager.registerListener(mLightSensorListener, rotationSensor, SensorManager.SENSOR_DELAY_NORMAL);

        }
    override fun onPause() {
        super.onPause()
        sensorMenager.unregisterListener(mLightSensorListener)
        mediaPlayer.release()
    }

}


