package com.example.datacalculator
//
//import android.app.AppOpsManager
//import android.app.usage.NetworkStats
//import android.app.usage.NetworkStatsManager
//import android.content.Context
//import android.content.Intent
//import android.net.ConnectivityManager.TYPE_MOBILE
//import android.net.TrafficStats
//import android.os.Bundle
//import android.provider.Settings
//import android.util.Log
//import android.view.View
//import android.widget.Button
//import android.widget.EditText
//import android.widget.Toast
//import androidx.appcompat.app.AppCompatActivity
//import com.example.datacalculator.R.layout.data_usage
//import com.example.datacalculator.helpers.DataHistoryDbHelper
//import com.example.datacalculator.helpers.DateFormatHelper
//import com.example.datacalculator.model.DataHistoryModel
//import java.util.*
//
//class MainActivity : AppCompatActivity(){
//
//    private val etStartDate : EditText by lazy {
//        findViewById(R.id.et_start_date)
//    }
//    private val etStartTime : EditText by lazy {
//        findViewById(R.id.et_start_time)
//    }
//    private val etEndDate : EditText by lazy {
//        findViewById(R.id.et_end_date)
//    }
//    private val etEndTime : EditText by lazy {
//        findViewById(R.id.et_end_time)
//    }
//    private val btnDataUsage : Button by lazy {
//        findViewById(R.id.btn_data_usage)
//    }
//    private val btnShowDataUsage : Button by lazy {
//        findViewById(R.id.btn_show_data_usage)
//    }
//
//    private var dbHelper: DataHistoryDbHelper? = null
//    private var dateFragment : DatePickerFragment?= null
//    private var timeFragment : TimePickerFragment?= null
//    private val dateFormatHelper = DateFormatHelper()
//    private var startTimeInMillis: Long = 0
//    private var endTimeInMillis: Long = 0
//    private var startDateVal = ""
//    private var startTimeVal = ""
//    private var endDateVal = ""
//    private var endTimeVal = ""
//
//    override fun onCreate(savedInstanceState: Bundle?) {
//        super.onCreate(savedInstanceState)
//        setContentView(data_usage)
//
//        dbHelper = DataHistoryDbHelper(this)
//
//        etStartDate.setOnClickListener {
//            openDatePicker(dateFormatHelper, true)
//        }
//        etStartTime.setOnClickListener {
//            openTimePicker(dateFormatHelper, true)
//        }
//        etEndDate.setOnClickListener {
//            openDatePicker(dateFormatHelper, false)
//        }
//        etEndTime.setOnClickListener {
//            openTimePicker(dateFormatHelper, false)
//        }
//
//        btnShowDataUsage.setOnClickListener {
//            val intent = Intent(this, DataHistoryActivity::class.java)
//            startActivity(intent)
//        }
//
//        btnDataUsage.setOnClickListener(View.OnClickListener {
//
//            if (!checkPermissionGranted()) {
//                // Permission not granted, handle accordingly
//                Toast.makeText(this, "You have not provided the required permissions." +
//                        "\nPlease provide the permission and continue", Toast.LENGTH_LONG).show()
//                startActivity(Intent(Settings.ACTION_USAGE_ACCESS_SETTINGS))
//                return@OnClickListener
//            }
//
//            dateFormatHelper.setDate(startDateVal)
//            dateFormatHelper.setTime(startTimeVal)
//            startTimeInMillis = dateFormatHelper.getDateToMillis()
//
//            dateFormatHelper.setDate(endDateVal)
//            dateFormatHelper.setTime(endTimeVal)
//            endTimeInMillis = dateFormatHelper.getDateToMillis()
//
//            val networkStatsManager = this.getSystemService(Context.NETWORK_STATS_SERVICE) as NetworkStatsManager
//            val networkType = TYPE_MOBILE // network type (e.g. TYPE_MOBILE or TYPE_WIFI)
//            val networkStats = networkStatsManager.querySummary(networkType, null, startTimeInMillis, endTimeInMillis)
//            val bucket = NetworkStats.Bucket()
//            var totalBytes: Long = if (networkStats.getNextBucket(bucket)) bucket.rxBytes + bucket.txBytes else 0
//            while (networkStats.hasNextBucket())
//            {
//                if (networkStats.getNextBucket(bucket))
//                    totalBytes += bucket.rxBytes + bucket.txBytes
//            }
//            Log.i("Mobile Data Usage Bytes", totalBytes.toString())
//            // TODO: Make a dialog box instead of toast
//            Toast.makeText(this, "Mobile Data Usage " +
//                    "\n$totalBytes Bytes, ${totalBytes/1024.00} KB, ${totalBytes/(1024.00*1024.00)} MB ${totalBytes/(1024.00*1024.00*1024)} GB", Toast.LENGTH_LONG).show()
//            storeData(totalBytes)
//            totalBytes = TrafficStats.getTotalRxBytes() + TrafficStats.getTotalTxBytes()
//            Log.i("Total Data Usage Bytes (Mobile + Wifi)", totalBytes.toString())
//        })
//    }
//
//    private fun openDatePicker(dateFormatHelper: DateFormatHelper, isStartDate: Boolean){
//        dateFragment = DatePickerFragment(dateFormatHelper, isStartDate, ::dateIsSet)
//        dateFragment!!.show(supportFragmentManager, "Date Picker")
//    }
//
//    private fun dateIsSet(isStartDate: Boolean){
//        if(isStartDate) {
//            etStartDate.setText(dateFormatHelper.getDate())
//            startDateVal = dateFormatHelper.getDate()
//        }
//        else
//        {
//            etEndDate.setText(dateFormatHelper.getDate())
//            endDateVal = dateFormatHelper.getDate()
//        }
//    }
//
//    private fun openTimePicker(dateFormatHelper: DateFormatHelper, isStartTime: Boolean){
//        timeFragment = TimePickerFragment(dateFormatHelper, isStartTime, ::timeIsSet)
//        timeFragment!!.show(supportFragmentManager, "Time Picker")
//
//    }
//
//    private fun timeIsSet(isStartTime: Boolean){
//        if(isStartTime) {
//            etStartTime.setText(dateFormatHelper.getTime())
//            startTimeVal = dateFormatHelper.getTime()
//        }
//        else
//        {
//            etEndTime.setText(dateFormatHelper.getTime())
//            endTimeVal = dateFormatHelper.getTime()
//        }
//    }
//
//    private fun storeData(data: Long) {
//        // Store the Data in the Database
//
//        val dataHistoryModel = DataHistoryModel(
//            dateFormatHelper.getMillisToDate(Calendar.getInstance().timeInMillis, "dd-MM-yyyy"), // Date
//            dateFormatHelper.getMillisToDate(Calendar.getInstance().timeInMillis, "hh:mm"), // Time
//            dateFormatHelper.getMillisToDate(startTimeInMillis), // From
//            dateFormatHelper.getMillisToDate(endTimeInMillis), // To
//            data.toDouble(), // Bytes
//        )
//
//        // TODO: Add data history model object in the DB. and Get the list in the history screen and show.
//        dbHelper?.addUsageHistory(dataHistoryModel)
//    }
//
//    private fun checkPermissionGranted(): Boolean {
//
//        val appOpsManager = getSystemService(Context.APP_OPS_SERVICE) as AppOpsManager
//        val mode = appOpsManager.checkOpNoThrow(
//            AppOpsManager.OPSTR_GET_USAGE_STATS,
//            android.os.Process.myUid(),
//            packageName
//        )
//        return (mode == AppOpsManager.MODE_ALLOWED)
//    }
//}
import android.os.Bundle
import android.view.View
import android.widget.ExpandableListView
import androidx.appcompat.app.AppCompatActivity


