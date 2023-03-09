package com.example.datacalculator

import android.content.Context
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.BaseExpandableListAdapter
import android.widget.TextView
import kotlin.collections.HashMap

class ListViewAdapter     // initialize class variables
    (
    private val context: Context,
    private val lstGroups: MutableList<String>,
    private val lstItemsGroups: HashMap<String, kotlin.collections.List<Investment>>
) : BaseExpandableListAdapter() {
    override fun getGroupCount(): Int {
        // returns groups count
        return lstGroups.size
    }

    override fun getChildrenCount(groupPosition: Int): Int {
        // returns items count of a group
        return lstItemsGroups[getGroup(groupPosition)]!!.size
    }

    override fun getGroup(groupPosition: Int): Any {
        // returns a group
        return lstGroups[groupPosition]
    }

    override fun getChild(groupPosition: Int, childPosition: Int): Any {
        // returns a group item
        return lstItemsGroups[getGroup(groupPosition)]!![childPosition]
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
        parent: ViewGroup
    ): View {
        // create main items (groups)
        var convertView = convertView
        if (convertView == null) {
            val layoutInflater =
                context.getSystemService(Context.LAYOUT_INFLATER_SERVICE) as LayoutInflater
            convertView = layoutInflater.inflate(R.layout.list_group, null)
        }
        val tvGroup = convertView?.findViewById<View>(R.id.tvGroup) as TextView
        val tvAmount = convertView?.findViewById<View>(R.id.tvAmount) as TextView
        tvGroup.text = getGroup(groupPosition) as String
        tvAmount.text = getChildrenCount(groupPosition).toString()
        return convertView
    }

    override fun getChildView(
        groupPosition: Int,
        childPosition: Int,
        isLastChild: Boolean,
        convertView: View?,
        parent: ViewGroup
    ): View {
        // create the subitems (items of groups)
        var convertView = convertView
        if (convertView == null) {
            val layoutInflater =
                context.getSystemService(Context.LAYOUT_INFLATER_SERVICE) as LayoutInflater
            convertView = layoutInflater.inflate(R.layout.list_item_group, null)
        }
        val tvItem = convertView?.findViewById<View>(R.id.tvItem) as TextView
        val tvRealAmount = convertView?.findViewById<View>(R.id.tvRealAmount) as TextView
        val investment = getChild(groupPosition, childPosition) as Investment
        tvItem.text = investment.name
        tvRealAmount.text = investment.stringAmount
        return convertView
    }

    override fun isChildSelectable(groupPosition: Int, childPosition: Int): Boolean {
        // returns if the subitem (item of group) can be selected
        return true
    }
}