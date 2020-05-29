package com.filiplike.powermask

import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.provider.Settings
import android.widget.ArrayAdapter
import kotlinx.android.synthetic.main.activity_scroll.*
import kotlinx.coroutines.awaitAll
import kotlinx.coroutines.delay
import kotlinx.coroutines.launch
import kotlin.coroutines.*

class ScrollActivity : AppCompatActivity() {


    private lateinit var cloudControler:CloudControler

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_scroll)

        cloudControler = CloudControler(this)
        cloudControler.user = "kisielWrosole"

        cloudControler.pushArray(arrayOf())
        launch {  updateListView()}
    }
    private suspend fun updateListView(){

        listview.adapter = ArrayAdapter(this, R.layout.list_item, cloudControler.pullData())
    }
}
