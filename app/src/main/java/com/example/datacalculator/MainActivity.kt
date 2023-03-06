package com.example.datacalculator

import android.app.AppOpsManager
import android.app.usage.NetworkStats
import android.app.usage.NetworkStatsManager
import android.content.Context
import android.content.Intent
import android.net.ConnectivityManager.TYPE_MOBILE
import android.net.TrafficStats
import android.os.Bundle
import android.provider.Settings
import android.util.Log
import android.view.View
import android.widget.Button
import android.widget.EditText
import android.widget.Toast
import androidx.appcompat.app.AppCompatActivity
import com.example.datacalculator.R.layout.data_usage
import com.example.datacalculator.helpers.DataHistoryDbHelper
import com.example.datacalculator.model.DataHistoryModel
import java.util.*

class MainActivity : AppCompatActivity(){

    private val etStartDate : EditText by lazy {
        findViewById(R.id.et_start_date)
    }
    private val etStartTime : EditText by lazy {
        findViewById(R.id.et_start_time)
    }
    private val etEndDate : EditText by lazy {
        findViewById(R.id.et_end_date)
    }
    private val etEndTime : EditText by lazy {
        findViewById(R.id.et_end_time)
    }
    private val btnDataUsage : Button by lazy {
        findViewById(R.id.btn_data_usage)
    }
    private val btnShowDataUsage : Button by lazy {
        findViewById(R.id.btn_show_data_usage)
    }

    private var dbHelper: DataHistoryDbHelper? = null
    private var dateFragment : DatePickerFragment?= null
    private var timeFragment : TimePickerFragment?= null
    private val myDateFormat = MyDateFormat()
    private var startTimeInMillis: Long = 0
    private var endTimeInMillis: Long = 0
    private var startDateVal = ""
    private var startTimeVal = ""
    private var endDateVal = ""
    private var endTimeVal = ""

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(data_usage)

        dbHelper = DataHistoryDbHelper(this)

        etStartDate.setOnClickListener {
            openDatePicker(myDateFormat, true)
        }
        etStartTime.setOnClickListener {
            openTimePicker(myDateFormat, true)
        }
        etEndDate.setOnClickListener {
            openDatePicker(myDateFormat, false)
        }
        etEndTime.setOnClickListener {
            openTimePicker(myDateFormat, false)
        }

        btnShowDataUsage.setOnClickListener {
            val a = dbHelper!!.getDataHistoryList()
            for (b in a) {
                Log.i(
                    "Data Usage",
                    "Date = ${b.getDate().toString()}Data Usage = ${b.getDataUsage().toString()}"
                )
            }
        }

        btnDataUsage.setOnClickListener(View.OnClickListener {

            if (!checkPermissionGranted()) {
                // Permission not granted, handle accordingly
                Toast.makeText(this, "You have not provided the required permissions." +
                        "\nPlease provide the permission and continue", Toast.LENGTH_LONG).show()
                startActivity(Intent(Settings.ACTION_USAGE_ACCESS_SETTINGS))
                return@OnClickListener
            }

            myDateFormat.setDate(startDateVal)
            myDateFormat.setTime(startTimeVal)
            startTimeInMillis = myDateFormat.getDateToMillis()

            myDateFormat.setDate(endDateVal)
            myDateFormat.setTime(endTimeVal)
            endTimeInMillis = myDateFormat.getDateToMillis()

            val networkStatsManager = this.getSystemService(Context.NETWORK_STATS_SERVICE) as NetworkStatsManager
            val networkType = TYPE_MOBILE // network type (e.g. TYPE_MOBILE or TYPE_WIFI)
            val networkStats = networkStatsManager.querySummary(networkType, null, startTimeInMillis, endTimeInMillis)
            val bucket = NetworkStats.Bucket()
            var totalBytes: Long = if (networkStats.getNextBucket(bucket)) bucket.rxBytes + bucket.txBytes else 0
            while (networkStats.hasNextBucket())
            {
                if (networkStats.getNextBucket(bucket))
                    totalBytes += bucket.rxBytes + bucket.txBytes
            }
            Log.i("Mobile Data Usage Bytes", totalBytes.toString())
            // TODO: Make a dialog box instead of toast
            Toast.makeText(this, "Mobile Data Usage " +
                    "\n$totalBytes Bytes, ${totalBytes/1024.00} KB, ${totalBytes/(1024.00*1024.00)} MB ${totalBytes/(1024.00*1024.00*1024)} GB", Toast.LENGTH_LONG).show()
            storeData(totalBytes)
            totalBytes = TrafficStats.getTotalRxBytes() + TrafficStats.getTotalTxBytes()
            Log.i("Total Data Usage Bytes (Mobile + Wifi)", totalBytes.toString())
        })
    }

    private fun openDatePicker(myDateFormat: MyDateFormat, isStartDate: Boolean){
        dateFragment = DatePickerFragment(myDateFormat, isStartDate, ::dateIsSet)
        dateFragment!!.show(supportFragmentManager, "Date Picker")
    }

    private fun dateIsSet(isStartDate: Boolean){
        if(isStartDate) {
            etStartDate.setText(myDateFormat.getDate())
            startDateVal = myDateFormat.getDate()
        }
        else
        {
            etEndDate.setText(myDateFormat.getDate())
            endDateVal = myDateFormat.getDate()
        }
    }

    private fun openTimePicker(myDateFormat: MyDateFormat, isStartTime: Boolean){
        timeFragment = TimePickerFragment(myDateFormat, isStartTime, ::timeIsSet)
        timeFragment!!.show(supportFragmentManager, "Time Picker")

    }

    private fun timeIsSet(isStartTime: Boolean){
        if(isStartTime) {
            etStartTime.setText(myDateFormat.getTime())
            startTimeVal = myDateFormat.getTime()
        }
        else
        {
            etEndTime.setText(myDateFormat.getTime())
            endTimeVal = myDateFormat.getTime()
        }
    }

    private fun storeData(data: Long) {
        // Store the Data in the Database

        val dataHistoryModel = DataHistoryModel(
            myDateFormat.getMillisToDate(Calendar.getInstance().timeInMillis, "dd-MM-yyyy"), // Date
            myDateFormat.getMillisToDate(Calendar.getInstance().timeInMillis, "hh:mm"), // Time
            myDateFormat.getMillisToDate(startTimeInMillis), // From
            myDateFormat.getMillisToDate(endTimeInMillis), // To
            data.toDouble(), // Bytes
        )

        // TODO: Add data history model object in the DB. and Get the list in the history screen and show.
        dbHelper?.addUsageHistory(dataHistoryModel)
    }

    private fun checkPermissionGranted(): Boolean {

        val appOpsManager = getSystemService(Context.APP_OPS_SERVICE) as AppOpsManager
        val mode = appOpsManager.checkOpNoThrow(
            AppOpsManager.OPSTR_GET_USAGE_STATS,
            android.os.Process.myUid(),
            packageName
        )
        return (mode == AppOpsManager.MODE_ALLOWED)
    }
}
