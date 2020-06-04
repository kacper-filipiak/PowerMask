package com.filiplike.powermask

import java.time.LocalDateTime
import java.time.format.DateTimeFormatter

class TimeDeserializer() {

    fun convertStringArray(list: MutableList<String>):Array<LocalDateTime?> {
        var result = mutableListOf<LocalDateTime>()
        list.forEach{ s ->
                val t = LocalDateTime.parse(s)
                result.add(t) }
        return timeBubbleSort(result.toTypedArray())

    }

    private fun timeBubbleSort(a: Array<LocalDateTime?>): Array<LocalDateTime?> {
        for (i in a.indices) {
            for (j in 0 until a.size - 1) {
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