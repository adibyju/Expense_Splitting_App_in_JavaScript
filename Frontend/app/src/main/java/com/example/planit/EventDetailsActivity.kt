package com.example.planit

import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import androidx.viewpager.widget.ViewPager
import androidx.viewpager2.widget.ViewPager2
import com.google.android.material.tabs.TabLayout

class EventDetailsActivity() : AppCompatActivity() {

    private lateinit var viewPager: ViewPager
    private lateinit var tabLayout: TabLayout
    private var eventId: Int = 0
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_event_details)

        viewPager = findViewById(R.id.view_pager)
        tabLayout = findViewById(R.id.tab_layout)

        eventId = intent.getIntExtra("eventId", 0)

        val adapter = EventDetailPagerAdapter(supportFragmentManager, eventId)
        viewPager.adapter = adapter

        tabLayout.setupWithViewPager(viewPager)
    }

    fun getEventId(): Int {
        return eventId
    }
}