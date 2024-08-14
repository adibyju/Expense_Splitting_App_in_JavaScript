package com.example.planit

import android.content.Context
import android.util.Log
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.CheckBox
import android.widget.LinearLayout
import android.widget.TextView
import androidx.recyclerview.widget.RecyclerView

class ParticipantsAdapter (private val itemList: List<Participant>, private val mContext: Context) : RecyclerView.Adapter<ParticipantsAdapter.ParticipantViewHolder>() {

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): ParticipantViewHolder {
        val itemView = LayoutInflater.from(parent.context).inflate(R.layout.participant_item, parent, false)
        return ParticipantViewHolder(itemView)
    }

    override fun onBindViewHolder(holder: ParticipantViewHolder, position: Int) {
        val item = itemList[position]
        holder.nameTextView.text = item.name
        holder.checkBox.isChecked = false
        holder.checkBox.setOnClickListener{
            holder.checkBox.isChecked = holder.checkBox.isChecked
            itemList[position].isChecked = holder.checkBox.isChecked
            Log.e("ParticipantAdapter", "itemList[position].isChecked: ${getCheckedItems()}")
        }
    }

    override fun getItemCount() = itemList.size

    public fun setItems(items: List<Participant>) {
        // clear itemList
        itemList.toMutableList().clear()
        // add new items
        itemList.toMutableList().addAll(items)
        // notify adapter of change
        notifyDataSetChanged()

    }

    public fun getCheckedItems(): List<Participant> {
        val checkedItems = mutableListOf<Participant>()
        for (item in itemList) {
            if (item.isChecked) {
                checkedItems.add(item)
            }
        }
        return checkedItems
    }

    class ParticipantViewHolder(itemView: View) : RecyclerView.ViewHolder(itemView) {
        val nameTextView: TextView = itemView.findViewById(R.id.nameTextView)
        val checkBox: CheckBox = itemView.findViewById(R.id.checkBox)
        val linearLayoutButton: LinearLayout = itemView.findViewById(R.id.itemDetLayout)
    }
}