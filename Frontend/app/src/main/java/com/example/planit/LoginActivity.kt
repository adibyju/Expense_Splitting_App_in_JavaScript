package com.example.planit

import android.content.Intent
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.util.Log
import android.widget.Button
import android.widget.EditText
import android.widget.TextView
import android.widget.Toast
import com.android.volley.Request
import com.android.volley.toolbox.JsonObjectRequest
import com.android.volley.toolbox.Volley
import io.github.cdimascio.dotenv.Dotenv
import org.json.JSONObject
import java.net.CookieHandler
import java.net.CookieManager


class LoginActivity : AppCompatActivity() {

    private lateinit var emailEditText: EditText
    private lateinit var passwordEditText: EditText
    private lateinit var loginButton: Button
    private lateinit var signupButton: TextView

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_login)

        emailEditText = findViewById(R.id.emailEditText)
        passwordEditText = findViewById(R.id.passwordEditText)
        loginButton = findViewById(R.id.loginButton)
        signupButton = findViewById(R.id.signupTextView)



//        val dotenv = Dotenv.configure().directory("/app/src/main/assets").filename(".env").load()
//        val apiBaseUrl = dotenv["API_URL"]

        loginButton.setOnClickListener {
            val email = emailEditText.text.toString()
            val password = passwordEditText.text.toString()
            val queue = VolleySingleton.getInstance(this).requestQueue


            if (email.isNotEmpty() && password.isNotEmpty()) {

                val reqstr = """
                    {
                        "logindata": {
                              "email": "$email",
                              "password": "$password"
                        }
                    }
                """.trimIndent()

                val reqJson = JSONObject(reqstr)

                val uriObj = BaseUrl()
                val url = uriObj.url +  "/auth/login/"

                val loginRequest = JsonObjectRequest(
                    Request.Method.POST,
                    url,
                    reqJson,
                    { response ->
                        Log.e("Volley", "Response is: $response")

                        if (response.getBoolean("succes")) {
                            startHomeActivity()
                            finish()
                        }
                    },
                    { error ->
//                        Toast.makeText(this, error.message, Toast.LENGTH_SHORT).show()
                        Log.e("LoginActivity", error.message.toString())
                        error.printStackTrace()
                        }
                )


                queue.add(loginRequest)

            } else {
                // show error message if email or password is empty
                Toast.makeText(this, "Please enter email and password", Toast.LENGTH_SHORT).show()
            }
        }

        signupButton.setOnClickListener {
            startSignupActivity()
        }
    }

    private fun startMainActivity() {
        val intent = Intent(this, MainActivity::class.java)
        startActivity(intent)
        finish()
    }

    private fun startSignupActivity() {
        val intent = Intent(this, SignupActivity::class.java)
        startActivity(intent)
        finish()
    }

    private fun startHomeActivity() {
        val intent = Intent(this, HomeActivity::class.java)
        startActivity(intent)
        finish()
    }
}
