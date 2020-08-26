package com.filiplike.powermask

import java.time.LocalDateTime

class TimeDeserializer {

    //converts list of strings to array of LocaleDateTime
    fun convertStringArray(list: MutableList<String>):Array<LocalDateTime?> {
        var result = mutableListOf<LocalDateTime>()
        list.forEach{ s ->
            //parsing witch default formatter
                val t = LocalDateTime.parse(s)
                result.add(t) }
        return timeBubbleSort(result.toTypedArray())

    }

    //sorting array using bubble algorithm
    private fun timeBubbleSort(a: Array<LocalDateTime?>): Array<LocalDateTime?> {
        for (i in a.indices) {
            for (j in 0 until a.size - 1) {
                //comparing dates
                if (a[j]!!.isBefore( a[j + 1])) {
                    val temp = a[j + 1]
                    a[j + 1] = a[j]
                    a[j] = temp
                }
            }
        }
        return a
    }
} 