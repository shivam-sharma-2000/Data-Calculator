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
import android.widget.TextView
import android.widget.Toast
import androidx.appcompat.app.AppCompatActivity
import androidx.recyclerview.widget.RecyclerView
import com.example.datacalculator.R.layout.data_usage
import java.util.*

class MainActivity : AppCompatActivity(){

    private var dataHistoryList: MutableList<DataHistoryModel>? = null
    private var dataHistoryListAdapter : DataHistoryListAdapter? = null

    private val permission : Button by lazy {
        findViewById(R.id.get_permission)
    }
    private val dataCalculator : Button by lazy {
        findViewById(R.id.data_calculator)
    }
    private val startTimeTV : TextView by lazy {
        findViewById(R.id.start_time_tv)
    }
    private val endTimeTV : TextView by lazy {
        findViewById(R.id.end_time_tv)
    }
    private val startTime : TextView by lazy {
        findViewById(R.id.start_time)
    }
    private val endTime : TextView by lazy {
        findViewById(R.id.end_time)
    }
    private val dataHistoryRecyclerView : RecyclerView by lazy {
        findViewById(R.id.data_history_recycler_view)
    }

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

//        this.dataHistoryList = mutableListOf()
//        this.dataHistoryListAdapter = DataHistoryListAdapter(this, dataHistoryList!!)
//        dataHistoryRecyclerView.layoutManager = LinearLayoutManager(this)
//        dataHistoryRecyclerView.adapter = dataHistoryListAdapter
//        startTimeTV.setOnClickListener {
//            openDateTimePicker(myDateFormat, true)
//        }
//        endTimeTV.setOnClickListener {
//            openDateTimePicker(myDateFormat, false)
//        }

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
            Toast.makeText(this, "Mobile Data Usage " +
                    "\n$totalBytes Bytes, ${totalBytes/1024.00} KB, ${totalBytes/(1024.00*1024.00)} MB ${totalBytes/(1024.00*1024.00*1024)} GB", Toast.LENGTH_LONG).show()
//            updateUI(totalBytes)
//            "%.2f".format(data / 1024.00).toDouble(),
//            "%.2f".format(data/(1024.00*1024.00)).toDouble(),
//            "%.2f".format(data/(1024.00*1024.00*1024)).toDouble()
            totalBytes = TrafficStats.getTotalRxBytes() + TrafficStats.getTotalTxBytes()
            Log.i("Total Data Usage Bytes (Mobile + Wifi)", totalBytes.toString())
        })
//        permission.setOnClickListener {
//            startActivity(Intent(Settings.ACTION_USAGE_ACCESS_SETTINGS))
//        }
    }

//    private fun openDateTimePicker(myDateFormat: MyDateFormat, isStartTime: Boolean){
//        // Pick Time
//        timeFragment = TimePickerFragment(myDateFormat, isStartTime, ::timeIsSet)
//        timeFragment!!.show(supportFragmentManager, "Time Picker")
//        // Pick Date
//        dateFragment = DatePickerFragment(myDateFormat)
//        dateFragment!!.show(supportFragmentManager, "Date Picker")
//    }

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
            // endTimeInMillis = myDateFormat.getDateToMillis()
        }
    }

//    private fun updateUI(data: Long) {
//        // Update the UI here
//        println("Result: $data")
//
//        val dataHistoryModel = DataHistoryModel(
//            myDateFormat.getMillisToDate(Calendar.getInstance().timeInMillis, "dd-MM-yyyy"),
//            myDateFormat.getMillisToDate(startTimeInMillis),
//            myDateFormat.getMillisToDate(endTimeInMillis),
//            data.toDouble(),
//            "%.2f".format(data / 1024.00).toDouble(),
//            "%.2f".format(data/(1024.00*1024.00)).toDouble(),
//            "%.2f".format(data/(1024.00*1024.00*1024)).toDouble()
//        )
//
//        this.dataHistoryList?.add(dataHistoryModel)
//        this.dataHistoryListAdapter?.notifyDataSetChanged()
//    }

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
