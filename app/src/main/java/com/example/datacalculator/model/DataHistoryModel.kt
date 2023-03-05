package com.example.datacalculator.model

class DataHistoryModel {
    var id = 0
    var date: String? = null // date / month / year
    var time: String? = null // hh:mm:ss
    var from: String? = null // 00:00:00
    var to: String? = null
    var byte: Double = 0.0

    // TODO: Don't need KB MB and GB. Write logic to convert byte to KB/MB/GB based on the number.
    var kiloByte: Double = 0.0
    var megaByte: Double = 0.0
    var gigaByte: Double = 0.0

    constructor() {
        // Empty constructor
    }

    constructor(date: String, time: String, from: String, to: String, byte: Double) {
        this.date = date
        this.time = time
        this.from = from
        this.to = to
        this.byte = byte
    }

    fun getDate(): String? {
        return date
    }

    fun getTime(): String? {
        return time
    }

    fun getFrom(): String? {
        return from
    }

    fun getTo(): String? {
        return to
    }

    fun getBytes(): Double {
        return byte
    }

    fun setId(id: Int) {
        this.id = id
    }

    fun setDate(date: String) {
        this.date = date
    }

    fun setTime(time: String?) {
        this.time = time
    }

    fun setFrom(from: String) {
        this.from = from
    }

    fun setTo(to: String) {
        this.to = to
    }

    fun setBytes(byte: Double) {
        this.byte = byte
    }
}