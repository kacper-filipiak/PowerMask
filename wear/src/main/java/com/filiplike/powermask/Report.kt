package com.filiplike.powermask

import android.R.attr.path
import android.R.id.message
import android.app.Activity
import android.content.ContentProvider
import android.content.ContentValues
import android.content.ContentValues.TAG
import android.content.Context
import android.os.Looper
import android.util.Log
import android.widget.Toast
import com.google.android.gms.tasks.Task
import com.google.android.gms.tasks.Tasks
import com.google.android.gms.wearable.*
import java.time.LocalDateTime
import java.util.concurrent.ExecutionException


//key to identify transferred object
private const val COUNT_KEY = "com.powermask.key.count"

class Report {
    private val index: Int = 0
    private var items:HashMap<String, LocalDateTime> = HashMap()
    private var sessionData = String()
    private var user = "kisielWrosole"
    lateinit var dataClient: DataClient
    fun user():String{
        return user
    }
    //adds LocaleDateTime to prepare it for sending
    fun addItem(timestamp: LocalDateTime){
        items.put("$index", timestamp)
        sessionData += "$timestamp|"
    }
    //clearing map
    fun clear(){
        sessionData = ""
        items = hashMapOf()
    }
    fun getData():String{
        return  sessionData.dropLast(1)
    }
    fun pushMessage(context: Context){
        CloudControler().pushList(sessionData.split('|'))
        val localNodeId = Wearable.getNodeClient(context).connectedNodes
        localNodeId.addOnCompleteListener { node ->
            Log.d(
                ContentValues.TAG,
                "Sending text was completed: $node"
            )
        }
        localNodeId.addOnSuccessListener{ nodeId ->
            nodeId.forEach {
                Wearable.getMessageClient(context)
                    .sendMessage(it.id, "/count", sessionData.toByteArray())
                    .addOnSuccessListener {
                        Log.d(
                            ContentValues.TAG,
                            "Sending text success: $it"
                        )
                    }
                Log.d(
                    ContentValues.TAG,
                    "Sending text: ${it.displayName}"
                )
            }
        }
    }
    //sending data to phone via DataLayer
    fun pushReport(context: Context){
        Log.d(
            TAG,
            "Sending text ${sessionData}"
        )
        //dataClient = Wearable.getDataClient(context)
        dataClient = Wearable.WearableOptions.Builder().setLooper(Looper.myLooper()).build().let { options ->
            Wearable.getDataClient(context)
        }
         val  putDataMapRequest = PutDataMapRequest.create("/count").run {
            dataMap.putStringArray(COUNT_KEY, arrayOf(sessionData))
             setUrgent()
             asPutDataRequest()
        }
        val putDataTask: Task<DataItem> = dataClient.putDataItem(putDataMapRequest)
        putDataTask.addOnSuccessListener { dataItem ->
            Log.d(
                TAG,
                "Sending text was successful: $dataItem"
            )
        }
        putDataTask.addOnFailureListener { dataItem ->
            Log.d(
                TAG,
                "Sending text was unsuccessful: $dataItem"
            )

        }


    }
    fun pushReportToNodes(context: Context){
        val nodeListTask: Task<List<Node>> = Wearable.getNodeClient(context).connectedNodes
        try {
            val nodes = Tasks.await(nodeListTask)
            nodes.forEach { node ->
                val sendMessageTask = Wearable.getMessageClient(context).sendMessage(node.id, "/powermask/count", sessionData.toByteArray())
                     try {

                         var result = Tasks.await(sendMessageTask)

//Handle the e      rors//

                     } catch (exception: ExecutionException) {

//TO DO//

                     } catch ( exception:InterruptedException) {

//TO DO//

                     }

            }

        }
        catch (exception: ExecutionException) {

//TO DO//

        } catch ( exception:InterruptedException) {

//TO DO//

        }

    }
}