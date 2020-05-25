package com.filiplike.powermask

import android.content.Context
import com.google.firebase.database.DataSnapshot
import com.google.firebase.database.DatabaseReference
import com.google.firebase.database.ValueEventListener
import com.google.firebase.database.ktx.database
import com.google.firebase.ktx.Firebase

class CloudControler(context: Context){

    var dataArray = ArrayList<String>()
    val firebase:DatabaseReference = Firebase.database.reference
    fun pushArray(array:Array<String>){
        array.forEach { elem-> firebase.child("clicks").setValue(elem)
        }
    }

   /* val databaseListiner:ValueEventListener = ( (){
         fun onDataChange(dataSnapshot: DataSnapshot) {

            dataSnapshot.children.forEach{
                dataArray.add(it.child("time").value.toString())
            }
        }
    })*/

}