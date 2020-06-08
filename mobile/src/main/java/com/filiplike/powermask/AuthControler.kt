package com.filiplike.powermask
import  com.filiplike.powermask.CloudControler

class AuthControler(private val cloudControler: CloudControler) {

    var isLogged = false
    //signing up user amd adding username to cloudControler
    fun signUp(){
       cloudControler.user = "kisielWrosole"
        isLogged = true
    }
}