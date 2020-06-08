package com.filiplike.powermask

import android.content.ContentValues.TAG
import android.content.Context
import android.util.Log
import android.widget.Toast
import com.google.firebase.firestore.SetOptions
import com.google.firebase.firestore.ktx.firestore
import com.google.firebase.ktx.Firebase
import kotlinx.coroutines.delay
import kotlinx.coroutines.joinAll
import kotlinx.coroutines.launch
import kotlinx.coroutines.runBlocking
import java.time.Duration
import java.time.LocalDateTime

//control connections to Firestore
class CloudControler(context: Context) {
    //getting copy of context
    private val cont = context
    //getting reference to Firesrore
    private val firebase = Firebase.firestore
    //User login to Firebase data
    var user: String = R.string.userName.toString()
    private var timeList: MutableList<String> = mutableListOf()


    //setting default value
    private var record = Duration.ZERO

    //getting deserializer for LocaleTimeDate
    private val deserializer = TimeDeserializer()

    //uploading data from wearable
    fun pushArray(array: Array<String>) {
        val map = mutableMapOf<String, Any>()
        //sorting and reassigning value
        array.sortedArray().forEach { s -> map[s] = 0 }

        //upload map to Firestore
        firebase.collection("users").document(user).set(map, SetOptions.merge())
            .addOnSuccessListener { Toast.makeText(cont, "uploaded", Toast.LENGTH_SHORT).show() }
            .addOnFailureListener {
                //throwing exception
                Log.d(TAG,"Uploading array failed")
                Toast.makeText(cont, "upload failed", Toast.LENGTH_SHORT).show()
            }


    }

    suspend fun pullData(): Array<LocalDateTime?> {


        //getting data in another thread
        val corut = runBlocking {  launch {
            //getting data from Firestore
            firebase.collection("users").document(user).get()
                .addOnSuccessListener {
                    timeList = mutableListOf()
                    var i = 0
                    if (!it.data.isNullOrEmpty()) {
                        it.data?.forEach { timeList.add(i++, it.key) }
                        //orderedList = deserializer.convertStringArray(timeList).toMutableList()
                    } else {
                        Log.d(TAG,"No data has been pulled ${it}")
                        Toast.makeText(cont, "No data $it", Toast.LENGTH_LONG).show()

                    }
                }
                .addOnFailureListener {
                    Toast.makeText(cont, "$it", Toast.LENGTH_SHORT).show()
                    Log.d(TAG, "Error getting documents: ", it)

                }
        }}
        //waiting for thread to finish
        corut.join()
        //deserialization data and returning
        return deserializer.convertStringArray(timeList)

    }
    //pull user record
    suspend fun pullUserData():Duration{
        //pulling data async
        val corut = runBlocking {
            launch {
                firebase.collection("usersData").document(user).get()
                    .addOnSuccessListener {
                        //add record to 0
                        record = Duration.ofSeconds( it?.data?.values?.first().toString().toLong())

                    }.addOnFailureListener {
                        Log.d(TAG, "Get user record failure", it)
                    }
            }
        }
        corut.join()
        return record
        }
    //updating record to firestore
    suspend fun putRecord(duration: Duration){
        val corut = runBlocking {
            launch {
                firebase.collection("usersData").document(user).update("record",duration.seconds)
            }
        }
        corut.join()
    }
}



