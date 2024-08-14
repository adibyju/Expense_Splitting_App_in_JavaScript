package com.example.planit

import android.content.Context
import android.os.Bundle
import android.util.Log
import android.view.Gravity
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Button
import android.widget.EditText
import android.widget.ImageButton
import android.widget.PopupWindow
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import androidx.swiperefreshlayout.widget.SwipeRefreshLayout
import com.android.volley.Request
import com.android.volley.toolbox.JsonObjectRequest
import org.json.JSONObject

// TODO: Rename parameter arguments, choose names that match
// the fragment initialization parameters, e.g. ARG_ITEM_NUMBER
private const val ARG_PARAM1 = "param1"
private const val ARG_PARAM2 = "param2"

/**
 * A simple [Fragment] subclass.
 * Use the [EventItemsFragment.newInstance] factory method to
 * create an instance of this fragment.
 */
class EventItemsFragment( private val eventId: Int) : Fragment() {
    // TODO: Rename and change types of parameters
    private lateinit var recyclerView: RecyclerView
    private lateinit var adapter: ItemsAdapter
    private lateinit var mContext : Context
    private lateinit var swipeRefreshLayout: SwipeRefreshLayout
    private lateinit var addItemButton: ImageButton
    private lateinit var removeItemsButton: ImageButton

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

        val view = inflater.inflate(R.layout.fragment_event_items, container, false)
        recyclerView = view.findViewById(R.id.eventItemsRecyclerView)
        recyclerView.layoutManager = LinearLayoutManager(mContext)

        addItemButton = view.findViewById(R.id.addEventItemButton)
        removeItemsButton = view.findViewById(R.id.deleteEventItemButton)

      addItemButton.setOnClickListener {
        popupItemCreator()
      }

        swipeRefreshLayout = view.findViewById(R.id.swipeRefreshLayout)
        swipeRefreshLayout.setOnRefreshListener {
            swipeRefreshLayout.isRefreshing = true
            getItems()
            swipeRefreshLayout.isRefreshing = false
        }

        getItems()


        return view
    }

    companion object {
        /**
         * Use this factory method to create a new instance of
         * this fragment using the provided parameters.
         *
         * @param param1 Parameter 1.
         * @param param2 Parameter 2.
         * @return A new instance of fragment EventItemsFragment.
         */
        // TODO: Rename and change types and number of parameters
        @JvmStatic
        fun newInstance(param1: String, param2: String, eventId: Int) =
            EventItemsFragment(eventId).apply {
                arguments = Bundle().apply {
                    putString(ARG_PARAM1, param1)
                    putString(ARG_PARAM2, param2)
                }
            }
    }

    private fun getItems() {
        val uriObj = BaseUrl()
        val url = uriObj.url + "/events/items/${eventId}"
        val eventItemsRequest = JsonObjectRequest(
            Request.Method.GET, url, null,
            { response ->
                Log.e("Rest Response", response.toString())
                Log.e( "Id Rest Response", eventId.toString() )

                if (response.has("items")) {
                    val items = response.getJSONArray("items")
                    val itemsList = mutableListOf<Item>()
                    for (i in 0 until items.length()) {
                        val iteme = items.getJSONObject(i)
                        val item = Item(
                            iteme.getInt("id"),
                            iteme.getString("name"),
                            false,
                            iteme.getString("description"),
                            iteme.getString("expense").toDouble()
                        )
                        itemsList.add(item)
                    }
                    adapter = ItemsAdapter(itemsList, mContext, eventId)
                    recyclerView.adapter = adapter

                }
                else
                    Log.e("Rest Response", "No items")
            },
            { error ->
                Log.e("Rest Response", error.toString())
                Log.e( "Id Rest Response", eventId.toString() )
            }

        )

        val queue = VolleySingleton.getInstance(mContext).requestQueue
        queue.add(eventItemsRequest)
    }

    private fun popupItemCreator() {
        val inflater = LayoutInflater.from(context)
        val popupView = inflater.inflate(R.layout.popup_item_form, null)

        val popupWindow = PopupWindow(popupView, ViewGroup.LayoutParams.WRAP_CONTENT, ViewGroup.LayoutParams.WRAP_CONTENT)
        popupWindow.isFocusable = true

        // Get references to the UI elements in the popup form
        val nameInput = popupView.findViewById<EditText>(R.id.editTextName)
        val descInput = popupView.findViewById<EditText>(R.id.editTextDesc)
        val expenseInput = popupView.findViewById<EditText>(R.id.editTextExpense)
        val saveButton = popupView.findViewById<Button>(R.id.buttonSave)
        val cancelButton = popupView.findViewById<Button>(R.id.buttonCancel)

        // Set an OnClickListener for the "Save" button
        saveButton.setOnClickListener {
            val name = nameInput.text.toString()
            val desc = descInput.text.toString()
            val expense = expenseInput.text.toString()

            createItem(name, desc, expense)
            getItems()
            popupWindow.dismiss()
        }

        // Set an OnClickListener for the "Cancel" button
        cancelButton.setOnClickListener {
            // Dismiss the popup window
            popupWindow.dismiss()
        }

        // Show the popup window
        popupWindow.showAtLocation(popupView, Gravity.CENTER, 0, 0)
    }


    private fun createItem(name: String, desc: String, expense: String) {
        val uriObj = BaseUrl()
        val url = uriObj.url + "/items/create"

        val reqstr = """
            {
                "name": "$name",
                "descr": "$desc",
                "expense": "$expense",
                "event_id": "$eventId"
            }
        """.trimIndent()

        val reqJsonObj = JSONObject(reqstr)

        val createItemRequest = JsonObjectRequest(
            Request.Method.POST, url, reqJsonObj,
            { response ->
                Log.e("Rest Response", response.toString())
            },
            { error ->
                Log.e("Rest Response", error.toString())
            }

        )

        val queue = VolleySingleton.getInstance(mContext).requestQueue
        queue.add(createItemRequest)



    }

}