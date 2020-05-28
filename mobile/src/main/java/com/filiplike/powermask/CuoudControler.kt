package com.filiplike.powermask

import android.content.Context
import android.widget.Toast
import com.google.firebase.firestore.SetOptions
import com.google.firebase.firestore.ktx.firestore
import com.google.firebase.ktx.Firebase

class CloudControler(context: Context){
    private  val cont = context
    var timeArray:Array<String> = arrayOf()
    private val firebase = Firebase.firestore
    var user:String = R.string.userName.toString()



    fun pushArray(array:Array<String>){
        val map = mutableMapOf<String, Any>()
        array.sortedArray().forEach {  s -> map[s] = 0}

         firebase.collection("users").document(user).set(map, SetOptions.merge())
           .addOnSuccessListener { Toast.makeText(cont,"uploaded", Toast.LENGTH_SHORT).show() }
            .addOnFailureListener { Toast.makeText(cont, "upload failed",Toast.LENGTH_SHORT).show() }


    }
    fun pullData(){
        firebase.collection("clicks").document(user).get()
            .addOnSuccessListener {
                var i = 0
                it.data!!.forEach { timeArray[i++] = it.key }
                }
            }



    }

