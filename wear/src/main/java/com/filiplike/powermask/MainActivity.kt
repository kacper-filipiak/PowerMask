package com.filiplike.powermask

import android.content.Context
import android.hardware.Sensor
import android.hardware.SensorEvent
import android.hardware.SensorEventListener
import android.hardware.SensorManager
import android.media.MediaPlayer
import android.os.Bundle
import android.os.VibrationEffect
import android.os.Vibrator
import android.support.wearable.activity.WearableActivity
import android.widget.Toast
import kotlinx.android.synthetic.main.activity_main.*


class MainActivity : WearableActivity() {

    private  lateinit var contectDetector: ContactDetector
    private var currentState = FloatArray(5)

    private var counter = 0

    private var maskOn = false
    private var lockMedia = false

    private lateinit var sensorMenager : SensorManager
    private lateinit var rotationSensor: Sensor

    private lateinit var mediaPlayer: MediaPlayer
    private lateinit var vibrator: Vibrator

    private val mLightSensorListener = object : SensorEventListener {
        override fun onSensorChanged(event: SensorEvent) {
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

        vibrator = getSystemService(Context.VIBRATOR_SERVICE) as Vibrator

        mediaPlayer = MediaPlayer.create(this, R.raw.sound)

        // Enables Always-on
        setAmbientEnabled()
    }
    private fun makeBeep(){
        if (!lockMedia){
            lockMedia = true
            vibrator.vibrate(VibrationEffect.createOneShot(200,VibrationEffect.DEFAULT_AMPLITUDE))
            mediaPlayer.start()
            counter++
        }
    }
    private fun maskWear(){
        if (!maskOn){
            contectDetector = ContactDetector(currentState)
            vibrator.vibrate(VibrationEffect.createOneShot(200,VibrationEffect.DEFAULT_AMPLITUDE))
            button1.setText(R.string.button_on_text)
            imageView2.setImageResource(R.drawable.mask_on)
            maskOn = true
        }
        else{
            button1.setText(R.string.button_off_text)
            imageView2.setImageResource(R.drawable.mask_off)
            maskOn = false
            lockMedia=false
            Toast.makeText(applicationContext, "You touched your face "+(counter-1).toString()+" times.", Toast.LENGTH_SHORT).show()
            counter = 0
        }
    }

    override fun onResume() {
        super.onResume()
        sensorMenager.registerListener(mLightSensorListener, rotationSensor, SensorManager.SENSOR_DELAY_NORMAL)

        }
    override fun onPause() {
        super.onPause()
        sensorMenager.unregisterListener(mLightSensorListener)
        mediaPlayer.release()
    }

}


