package com.filiplike.powermask
import  com.filiplike.powermask.CloudControler

class AuthControler(private val cloudControler: CloudControler) {

    var isLogged = false
    fun signUp(){
       cloudControler.user = "kisielWrosole"
        isLogged = true
    }
}