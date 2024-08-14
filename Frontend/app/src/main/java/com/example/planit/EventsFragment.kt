package com.example.planit

import android.content.ClipData.newIntent
import android.content.Context
import android.content.Intent
import android.os.Bundle
import android.util.Log
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.ImageButton
import android.widget.LinearLayout
import android.widget.Toast
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import androidx.swiperefreshlayout.widget.SwipeRefreshLayout
import com.android.volley.Request
import com.android.volley.toolbox.JsonObjectRequest
import com.android.volley.toolbox.Volley
import com.google.android.material.tabs.TabLayout
import java.net.CookieHandler
import java.net.CookieManager
import java.time.LocalDateTime
import java.time.format.DateTimeFormatter

// TODO: Rename parameter arguments, choose names that match
// the fragment initialization parameters, e.g. ARG_ITEM_NUMBER
private const val ARG_PARAM1 = "param1"
private const val ARG_PARAM2 = "param2"

/**
 * A simple [Fragment] subclass.
 * Use the [EventsFragment.newInstance] factory method to
 * create an instance of this fragment.
 */
class EventsFragment : Fragment() {
    // TODO: Rename and change types of parameters
    private lateinit var recyclerView: RecyclerView
    private lateinit var adapter: EventAdapter
    private lateinit var mContext : Context
    private lateinit var swipeRefreshLayout: SwipeRefreshLayout
    private lateinit var addEventButton: ImageButton






    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)

    }

    override fun onAttach(context: Context) {
        super.onAttach(context)
        mContext = context
    }

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {

        val view = inflater.inflate(R.layout.fragment_events, container, false)
        recyclerView = view.findViewById(R.id.eventRecyclerView)
        recyclerView.layoutManager = LinearLayoutManager(mContext)

        addEventButton = view.findViewById(R.id.addEventButton)

        addEventButton.setOnClickListener {
            val intent = Intent(mContext, CreateEventActivity::class.java)
            startActivity(intent)
        }

        swipeRefreshLayout = view.findViewById<SwipeRefreshLayout>(R.id.swipeRefreshLayout)
        swipeRefreshLayout.setOnRefreshListener {
            swipeRefreshLayout.isRefreshing = true
            fetchEvents()
            swipeRefreshLayout.isRefreshing = false
        }

        fetchEvents()

        return view

    }

    override fun onResume() {
        super.onResume()
        fetchEvents()

    }

    private fun fetchEvents()
    {

        val queue = VolleySingleton.getInstance(mContext).requestQueue

        val uriObj = BaseUrl()
        val url = uriObj.url + "/events/"
        val eventRequest = JsonObjectRequest(
            Request.Method.GET, url, null,
            { response ->
                Log.e("EventsFragment", "Response: %s".format(response.toString()))
                if (response.has("err"))
                {
                    Log.e("EventsFragment", "Error: %s".format(response.getString("err")))
                    Toast.makeText(mContext, "Please login again", Toast.LENGTH_SHORT).show()
                    startLoginActivity()
                }
                else
                {
                    val events = response.getJSONArray("eventsList")
                    val eventList = mutableListOf<Event>()
                    for (i in 0 until events.length())
                    {
                        val event = events.getJSONObject(i)
                        val id = event.getInt("id")
                        val name = event.getString("name")
                        val description = event.getString("description")
                        val date = LocalDateTime.parse(event.getString("date"), DateTimeFormatter.ofPattern("yyyy-MM-dd HH:mm:ss"))
                        val eventObj = Event(id, name,  date,  description)
                        eventList.add(eventObj)
                    }
                    adapter = EventAdapter(eventList, mContext)
                    recyclerView.adapter = adapter
                }
            },
            { error ->
                error.printStackTrace()
                Log.e("EventsFragment", "Error: %s".format(error.toString()))
                Toast.makeText(mContext, "Please login again", Toast.LENGTH_SHORT).show()
                startLoginActivity()
            }
        )

        queue.add(eventRequest)


    }

    private fun startLoginActivity()
    {
        val intent = Intent(mContext, LoginActivity::class.java)
        startActivity(intent)
    }

    companion object {
        /**
         * Use this factory method to create a new instance of
         * this fragment using the provided parameters.
         *
         * @param param1 Parameter 1.
         * @param param2 Parameter 2.
         * @return A new instance of fragment EventsFragment.
         */
        // TODO: Rename and change types and number of parameters
        @JvmStatic
        fun newInstance(param1: String, param2: String) =
            EventsFragment().apply {
                arguments = Bundle().apply {
                    putString(ARG_PARAM1, param1)
                    putString(ARG_PARAM2, param2)
                }
            }
    }
}