class MainActivity : AppCompatActivity() {
    private val FLAG_HIDE_UNHIDE = 0
    private val totalAmount = ""
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.list_layout)
        val elvInvestments = findViewById<View>(R.id.elvInvestments) as ExpandableListView

        // Create the groups
        val lstGroups: MutableList<String> = ArrayList()
        lstGroups.add("Renda Variavél")
        lstGroups.add("Renda Fixa")
        lstGroups.add("Extrato")


        // Create items of each group
        val lstRendaVariavel: MutableList<Investment> = ArrayList()
        lstRendaVariavel.add(Investment("ITSA4", 100, 1000000.00))
        lstRendaVariavel.add(Investment("KLBN11", 500, 5673.33))
        lstRendaVariavel.add(Investment("PETR4", 1000, 10325.26))

        val lstRendaFixa: MutableList<Investment> = ArrayList()
        lstRendaFixa.add(Investment("CDB NBC", 0, 5000.00))
        lstRendaFixa.add(Investment("CDB Modal Pré-Fixado", 0, 2500.00))
        lstRendaFixa.add(Investment("LCA Indusval", 0, 4000.00))

        val lstExtrato: MutableList<Investment> = ArrayList()
        lstExtrato.add(Investment("Transferência AG000/CC0000-0", 0, 2500.00))

        // Create the relationship of groups and your items
        val lstItemsGroup = HashMap<String, List<Investment>>()
        lstItemsGroup[lstGroups[0]] = lstRendaVariavel
        lstItemsGroup[lstGroups[1]] = lstRendaFixa
        lstItemsGroup[lstGroups[2]] = lstExtrato

        // Create an adapter (BaseExpandableListAdapter) with the data above
        val listViewAdapter = ListViewAdapter(this, lstGroups, lstItemsGroup)
        // defines the ExpandableListView adapter
        elvInvestments.setAdapter(listViewAdapter)
    }
}