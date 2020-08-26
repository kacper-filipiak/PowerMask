package com.filiplike.powermask

import android.hardware.Sensor
import android.hardware.SensorEvent
import android.hardware.SensorEventListener
import android.hardware.SensorManager
import android.media.MediaPlayer
import android.os.VibrationEffect
import android.os.Vibrator
import java.time.LocalDateTime

class MaskOnMenager {

    private  lateinit var contectDetector: ContactDetector
    private var currentState = FloatArray(5)

    private var counter = 0
    private val report:Report = Report()

    private var maskOn = false
    private var lockMedia = false

    private lateinit var sensorMenager : SensorManager
    private lateinit var rotationSensor: Sensor

    private lateinit var mediaPlayer: MediaPlayer
    private lateinit var vibrator: Vibrator


    private val mLightSensorListener = object : SensorEventListener {
        override fun onSensorChanged(event: SensorEvent) {
            currentState = event.values
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

    private fun makeBeep(){
        //if media player unlocked
        if (!lockMedia){
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
}