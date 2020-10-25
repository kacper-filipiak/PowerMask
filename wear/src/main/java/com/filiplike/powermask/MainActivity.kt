package com.filiplike.powermask


import android.content.ContentValues
import android.content.Context
import android.content.Intent
import android.media.MediaPlayer
import android.os.Bundle
import android.os.VibrationEffect
import android.os.Vibrator
import android.support.wearable.activity.WearableActivity
import android.util.Log
import android.widget.ImageView
import android.widget.Toast
import androidx.fragment.app.FragmentActivity
import com.google.android.gms.auth.api.Auth
import com.google.android.gms.auth.api.signin.GoogleSignIn
import com.google.android.gms.auth.api.signin.GoogleSignInAccount
import com.google.android.gms.auth.api.signin.GoogleSignInClient
import com.google.android.gms.auth.api.signin.GoogleSignInOptions
import com.google.android.gms.common.ConnectionResult
import com.google.android.gms.common.GoogleApiAvailabilityLight
import com.google.android.gms.common.api.GoogleApiClient
import com.google.android.gms.common.api.internal.OnConnectionFailedListener
import com.google.android.gms.tasks.Task
import kotlinx.android.synthetic.main.activity_main.*


class MainActivity : WearableActivity() {

    private var mId: String? = ""
    private var idToken: String? = null
    private var mEmail: String? = ""
    private var mFamilyName: String? = ""
    private var mGivenName: String? = ""
    private var fullName: String? = ""
    private  lateinit var contectDetector: ContactDetector
    private var currentState = FloatArray(5)



    private var maskOn = false
    private var lockMedia = false

    lateinit var mediaPlayer: MediaPlayer
     lateinit var vibrator: Vibrator
    //lateinit var maskAnimation:AnimationDrawable

    lateinit var googleSignInClient:GoogleSignInClient

    private var onConnectionFailedListener = GoogleApiClient.OnConnectionFailedListener { ConnectionResult ->
        Toast.makeText(this, "Connection failed, your data can be not synchronised", Toast.LENGTH_LONG)
    }

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

        login()


        imageView2.setOnClickListener { maskWear() }

        vibrator = getSystemService(Context.VIBRATOR_SERVICE) as Vibrator
        mediaPlayer = MediaPlayer.create(this, R.raw.sound)

        // Enables Always-on
        setAmbientEnabled()
    }
    private fun login(){
        val googleSignInOptions = GoogleSignInOptions.Builder(GoogleSignInOptions.DEFAULT_SIGN_IN)
            .requestEmail()
            .build()
        googleSignInClient = GoogleSignIn.getClient(this, googleSignInOptions)
        val account = GoogleSignIn.getLastSignedInAccount(this)
        if(account==null) {
            googleSignInClient.signInIntent.also { signInIntent ->
                startActivityForResult(signInIntent, 1)
            }
        }else{
            updateCredentials(account)
        }
    }
    fun updateCredentials(signInAccount: GoogleSignInAccount?){
        fullName = signInAccount?.displayName
        mGivenName = signInAccount?.givenName
        mFamilyName = signInAccount?.familyName
        mEmail = signInAccount?.email
        idToken = signInAccount?.idToken
        mId = signInAccount?.id

    }
    override fun onActivityResult(requestCode: Int, resultCode: Int, data: Intent) {
        super.onActivityResult(requestCode, resultCode, data)

        // Result returned from launching the Intent from GoogleSignInApi.getSignInIntent(...)
        if (requestCode == 1) {
            val task :Task<GoogleSignInAccount> = GoogleSignIn.getSignedInAccountFromIntent(data)
             task.addOnSuccessListener {
                 updateCredentials(it)
                 Log.d(ContentValues.TAG, "Auth succeeded, data: $it")
             }
            task.addOnFailureListener{

                Log.d(ContentValues.TAG, "Auth failed, no data: $it")
                Toast.makeText(this@MainActivity, "Auth failed, no data",Toast.LENGTH_LONG)

            }
        }
    }
    private fun maskWear(){
        if (!maskOn){
            //put on mask and recalibrating states
            contectDetector = ContactDetector(currentState)
            //enable notification via buzzing
            vibrator.vibrate(VibrationEffect.createOneShot(200,VibrationEffect.DEFAULT_AMPLITUDE))
            //mediaPlayer.start()
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
            TrackingService.startService(this, "$mGivenName|$mId|$idToken")
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
        //mediaPlayer.release()
    }

}


