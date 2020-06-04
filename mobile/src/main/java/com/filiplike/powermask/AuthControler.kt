package com.filiplike.powermask
import  com.filiplike.powermask.CloudControler

class AuthControler(val cloudControler: CloudControler) {

    fun signUp(){
       cloudControler.user = "kisielWrosole"
    }
}