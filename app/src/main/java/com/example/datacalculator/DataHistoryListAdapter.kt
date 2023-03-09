package com.example.datacalculator

import android.content.Context
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.BaseExpandableListAdapter
import android.widget.TextView
import androidx.recyclerview.widget.RecyclerView
import com.example.datacalculator.model.DataHistoryModel


class DataHistoryListAdapter(private val context: Context,
                             private val groups: List<String>,
                             private val groupItems: Map<String, List<DataHistoryModel>>) :
    BaseExpandableListAdapter()  {

    class ViewHolder(itemView: View) : RecyclerView.ViewHolder(itemView) {
        val data: TextView = itemView.findViewById(R.id.data)
        val date: TextView = itemView.findViewById(R.id.date)
        val from: TextView = itemView.findViewById(R.id.text_view_duration_from)
        val to: TextView = itemView.findViewById(R.id.text_view_duration_to)
    }

    override fun getGroupCount(): Int {
        // returns groups count
        return groups.size
    }

    override fun getChildrenCount(groupPosition: Int): Int {
        // returns items count of a group
        return groupItems[getGroup(groupPosition)]!!.size
    }

    override fun getGroup(groupPosition: Int): Any {
        // returns a group
        return groups[groupPosition]
    }

    override fun getChild(groupPosition: Int, childPosition: Int): Any {
        // returns a group item
        return groupItems[getGroup(groupPosition)]!![childPosition]
    }

    override fun getGroupId(groupPosition: Int): Long {
        // return the group id
        return groupPosition.toLong()
    }

    override fun getChildId(groupPosition: Int, childPosition: Int): Long {
        // returns the item id of group
        return childPosition.toLong()
    }

    override fun hasStableIds(): Boolean {
        // returns if the ids are specific ( unique for each group or item)
        // or relatives
        return false
    }

    override fun getGroupView(
        groupPosition: Int,
        isExpanded: Boolean,
        convertView: View?,
        parent: ViewGroup?
    ): View {
        // create main items (groups)
        var newView = convertView
        if (newView == null) {
            newView = LayoutInflater.from(context).inflate(R.layout.data_history_group_layout, parent, false)
        }
        val tvGroup = newView?.findViewById<View>(R.id.tvGroup) as TextView
        tvGroup.text = getGroup(groupPosition) as String
        return newView
    }

    override fun getChildView(
        groupPosition: Int,
        childPosition: Int,
        isLastChild: Boolean,
        convertView: View?,
        parent: ViewGroup?
    ): View {
        // create the group items (items of groups)
        var newView = convertView
        if (newView == null) {
            newView = LayoutInflater.from(context).inflate(R.layout.data_history_item_layout, parent, false)
        }
        val history = getChild(groupPosition, childPosition) as DataHistoryModel

        val data: TextView = newView?.findViewById(R.id.data) as TextView
        val date: TextView = newView?.findViewById(R.id.date) as TextView
        val from: TextView = newView?.findViewById(R.id.text_view_duration_from) as TextView
        val to: TextView = newView?.findViewById(R.id.text_view_duration_to) as TextView

        date.text = history.getDate().toString()
        data.text = history.getDataUsage().toString()
        from.text = history.getFrom().toString()
        to.text = history.getTo().toString()

        return newView
    }

    override fun isChildSelectable(groupPosition: Int, childPosition: Int): Boolean {
        // returns if the group item (item of group) can be selected
        return true
    }
}

