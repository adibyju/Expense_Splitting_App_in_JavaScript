package com.example.planit

import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.util.Log
import android.widget.*
import com.android.volley.Request
import com.android.volley.toolbox.JsonObjectRequest
import org.json.JSONObject

class CreateEventActivity : AppCompatActivity() {


    private lateinit var eventNameEditText: EditText
    private lateinit var eventDescriptionEditText: EditText
    private lateinit var eventDateEditText: DatePicker
    private lateinit var eventTimeEditText: TimePicker
    private lateinit var eventCreateButton: Button

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_create_event)

        eventNameEditText = findViewById(R.id.eventNameEditText)
        eventDescriptionEditText = findViewById(R.id.eventDescriptionEditText)
        eventDateEditText = findViewById(R.id.datePicker)
        eventTimeEditText = findViewById(R.id.timePicker)
        eventCreateButton = findViewById(R.id.createEventButton)


        eventCreateButton.setOnClickListener {
            val eventName = eventNameEditText.text.toString()
            val eventDescription = eventDescriptionEditText.text.toString()
            val year = eventDateEditText.year
            val month = eventDateEditText.month
            val day = eventDateEditText.dayOfMonth
            val eventDate = "$year-$month-$day"
            val hour = eventTimeEditText.hour
            val minute = eventTimeEditText.minute
            val eventTime = "$hour:$minute:00"



            val dateTime = "$eventDate $eventTime"

            if (eventName.isNotEmpty() && eventDescription.isNotEmpty() && eventDate.isNotEmpty() && eventTime.isNotEmpty()) {
                val reqstr = """
                    {
                        "eventdata": {
                              "name": "$eventName",
                              "description": "$eventDescription",
                              "date": "$dateTime"
                        }
                    }
                """.trimIndent()

                val reqJson = JSONObject(reqstr)
                val uriObj = BaseUrl()
                val url = uriObj.url + "/events/create"

                val req = JsonObjectRequest(
                    Request.Method.POST, url, reqJson,
                    { response ->
                        Toast.makeText(this, "Event created successfully", Toast.LENGTH_SHORT)
                            .show()
                        Log.e("Response", response.toString())
                        finish()
                    },
                    { error ->
                        Toast.makeText(this, "Error creating event", Toast.LENGTH_SHORT).show()
                        Log.e("Error", error.toString())
                    }
                )

                VolleySingleton.getInstance(this).addToRequestQueue(req)
            }
        }


    }
}