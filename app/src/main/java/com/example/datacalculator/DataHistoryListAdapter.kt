package com.example.datacalculator

import android.content.Context
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.TextView
import androidx.recyclerview.widget.RecyclerView
import com.example.datacalculator.model.DataHistoryModel


class DataHistoryListAdapter(private val context: Context, private val items: List<DataHistoryModel>) :
    RecyclerView.Adapter<DataHistoryListAdapter.ViewHolder>() {

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): ViewHolder {
        val view = LayoutInflater.from(context).inflate(R.layout.data_history_layout, parent, false)
        return ViewHolder(view)
    }

    override fun onBindViewHolder(holder: ViewHolder, position: Int) {
        val item = items[position]
        holder.date.text = item.getDate().toString()
        holder.data.text = item.getDataUsage().toString()
        holder.from.text = item.getFrom().toString()
        holder.to.text = item.getTo().toString()
    }

    override fun getItemCount(): Int {
        return items.size
    }

    class ViewHolder(itemView: View) : RecyclerView.ViewHolder(itemView) {
        val data: TextView = itemView.findViewById(R.id.data)
        val date: TextView = itemView.findViewById(R.id.date)
        val from: TextView = itemView.findViewById(R.id.text_view_duration_from)
        val to: TextView = itemView.findViewById(R.id.text_view_duration_to)
    }
}

