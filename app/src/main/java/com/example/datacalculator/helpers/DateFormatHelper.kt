package com.example.datacalculator.helpers

import java.text.SimpleDateFormat

class DateFormatHelper {

    private var year:Int?= 0
    private var month:Int?= 0
    private var day:Int?= 0
    private var hour:Int?= 0
    private var minute:Int?= 0

    fun setYear(i:Int){
        year = i
    }
    fun setMonth(i:Int){
        month = i
    }
    fun setDay(i:Int){
        day = i
    }
    fun setHour(i:Int){
        hour = i
    }
    fun setMinutes(i:Int){
        minute = i
    }
    fun getDate(): String{
        return "$day-$month-$year"
    }
    fun getTime(): String{
        return "$hour:$minute"
    }
    fun setDate(date: String) {
        day = date.split('-')[0].toInt()
        month = date.split('-')[1].toInt()
        year = date.split('-')[2].toInt()
    }
    fun setTime(time: String) {
        hour = time.split(':')[0].toInt()
        minute = time.split(':')[1].toInt()
    }

    fun getDateToMillis(): Long {
        val dateString = "$day-$month-$year $hour:$minute"
        val formatter = SimpleDateFormat("dd-MM-yyyy hh:mm")
        val newDate = formatter.parse(dateString)
        return newDate!!.time
    }
    fun getMillisToDate(millis: Long, format: String = "dd-MM-yyyy hh:mm"): String {
        val formatter = SimpleDateFormat(format)
        return formatter.format(millis)
    }

}