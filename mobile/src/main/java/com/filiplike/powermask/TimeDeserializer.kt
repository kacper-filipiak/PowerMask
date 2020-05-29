package com.filiplike.powermask

import java.time.LocalDateTime
import java.time.format.DateTimeFormatter

class TimeDeserializer() {
    private val formatter = DateTimeFormatter.ofPattern("yyyy-MM-dd HH:mm:ss")

    fun convertStringArray(list: MutableList<String>):Array<LocalDateTime?> {
        var result: Array<LocalDateTime?> = arrayOfNulls(list.size)
        list.forEachIndexed { index, s ->  result[index] = LocalDateTime.parse(s)}
        return timeBubbleSort(result)

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