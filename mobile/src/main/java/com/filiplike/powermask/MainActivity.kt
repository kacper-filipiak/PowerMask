package com.filiplike.powermask

import android.content.Context
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


private val COUNT_KEY = "com.powermask.key.count"

class MainActivity : AppCompatActivity() {

    private lateinit var cloudControler:CloudControler
    private lateinit var dataClient: DataClient
    private lateinit var authControler: AuthControler
    private  lateinit var colector: Job
    private lateinit var timeList: MutableList<LocalDateTime?>

    override fun onCreate(savedInstanceState: Bundle?) {
       super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_main)
        cloudControler = CloudControler(this)
        dataClient = Wearable.getDataClient(this)
        authControler = AuthControler(cloudControler)
        button.setOnClickListener { loginButonClicked(this) }
    }
    private fun loginButonClicked(context: Context){
        if(!authControler.isLogged){
            authControler.signUp()
            button.text = "Get your data"
        }
        updateList(context)
        updateUserData()
    }
    private fun onDataChanged(dataEvents: DataEventBuffer) {
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
    fun updateData(array: Array<String>){
        cloudControler.pushArray(array)
    }
    fun updateUserData(){
        runBlocking { launch {
            val record = cloudControler.pullUserData()
            colector.join()
            if(timeList.isNotEmpty()){
                var currentRecord = LocalDateTime.now().minute - timeList.first()!!.minute
                textView2.setText("${currentRecord}")
            }
        }}

    }
    fun updateList(context:Context){
        colector = runBlocking {  launch{
            val data = cloudControler.pullData()
            listview.adapter = ArrayAdapter(context, R.layout.list_item, data)
            timeList = data.toMutableList()
        }}
    }
    private fun makeData(){
        cloudControler.pushArray(arrayOf(LocalDateTime.now().toString()))
    }
}



