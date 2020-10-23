package com.filiplike.powermask

import java.time.LocalDateTime


class Report {
    private val index: Int = 0
    private var items: HashMap<String, LocalDateTime> = HashMap()
    private var sessionData = String()
    private var user = "kisielWrosole"
    fun user(): String {
        return user
    }

    //adds LocaleDateTime to prepare it for sending
    fun addItem(timestamp: LocalDateTime) {
        items.put("$index", timestamp)
        sessionData += "$timestamp|"
    }

    //clearing map
    fun clear() {
        sessionData = ""
    }

    fun getData(): String {
        return sessionData.dropLast(1)
    }
}
