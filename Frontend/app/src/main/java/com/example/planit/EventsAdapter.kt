package com.example.planit

import android.content.Context
import android.content.Intent
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.LinearLayout
import android.widget.TextView
import androidx.core.content.ContextCompat.startActivity
import androidx.recyclerview.widget.RecyclerView
import java.time.format.DateTimeFormatter

class EventAdapter(private val eventList: List<Event>, private val mContext: Context) : RecyclerView.Adapter<EventAdapter.EventViewHolder>() {



    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): EventViewHolder {
        val itemView = LayoutInflater.from(parent.context).inflate(R.layout.event_item, parent, false)
        return EventViewHolder(itemView)
    }

    override fun onBindViewHolder(holder: EventViewHolder, position: Int) {
        val event = eventList[position]
        holder.nameTextView.text = event.name
        holder.dateTextView.text = event.date.format(DateTimeFormatter.ofPattern("dd/MM/yyyy HH:mm"))
        holder.descriptionTextView.text = event.description
        holder.linearLayoutButton.setOnClickListener {
            startEventDetailsActivity(event.id)

        }
    }

    override fun getItemCount() = eventList.size

    public fun setEvents(events: List<Event>) {
        // clear eventList
        eventList.toMutableList().clear()
        // add new events
        eventList.toMutableList().addAll(events)
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
        val dateTextView: TextView = itemView.findViewById(R.id.dateTextView)
        val descriptionTextView: TextView = itemView.findViewById(R.id.descriptionTextView)
        val linearLayoutButton: LinearLayout = itemView.findViewById(R.id.eventDetLayout)

    }
}
