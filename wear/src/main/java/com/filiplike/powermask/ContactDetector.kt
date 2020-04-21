package com.filiplike.powermask

import android.media.MediaPlayer
import kotlinx.android.synthetic.main.activity_main.*

class ContactDetector(var initState:FloatArray) {


    fun isContact(currentState:FloatArray):Boolean{

        var contact = true
        for (index in currentState.indices){
            if (currentState[index] <= initState[index]-0.2 && currentState[index] >= initState[index]+0.2){
                contact = false
            }
        }
        return contact
    }
}