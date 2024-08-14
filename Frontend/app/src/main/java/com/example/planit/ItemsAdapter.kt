package com.example.planit

import android.content.ClipData.newIntent
import android.content.Context
import android.content.Intent
import android.util.Log
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.CheckBox
import android.widget.LinearLayout
import android.widget.TextView
import androidx.recyclerview.widget.RecyclerView

class ItemsAdapter (private val itemList: List<Item>, private val mContext: Context, private val eventId: Int) : RecyclerView.Adapter<ItemsAdapter.ItemViewHolder>() {

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): ItemViewHolder {
        val itemView = LayoutInflater.from(parent.context).inflate(R.layout.item_item, parent, false)
        return ItemViewHolder(itemView)
    }

    override fun onBindViewHolder(holder: ItemViewHolder, position: Int) {
        val item = itemList[position]
        holder.nameTextView.text = item.name
        holder.descTextView.text = item.description
        holder.expenseTextView.text = item.expense.toString()
        holder.checkBox.isChecked = false
        holder.checkBox.setOnClickListener{
            holder.checkBox.isChecked = holder.checkBox.isChecked
            itemList[position].isChecked = holder.checkBox.isChecked
            Log.e("ItemAdapter", "itemList[position].isChecked: ${getCheckedItems()}")
        }
        holder.linearLayoutButton.setOnClickListener{
            startEditExpensesActivity(item.id, eventId)
        }
    }

    override fun getItemCount() = itemList.size

    public fun setItems(items: List<Item>) {
        // clear itemList
        itemList.toMutableList().clear()
        // add new items
        itemList.toMutableList().addAll(items)
        // notify adapter of change
        notifyDataSetChanged()

    }

    public fun getCheckedItems(): List<Item> {
        val checkedItems = mutableListOf<Item>()
        for (item in itemList) {
            if (item.isChecked) {
                checkedItems.add(item)
            }
        }
        return checkedItems
    }

    private fun startEditExpensesActivity(itemId: Int, eventId: Int) {
        val intent = Intent(mContext, EditExpensesActivity::class.java).apply {
            putExtra("itemId", itemId)
            putExtra("eventId", eventId)
        }
        mContext.startActivity(intent)
    }


    class ItemViewHolder(itemView: View) : RecyclerView.ViewHolder(itemView) {
        val nameTextView: TextView = itemView.findViewById(R.id.nameTextView)
        val checkBox: CheckBox = itemView.findViewById(R.id.checkBox)
        val descTextView: TextView = itemView.findViewById(R.id.descriptionTextView)
        val expenseTextView: TextView = itemView.findViewById(R.id.expenseTextView)
        val linearLayoutButton: LinearLayout = itemView.findViewById(R.id.itemDetLayout)
    }
}