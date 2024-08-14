package com.example.planit

import android.content.Intent
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.util.Log
import android.widget.Button
import android.widget.EditText
import android.widget.Toast
import com.android.volley.Request
import com.android.volley.toolbox.JsonObjectRequest
import com.android.volley.toolbox.Volley
import io.github.cdimascio.dotenv.Dotenv
import org.json.JSONObject
import java.net.CookieHandler
import java.net.CookieManager

class SignupActivity : AppCompatActivity() {

    private lateinit var emailEditText: EditText
    private lateinit var passwordEditText: EditText
    private lateinit var signupButton: Button
    private lateinit var firstnameEditText: EditText
    private lateinit var lastnameEditText: EditText
    private lateinit var middlenameEditText: EditText
    private lateinit var mobileEditText: EditText



    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_signup)

        emailEditText = findViewById(R.id.emailEditText)
        passwordEditText = findViewById(R.id.passwordEditText)
        signupButton = findViewById(R.id.signupButton)
        firstnameEditText = findViewById(R.id.firstnameEditText)
        lastnameEditText = findViewById(R.id.lastnameEditText)
        middlenameEditText = findViewById(R.id.middlenameEditText)
        mobileEditText = findViewById(R.id.mobileEditText)

        val queue = VolleySingleton.getInstance(this).requestQueue

//        val dotenv = Dotenv.configure().directory("/app/src/main/assets").filename(".env").load()
//        val apiBaseUrl = dotenv["API_URL"]

        signupButton.setOnClickListener {
            val email = emailEditText.text.toString()
            val password = passwordEditText.text.toString()
            val firstname = firstnameEditText.text.toString()
            val lastname = lastnameEditText.text.toString()
            val middlename = middlenameEditText.text.toString()
            val mobile = mobileEditText.text.toString()

            if (email.isNotEmpty() && password.isNotEmpty() && firstname.isNotEmpty() && mobile.isNotEmpty()) {
                val reqstr = """
                    {
                        "signupdata": {
                              "email": "$email",
                              "password": "$password",
                              "fname": "$firstname",
                              "lname": "$lastname",
                              "mname": "$middlename",
                              "mobileno": "$mobile"
                        }
                    }
                """.trimIndent()

                val reqJson = JSONObject(reqstr)

                val uriObj = BaseUrl()
                val url = uriObj.url + "/auth/signup/"

                val signupRequest = JsonObjectRequest(
                    Request.Method.POST,
                    url,
                    reqJson,
                    { response ->
                        Log.e("RESPONSE", response.toString())
                        if (response.getBoolean("success")) {
                            Toast.makeText(this, "Signup successful", Toast.LENGTH_SHORT).show()
                            startLoginActivity()
                        } else {
                            Toast.makeText(this, "Signup failed", Toast.LENGTH_SHORT).show()
                        }
                    },
                    { error ->
                        Log.e("ERROR", error.toString())
                        Toast.makeText(this, "Signup failed", Toast.LENGTH_SHORT).show()
                    }
                )

                queue.add(signupRequest)
            } else {
                Toast.makeText(this, "Please fill all the fields", Toast.LENGTH_SHORT).show()
            }
        }




    }

    private fun startLoginActivity() {
        val intent = Intent(this, LoginActivity::class.java)
        startActivity(intent)
        finish()
    }



    override fun onBackPressed() {
        super.onBackPressed()
        startLoginActivity()
    }




}