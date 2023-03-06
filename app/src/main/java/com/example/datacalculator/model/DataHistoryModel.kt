package com.example.datacalculator.model

import com.example.datacalculator.helpers.BinaryPrefixHelper

class DataHistoryModel {
    private var id = 0
    private var date: String? = null // date / month / year
    private var time: String? = null // hh:mm:ss
    private var from: String? = null // 00:00:00
    private var to: String? = null
    private var byte: Double = 0.0

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

    fun getDataUsage(): String? {
        return BinaryPrefixHelper().toBinaryPrefixes(byte)
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