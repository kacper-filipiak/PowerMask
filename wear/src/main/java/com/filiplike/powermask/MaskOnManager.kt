package com.filiplike.powermask


import android.content.Context
import android.hardware.Sensor
import android.hardware.SensorEvent
import android.hardware.SensorEventListener
import android.hardware.SensorManager
import android.media.MediaPlayer
import android.os.VibrationEffect
import android.os.Vibrator
import android.widget.Toast
import androidx.core.content.ContextCompat.getSystemService
import java.time.LocalDateTime

class MaskOnManager {

    private  lateinit var contectDetector: ContactDetector
    private var currentState = FloatArray(5)

    var counter = 0
    private val report:Report = Report()

    private var maskOn = true
    private var lockMedia = false

    private var sensorMenager : SensorManager
    private var rotationSensor: Sensor

    private var mediaPlayer: MediaPlayer
    private var vibrator: Vibrator

    private var initEvent = true

    private lateinit var mContext: Context

    constructor(context: Context){

        mContext = context
        sensorMenager = context.getSystemService(Context.SENSOR_SERVICE) as SensorManager
        rotationSensor = sensorMenager.getDefaultSensor(Sensor.TYPE_ROTATION_VECTOR)

        vibrator = context.getSystemService(Context.VIBRATOR_SERVICE) as Vibrator
        mediaPlayer = MediaPlayer.create(context, R.raw.sound)

        contectDetector = ContactDetector(currentState)
        sensorMenager.registerListener(mLightSensorListener, rotationSensor, 1)
    }
    private val mLightSensorListener = object : SensorEventListener {
        override fun onSensorChanged(event: SensorEvent) {
            currentState = event.values
            if(initEvent){
                initEvent = false
                contectDetector = ContactDetector(currentState)
            }
            //if protection is activated
            if (maskOn) {
                //if occurred face touch
                if (contectDetector.isContact(currentState)){
                    makeBeep()
                }else{
                    //release media player
                    lockMedia = false
                }
            }
        }

        override fun onAccuracyChanged(sensor: Sensor, accuracy: Int) {
        }
    }

    private fun makeBeep() {
        //if media player unlocked
        if (!lockMedia) {
            //lock media
            lockMedia = true
            //Vibrate
            vibrator.vibrate(VibrationEffect.createOneShot(200, VibrationEffect.DEFAULT_AMPLITUDE))
            //Play sound
            mediaPlayer.start()
            //add to send list
            report.addItem(LocalDateTime.now())
            //updates counter
            counter++
        }
    }
    fun destroy(){
        Toast.makeText(mContext, "You touched your face "+(counter-1).toString()+" times.", Toast.LENGTH_SHORT).show()

        sensorMenager.unregisterListener(mLightSensorListener)

    }
}