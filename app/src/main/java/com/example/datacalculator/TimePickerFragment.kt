package com.example.datacalculator

import android.app.Dialog
import android.app.TimePickerDialog
import android.os.Bundle
import android.text.format.DateFormat
import android.widget.TimePicker
import androidx.fragment.app.DialogFragment
import com.example.datacalculator.helpers.DateFormatHelper
import java.util.*

class TimePickerFragment(private val dateFormatHelper: DateFormatHelper, private val isStartTime: Boolean, val callBack: (isStartTime: Boolean) -> Unit) : DialogFragment(), TimePickerDialog.OnTimeSetListener {

    override fun onCreateDialog(savedInstanceState: Bundle?): Dialog {
        val c = Calendar.getInstance()
        val h = c.get(Calendar.HOUR)
        val m = c.get(Calendar.MINUTE)
        return TimePickerDialog(context,this, h,m, DateFormat.is24HourFormat(activity))
    }

    override fun onTimeSet(p0: TimePicker?, hourOfDay: Int, minute: Int) {
        dateFormatHelper.setHour(hourOfDay)
        dateFormatHelper.setMinutes(minute)
        callBack(isStartTime)
    }
}