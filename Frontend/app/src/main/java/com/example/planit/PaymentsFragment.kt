package com.example.planit

import android.content.Context
import android.os.Bundle
import android.util.Log
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import androidx.swiperefreshlayout.widget.SwipeRefreshLayout
import com.android.volley.Request
import com.android.volley.toolbox.JsonObjectRequest

// TODO: Rename parameter arguments, choose names that match
// the fragment initialization parameters, e.g. ARG_ITEM_NUMBER
private const val ARG_PARAM1 = "param1"
private const val ARG_PARAM2 = "param2"

/**
 * A simple [Fragment] subclass.
 * Use the [PaymentsFragment.newInstance] factory method to
 * create an instance of this fragment.
 */
class PaymentsFragment : Fragment() {
    // TODO: Rename and change types of parameters

    private lateinit var mContext : Context
    private lateinit var recyclerView: RecyclerView
    private lateinit var adapter: PaymentsAdapter
    private lateinit var swipeRefreshLayout: SwipeRefreshLayout


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
        val view: View = inflater.inflate(R.layout.fragment_payments, container, false)
        recyclerView = view.findViewById(R.id.eventRecyclerView)
        recyclerView.layoutManager = LinearLayoutManager(mContext)

        swipeRefreshLayout = view.findViewById(R.id.swipeRefreshLayout)



        swipeRefreshLayout.setOnRefreshListener {
            swipeRefreshLayout.isRefreshing = true
            fetchPayments()
            swipeRefreshLayout.isRefreshing = false
        }
        fetchPayments()
        return view;

    }


    companion object {
        /**
         * Use this factory method to create a new instance of
         * this fragment using the provided parameters.
         *
         * @param param1 Parameter 1.
         * @param param2 Parameter 2.
         * @return A new instance of fragment PaymentsFragment.
         */
        // TODO: Rename and change types and number of parameters
        @JvmStatic
        fun newInstance(param1: String, param2: String) =
            PaymentsFragment().apply {
                arguments = Bundle().apply {
                    putString(ARG_PARAM1, param1)
                    putString(ARG_PARAM2, param2)
                }
            }
    }

    private fun fetchPayments() {
        val uriObj = BaseUrl()
        val url = uriObj.url + "/payments"

        val request = JsonObjectRequest(
            Request.Method.GET, url, null,
            { response ->
                Log.e("Rest Response", response.toString())
                if(response.has("payments")) {
                    val payments = response.getJSONArray("payments")
                    val paymentsList = ArrayList<Payment>()
                    for (i in 0 until payments.length()) {
                        val paymente = payments.getJSONObject(i)
                        val other_id = paymente.getInt("other_id")
                        val name = paymente.getString("fname") + " " + paymente.getString("lname")
                        val sum = paymente.getDouble("sum")
                        val payment = Payment(other_id, name, sum)
                        paymentsList.add(payment)
                    }
                    adapter = PaymentsAdapter(paymentsList, mContext)
                    recyclerView.adapter = adapter
                }
            },
            { error ->
                Log.d("Error", error.toString())
                swipeRefreshLayout.isRefreshing = false
            }
        )

        val queue = VolleySingleton.getInstance(mContext).requestQueue
        queue.add(request)
    }
}