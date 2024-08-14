package com.example.planit

import android.content.Context
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.util.AttributeSet
import android.util.Log
import android.view.View
import android.view.WindowManager
import android.widget.EditText
import android.widget.ImageButton
import android.widget.Toast
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import com.android.volley.Request
import com.android.volley.toolbox.JsonObjectRequest
import org.json.JSONArray
import org.json.JSONObject

class EditExpensesActivity : AppCompatActivity() {

    private lateinit var recyclerView: RecyclerView
    private lateinit var adapter: ItemExpensesAdapter
    private lateinit var mContext : Context
    private lateinit var doneButton: ImageButton
    private lateinit var itemName: EditText
    private lateinit var itemExpense: EditText
    private lateinit var itemDesc: EditText
    private var itemId: Int = 0
    private var eventId: Int = 0
    private lateinit var partyExpenses: MutableList<ItemExpenseDistribution>


    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_edit_expenses)
        window.setSoftInputMode(WindowManager.LayoutParams.SOFT_INPUT_ADJUST_RESIZE)

        mContext = this
        itemId = intent.getIntExtra("itemId", 0)
        eventId = intent.getIntExtra("eventId", 0)

        recyclerView = findViewById(R.id.eventRecyclerView)
        recyclerView.layoutManager = LinearLayoutManager(mContext)

        doneButton = findViewById(R.id.ic_baseline_done_24)
        itemName = findViewById(R.id.nameTextView)
        itemExpense = findViewById(R.id.expenseTextView)
        itemDesc = findViewById(R.id.descriptionTextView)

        doneButton.setOnClickListener{
            var itemNameText = itemName.text.toString()
            var itemExpense = itemExpense.text.toString().toDouble()
            var itemDescText = itemDesc.text.toString()

            var expenseDist = adapter.fetchExpenseDistribution()
            var payings = mutableListOf<JSONObject>()
            var owings = mutableListOf<JSONObject>()

            var paysum = 0.0
            var owesum = 0.0

            for (i in 0 until expenseDist.size) {

                var paying = JSONObject()
                paying.put("user_id", expenseDist[i].id)
                paying.put("amount", expenseDist[i].payed)
                payings.add(paying)
                paysum += expenseDist[i].payed

                var owing = JSONObject()
                owing.put("user_id", expenseDist[i].id)
                owing.put("amount", expenseDist[i].owed)
                owings.add(owing)
                owesum += expenseDist[i].owed

            }

            if (paysum != itemExpense || owesum != itemExpense) {
                Log.e("Edit Expenses Error", "Paysum: $paysum, Owesum: $owesum, Expense: $itemExpense")
                Toast.makeText(mContext, "<Inconsistent Sums>Paysum: $paysum, Owesum: $owesum, Expense: $itemExpense", Toast.LENGTH_LONG).show()
                return@setOnClickListener
            }

            var item = JSONObject()
            item.put("name", itemNameText)
            item.put("expense", itemExpense)
            item.put("descr", itemDescText)

            var payers = JSONArray(payings)
            var owers = JSONArray(owings)

            item.put("payers", payers)
            item.put("owers", owers)

            Log.e("Edit Expenses", item.toString())

            updateItem(item)



        }

        fetchParties()

    }

    private fun updateItem( item: JSONObject)
    {
        val uriObj = BaseUrl()
        val url = uriObj.url + "/items/$itemId/update"

        var updateItemRequest = JsonObjectRequest(
            Request.Method.POST, url, item,
            { response ->
                Log.e("Rest Item Response", response.toString())
                Toast.makeText(mContext, "Item updated", Toast.LENGTH_LONG).show()
            },
            { error ->
                error.printStackTrace()
                Log.e("Rest Item Error", error.toString())
                Toast.makeText(mContext, "Item update failed", Toast.LENGTH_LONG).show()
            }
        )

        VolleySingleton.getInstance(mContext).addToRequestQueue(updateItemRequest)


    }


    private fun fetchItem() {
        val uriObj = BaseUrl()
        val url = uriObj.url + "/items/item/$itemId"

        val eventRequest = JsonObjectRequest(
            Request.Method.GET, url, null,
            { response ->
                Log.e("Rest Item Response", response.toString())
                if (response.has("item")) {
                    val item = response.getJSONObject("item")
                    val name = item.getString("name")
                    val expense = item.getDouble("expense")
                    val description = item.getString("description")
                    itemName.setText(name)
                    itemExpense.setText(expense.toString())
                    itemDesc.setText(description)
                }
                else {
                    Log.e("Rest Item Error", "No item found")
                }
                if (response.has("payings")) {
                    val paying = response.getJSONArray("payings")
                    for (i in 0 until paying.length()) {
                        val payer = paying.getJSONObject(i)
                        val payer_id = payer.getInt("payer_id")
                        val amount = payer.getDouble("amount")
                        for (j in 0 until partyExpenses.size) {
                            if (partyExpenses[j].id == payer_id) {
                                partyExpenses[j].payed = amount
                            }
                        }
                    }
                }
                else {
                    Log.e("Rest Item Error", "No paying found")
                }
                if (response.has("owings")) {
                    val owing = response.getJSONArray("owings")
                    for (i in 0 until owing.length()) {
                        val ower = owing.getJSONObject(i)
                        val ower_id = ower.getInt("ower_id")
                        val amount = ower.getDouble("amount")
                        for (j in 0 until partyExpenses.size) {
                            if (partyExpenses[j].id == ower_id) {
                                partyExpenses[j].owed = amount
                            }
                        }
                    }
                }
                else {
                    Log.e("Rest Item Error", "No owing found")
                }
                adapter = ItemExpensesAdapter(partyExpenses)
                recyclerView.adapter = adapter
            },
            { error ->
                error.printStackTrace()
                Log.e("Rest Item Error", error.toString())
            }
        )

        val queue = VolleySingleton.getInstance(this).requestQueue
        queue.add(eventRequest)

    }

    private fun fetchParties() {
        val uriObj = BaseUrl()
        val url = uriObj.url + "/events/participants/$eventId"

        val eventRequest = JsonObjectRequest(
            Request.Method.GET, url, null,
            { response ->
                Log.e("Rest Participants Response", response.toString())
                if (response.has("participants")) {
                    val participants = response.getJSONArray("participants")
                    partyExpenses = mutableListOf<ItemExpenseDistribution>()
                    for (i in 0 until participants.length()) {
                        val participant = participants.getJSONObject(i)
                        val name =
                            participant.getString("fname") + " " + participant.getString("lname")
                        val id = participant.getInt("participant_id")
                        val payed = 0.0
                        val owed = 0.0
                        val partyExpense = ItemExpenseDistribution(id, name, payed, owed)
                        partyExpenses.add(partyExpense)
                    }
                    fetchItem()

                } else {
                    Log.e("Rest Participants Error", "No participants found")
                }
            },
            { error ->
                error.printStackTrace()
                Log.e("Rest Participants Error", error.toString())
            }
        )

        val queue = VolleySingleton.getInstance(this).requestQueue
        queue.add(eventRequest)

    }
}