package com.example.planit

import androidx.fragment.app.Fragment
import androidx.fragment.app.FragmentManager
import androidx.fragment.app.FragmentPagerAdapter

class EventDetailPagerAdapter(fm: FragmentManager, eventId: Int) : FragmentPagerAdapter(fm) {
    private val fragments = listOf(
        EventItemsFragment(eventId),
        EventParticipantsFragment(eventId)
    )

    override fun getCount(): Int = fragments.size

    override fun getItem(position: Int): Fragment = fragments[position]

    override fun getPageTitle(position: Int): CharSequence? {
        return when (position) {
            0 -> "Items"
            1 -> "Participants"
            else -> null
        }
    }
}
