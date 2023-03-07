package com.example.datacalculator

import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import com.example.datacalculator.helpers.DataHistoryDbHelper
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView

class DataHistoryActivity : AppCompatActivity() {
    private val dataHistoryRecyclerView : RecyclerView by lazy {
        findViewById(R.id.data_history_recycler_view)
    }

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_data_history)

        dataHistoryRecyclerView.layoutManager = LinearLayoutManager(this)
        dataHistoryRecyclerView.adapter = DataHistoryListAdapter(this, DataHistoryDbHelper(this).getDataHistoryList())
    }
}