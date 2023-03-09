package com.example.datacalculator

import android.os.Bundle
import android.widget.ExpandableListView
import androidx.appcompat.app.AppCompatActivity
import com.example.datacalculator.helpers.DataHistoryDbHelper
import com.example.datacalculator.model.DataHistoryModel
import java.util.*
import java.util.stream.Collectors

class DataHistoryActivity : AppCompatActivity() {
    private val elvDataHistory : ExpandableListView by lazy {
        findViewById(R.id.elvDataHistory)
    }

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_data_history)

        val historyList = DataHistoryDbHelper(this).getDataHistoryList()

        val groupItems: MutableMap<String, List<DataHistoryModel>> = TreeMap(Comparator.reverseOrder())
        groupItems.putAll(historyList.stream().collect(Collectors.groupingBy { s -> s.getDate().toString() }))

        val groups: List<String> = groupItems.keys.toList()

        // Create an adapter (BaseExpandableListAdapter) with the data above
        val dataHistoryListAdapter = DataHistoryListAdapter(this, groups, groupItems)
        // defines the ExpandableListView adapter
        elvDataHistory.setAdapter(dataHistoryListAdapter)
        for (i in 0 until dataHistoryListAdapter.groupCount) elvDataHistory.expandGroup(i)
    }
}