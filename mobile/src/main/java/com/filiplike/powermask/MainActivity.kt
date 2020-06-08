package com.filiplike.powermask

import android.content.ContentValues
import android.content.Context
import android.graphics.Color
import android.hardware.SensorEventListener
import android.os.Bundle
import android.view.View
import android.widget.ArrayAdapter
import androidx.appcompat.app.AppCompatActivity
import com.google.android.gms.wearable.*
import com.filiplike.powermask.CloudControler
import kotlinx.android.synthetic.main.activity_main.*
import kotlinx.coroutines.GlobalScope
import kotlinx.coroutines.Job
import kotlinx.coroutines.launch
import kotlinx.coroutines.runBlocking
import java.time.LocalDateTime
import androidx.core.content.ContextCompat.getSystemService
import android.icu.lang.UCharacter.GraphemeClusterBreak.T
import android.text.style.BackgroundColorSpan
import android.util.Log
import android.widget.Toast
import androidx.core.view.get
import java.time.Duration
import kotlin.math.ceil


private val COUNT_KEY = "com.powermask.key.count"

class MainActivity : AppCompatActivity(), DataClient.OnDataChangedListener {

    private lateinit var cloudControler:CloudControler
    private lateinit var dataClient: DataClient
    private lateinit var authControler: AuthControler
    private  lateinit var colector: Job
    private lateinit var timeList: MutableList<LocalDateTime?>

    override fun onCreate(savedInstanceState: Bundle?) {
       super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_main)
        //initializing controllers amd clients
        cloudControler = CloudControler(this)
        dataClient = Wearable.getDataClient(this)
        authControler = AuthControler(cloudControler)
        //adding listeners
        dataClient.addListener(this)
        button.setOnClickListener { loginButonClicked(this) }

    }
    private fun loginButonClicked(context: Context){
        if(!authControler.isLogged){
            //signing uo
            authControler.signUp()
            button.text = "Get your data"
        }
        //Updating list and record
        updateList(context)
        updateUserData()
    }
    //getting data from wearable via DataLayer
    override fun onDataChanged(dataEvents: DataEventBuffer) {
        dataEvents.forEach { event ->
            // DataItem changed
            if (event.type == DataEvent.TYPE_CHANGED) {
                event.dataItem.also { item ->
                    if (item.uri.path?.compareTo("/count") == 0) {
                        DataMapItem.fromDataItem(item).dataMap.apply {
                            updateData(getStringArray(COUNT_KEY))
                        }
                    }
                }
            } else if (event.type == DataEvent.TYPE_DELETED) {
                // DataItem deleted
            }
        }
    }
    //calling push to Firestore
    fun updateData(array: Array<String>){
        cloudControler.pushArray(array)
    }
    //Updating record
    fun updateUserData(){
        runBlocking { launch {
            //get record
            val record = cloudControler.pullUserData()
            colector.join()
            //assertion on nulls
            if(timeList.isNotEmpty()){

                //calculate current rime since last touch
                val currentRecord = Duration.between(timeList.first(),LocalDateTime.now())
                //updating UI
                textView2.text = "${currentRecord.toMinutes()} minutes"
                progressBar2.setProgress((currentRecord.toMinutes()/(record.toMinutes()+0.001)).toInt()*100,true)
                if (currentRecord> record){
                    //detecting new record
                    Toast.makeText(this@MainActivity, "You reached record, congratulations!!!", Toast.LENGTH_SHORT).show()
                    cloudControler.putRecord(currentRecord)
                }
            }
        }}

    }


    fun updateList(context:Context){
        //building list
        colector = runBlocking {  launch{
            val data = cloudControler.pullData()
            listview.adapter = ArrayAdapter(context, R.layout.list_item, data)
            timeList = data.toMutableList()
        }}
    }
    //generating test data
    //TODO: cut it out
    private fun makeData(){
        cloudControler.pushArray(arrayOf(LocalDateTime.now().toString()))
    }
}



