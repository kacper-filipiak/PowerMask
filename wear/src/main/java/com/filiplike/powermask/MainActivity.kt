package com.filiplike.powermask


import android.content.Context
import android.content.Intent
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
import java.time.LocalDateTime


class MainActivity : WearableActivity() {

    private  lateinit var contectDetector: ContactDetector
    private var currentState = FloatArray(5)

    private var counter = 0
    private val report:Report = Report()

    private var maskOn = false
    private var lockMedia = false

//    lateinit var sensorMenager : SensorManager
//    lateinit var rotationSensor: Sensor

    lateinit var mediaPlayer: MediaPlayer
     lateinit var vibrator: Vibrator

    //adding listener for sensor state change


    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_main)

        button1.setOnClickListener { maskWear() }
//        this.sensorMenager = getSystemService(Context.SENSOR_SERVICE) as SensorManager
//        rotationSensor = sensorMenager.getDefaultSensor(Sensor.TYPE_ROTATION_VECTOR)
//
        vibrator = getSystemService(Context.VIBRATOR_SERVICE) as Vibrator
        mediaPlayer = MediaPlayer.create(this, R.raw.sound)

        // Enables Always-on
        setAmbientEnabled()
    }
    //handles beeping and buzzing
//    private fun makeBeep(){
//        //if media player unlocked
//        if (!lockMedia){
//            //lock media
//            lockMedia = true
//            //Vibrate
//            vibrator.vibrate(VibrationEffect.createOneShot(200,VibrationEffect.DEFAULT_AMPLITUDE))
//            //Play sound
//            mediaPlayer.start()
//            //add to send list
//            report.addItem(LocalDateTime.now())
//            //updates counter
//            counter++
//        }
//    }

    private fun maskWear(){
        if (!maskOn){
            //put on mask and recalibrating states
            contectDetector = ContactDetector(currentState)
            //enable notification via buzzing
            vibrator.vibrate(VibrationEffect.createOneShot(200,VibrationEffect.DEFAULT_AMPLITUDE))
            mediaPlayer.start()
            //update UI
            button1.setText(R.string.button_on_text)
            imageView2.setImageResource(R.drawable.mask_on)
            maskOn = true
            TrackingService.startService(this, "You're defended!")
        }
        else{
            //Update UI
            button1.setText(R.string.button_off_text)
            imageView2.setImageResource(R.drawable.mask_off)
            Toast.makeText(applicationContext, "You touched your face "+(counter-1).toString()+" times.", Toast.LENGTH_SHORT).show()
            //resetting values and pushing data
            maskOn = false
            lockMedia=false
            report.pushReport()
            report.clear()
            counter = 0
            Terminator(this)
            //val stopIntent = Intent(this, TrackingService::class.java)
            //stopService(stopIntent)
            //TrackingService.stopService(this)
        }
    }

    override fun onResume() {
        super.onResume()
        //sensorMenager.registerListener(mLightSensorListener, rotationSensor, SensorManager.SENSOR_DELAY_NORMAL)

        }
    override fun onPause() {
        super.onPause()
        //mediaPlayer.release()
    }

}


