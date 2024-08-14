package com.example.planit

import android.content.Context
import android.content.Intent
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Button
import android.widget.LinearLayout
import android.widget.TextView
import androidx.recyclerview.widget.RecyclerView
import com.android.volley.Request
import com.android.volley.toolbox.JsonObjectRequest
import org.json.JSONObject
import java.time.format.DateTimeFormatter
import kotlin.math.abs

class PaymentsAdapter(private val paymentList: MutableList<Payment>, private val mContext: Context) : RecyclerView.Adapter<PaymentsAdapter.EventViewHolder>() {

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): EventViewHolder {
        val itemView = LayoutInflater.from(parent.context).inflate(R.layout.payment_item, parent, false)
        return EventViewHolder(itemView)
    }

    override fun onBindViewHolder(holder: EventViewHolder, position: Int) {
        val payment = paymentList[position]
        if(payment.Amount > 0)
        {
            holder.nameTextView.text = ("You owe " + payment.name)
        }
        else
        {
            holder.nameTextView.text = (payment.name + " owes you")
        }
        holder.paymentTextView.text = abs(payment.Amount).toString()
        holder.settlePaymentButton.setOnClickListener{
            val pos = holder.adapterPosition
            val payment = paymentList[pos]
            settlePayment(payment)
        }


    }

    private fun settlePayment(payment: Payment)
    {
        val pos = paymentList.indexOf(payment)
        val uriObj = BaseUrl()
        val url = uriObj.url + "/payments/settle"
        val reqstr = """
            {
                "other_id": ${payment.payeeId},
                "amount": ${payment.Amount}
            }
        """.trimIndent()

        val reqJson = JSONObject(reqstr)

        val request = JsonObjectRequest(
            Request.Method.POST, url, reqJson,
            { response ->
                if (response.has("success")) {
                    paymentList[pos].Amount = 0.0
                    notifyDataSetChanged()
                }
            },
            { error ->
                println("Error: $error")
            }
        )

        val queue = VolleySingleton.getInstance(mContext).requestQueue
        queue.add(request)

    }


    override fun getItemCount() = paymentList.size

    public fun setEvents(events: List<Payment>) {
        // clear eventList
        paymentList.clear()
        // add new events
        paymentList.addAll(events)
        // notify adapter of change
        notifyDataSetChanged()

    }

    private fun startEventDetailsActivity( eventId: Int)
    {
        // call non-empty constructor wit value eventid
        val intent = Intent(mContext, EventDetailsActivity::class.java).apply {
            putExtra("eventId", eventId)
        }
        mContext.startActivity(intent)

    }

    class EventViewHolder(itemView: View) : RecyclerView.ViewHolder(itemView) {
        val nameTextView: TextView = itemView.findViewById(R.id.nameTextView)
        val paymentTextView: TextView = itemView.findViewById(R.id.paymentTextView)
        val settlePaymentButton : Button = itemView.findViewById(R.id.settlePaymentButton)
        val linearLayoutButton: LinearLayout = itemView.findViewById(R.id.paymentItemLayout)
    }


}