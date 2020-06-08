package com.filiplike.powermask

class ContactDetector( stateArray:FloatArray) {

    //getting independent copy of init state
    private var initState = stateArray.clone()

    //detects if occurred contact
    fun isContact(currentState: FloatArray): Boolean {

        var contact = true
        for (index in currentState.indices) {
            //checking position within range
            if (currentState[index] <= initState[index] - 0.2f || currentState[index] >= initState[index] + 0.2f) {
                contact = false
            }
        }
        return contact
    }
}
