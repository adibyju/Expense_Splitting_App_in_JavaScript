package com.example.planit

import android.text.Editable
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.EditText
import android.widget.TextView
import androidx.recyclerview.widget.RecyclerView
import android.text.TextWatcher
import android.util.Log


class ItemExpensesAdapter(private val itemExpenseDistributionList: List<ItemExpenseDistribution>) : RecyclerView.Adapter<ItemExpensesAdapter.ItemExpenseDistributionViewHolder>() {

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): ItemExpenseDistributionViewHolder {
        val itemView = LayoutInflater.from(parent.context).inflate(R.layout.expense_dist_item, parent, false)
        return ItemExpenseDistributionViewHolder(itemView)
    }

    override fun onBindViewHolder(holder: ItemExpenseDistributionViewHolder, position: Int) {
        val itemExpenseDistribution = itemExpenseDistributionList[position]
        holder.nameTextView.text = itemExpenseDistribution.name
        holder.payedTextView.setText(itemExpenseDistribution.payed.toString())
        holder.owedTextView.setText(itemExpenseDistribution.owed.toString())
        holder.payedTextView.addTextChangedListener(object : TextWatcher {
            override fun afterTextChanged(s: Editable?) {
                var pos = holder.adapterPosition
                if (s.toString() == "") {
                    itemExpenseDistributionList[pos].payed = 0.0
                }
                else {
                    itemExpenseDistributionList[pos].payed = s.toString().toDouble()
                }
                Log.e("ItemExpensesAdapter", "itemExpenseDistributionList[pos].payed: ${itemExpenseDistributionList[pos].payed}")
            }
            override fun beforeTextChanged(s: CharSequence?, start: Int, count: Int, after: Int) {
                // do nothing
            }
            override fun onTextChanged(s: CharSequence?, start: Int, before: Int, count: Int) {
                // do nothing
            }
        })
        holder.owedTextView.addTextChangedListener(object : TextWatcher {
            override fun afterTextChanged(s: Editable?) {
                var pos = holder.adapterPosition
                if (s.toString() == "") {
                    itemExpenseDistributionList[pos].owed = 0.0
                }
                else {
                    itemExpenseDistributionList[pos].owed = s.toString().toDouble()
                }
                Log.e("ItemExpensesAdapter", "itemExpenseDistributionList[pos].owed: ${itemExpenseDistributionList[pos].owed}")
            }
            override fun beforeTextChanged(s: CharSequence?, start: Int, count: Int, after: Int) {
                // do nothing
            }
            override fun onTextChanged(s: CharSequence?, start: Int, before: Int, count: Int) {
                // do nothing
            }
        })
    }

    public fun fetchExpenseDistribution(): List<ItemExpenseDistribution> {
        return itemExpenseDistributionList
    }

    override fun getItemCount() = itemExpenseDistributionList.size


    class ItemExpenseDistributionViewHolder(itemView: View) : RecyclerView.ViewHolder(itemView) {
        val nameTextView: TextView = itemView.findViewById(R.id.nameTextView)
        val payedTextView: EditText = itemView.findViewById(R.id.expenseEditText)
        val owedTextView: EditText = itemView.findViewById(R.id.expenseEditText2)
    }
}