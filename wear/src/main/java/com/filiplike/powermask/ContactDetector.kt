package com.filiplike.powermask

class ContactDetector( stateArray:FloatArray) {

    private var initState = stateArray.clone()

    fun isContact(currentState: FloatArray): Boolean {

        var contact = true
        for (index in currentState.indices) {
            if (currentState[index] <= initState[index] - 0.2f || currentState[index] >= initState[index] + 0.2f) {
                contact = false
            }
        }
        return contact
    }
}
