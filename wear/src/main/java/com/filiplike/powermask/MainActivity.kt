package com.filiplike.powermask


import android.content.Context
import android.media.MediaPlayer
import android.os.Bundle
import android.os.VibrationEffect
import android.os.Vibrator
import android.support.wearable.activity.WearableActivity
import android.widget.ImageView
import kotlinx.android.synthetic.main.activity_main.*


class MainActivity : WearableActivity() {

    private  lateinit var contectDetector: ContactDetector
    private var currentState = FloatArray(5)


    private var maskOn = false
    private var lockMedia = false

    lateinit var mediaPlayer: MediaPlayer
     lateinit var vibrator: Vibrator
    //lateinit var maskAnimation:AnimationDrawable



    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_main)

       val maskImage = findViewById<ImageView>(R.id.imageView2).apply {
       //    //setImageResource(R.drawable.animation_on_list)
           setImageResource(R.drawable.icon_lightning)
       //    maskAnimation = drawable as AnimationDrawable
       }
       ////TODO("Uncomment animation to start it")
       //maskAnimation.start()

        imageView2.setOnClickListener { maskWear() }

        vibrator = getSystemService(Context.VIBRATOR_SERVICE) as Vibrator
        mediaPlayer = MediaPlayer.create(this, R.raw.sound)

        // Enables Always-on
        setAmbientEnabled()
    }

    private fun maskWear(){
        if (!maskOn){
            //put on mask and recalibrating states
            contectDetector = ContactDetector(currentState)
            //enable notification via buzzing
            vibrator.vibrate(VibrationEffect.createOneShot(200,VibrationEffect.DEFAULT_AMPLITUDE))
            mediaPlayer.start()
            //update UI
            textView.setText(R.string.button_on_text)
            //findViewById<ImageView>(R.id.imageView2).apply {
            //    //setImageResource(R.drawable.animation_on_list)
            //    setImageResource(R.drawable.animation_on_list)
            //    maskAnimation = drawable as AnimationDrawable
//
            //}
            //maskAnimation.start()
            maskOn = true
            TrackingService.startService(this, "You're defended!")
        }
        else{
            //Update UI
            textView.setText(R.string.button_off_text)
           //findViewById<ImageView>(R.id.imageView2).apply {
           //    //setImageResource(R.drawable.animation_on_list)
           //    setImageResource(R.drawable.animation_off_list)
           //    //maskAnimation = drawable as AnimationDrawable
           //}
            //maskAnimation.start()
            maskOn = false
            lockMedia=false

            TrackingService.stopService(this)
        }
    }

    override fun onResume() {
        super.onResume()
        //sensorMenager.registerListener(mLightSensorListener, rotationSensor, SensorManager.SENSOR_DELAY_NORMAL)

        }
    override fun onPause() {
        super.onPause()
        mediaPlayer.release()
    }

}


