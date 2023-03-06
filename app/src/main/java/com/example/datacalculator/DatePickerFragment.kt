package com.example.datacalculator

import android.app.DatePickerDialog
import android.app.Dialog
import android.os.Bundle
import android.widget.DatePicker
import androidx.fragment.app.DialogFragment
import com.example.datacalculator.helpers.DateFormatHelper
import java.util.*

class DatePickerFragment(private val dateFormatHelper: DateFormatHelper, private val isStartDate: Boolean, val callBack: (isStartDate: Boolean) -> Unit) : DialogFragment(), DatePickerDialog.OnDateSetListener {

    override fun onCreateDialog(savedInstanceState: Bundle?): Dialog {
        val calendar = Calendar.getInstance()
        val year = calendar[Calendar.YEAR]
        val month = calendar[Calendar.MONTH]
        val dayOfMonth = calendar[Calendar.DAY_OF_MONTH]
        return DatePickerDialog(requireContext(), this, year, month, dayOfMonth)
    }

    override fun onDateSet(p0: DatePicker?, year: Int, month: Int, dayOfMonth: Int) {
        dateFormatHelper.setYear(year)
        dateFormatHelper.setDay(dayOfMonth)
        dateFormatHelper.setMonth(month + 1)
        callBack(isStartDate)
    }
}