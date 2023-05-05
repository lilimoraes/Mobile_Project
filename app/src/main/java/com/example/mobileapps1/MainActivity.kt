package com.example.mobileapps1

import android.content.Intent
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.util.Log
import android.widget.Button
import android.widget.EditText
import android.widget.Toast
import okhttp3.*
import okhttp3.RequestBody.Companion.toRequestBody
import java.io.IOException

class MainActivity : AppCompatActivity() {
    private val client = OkHttpClient()
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_main)

        val username = findViewById<EditText>(R.id.username)
        val password = findViewById<EditText>(R.id.password)
        val signIn = findViewById<Button>(R.id.signIn)
        val signUp = findViewById<Button>(R.id.signUp)

        signIn.setOnClickListener {
            val usernameString = username.text.toString()
            val passwordString = password.text.toString()
            val requestString = """ {
                    "username": ${usernameString},
                    "password": ${passwordString},
                    |} """.trimIndent()
            Log.i("MAIN_ACT", "Request Body: $requestString")
            val request = Request.Builder()
                .url("https://fakestoreapi.com/auth/login")
                .post(requestString.toRequestBody())
                .build()

            client.newCall(request).enqueue(object : Callback {
                override fun onFailure(call: Call, e: IOException) {
                    Log.i("MAIN_ACT", "Failure: $e")
                    }
                override fun onResponse(call: Call, response: Response) {
                    if (response.isSuccessful) {
                        val responseString = response.body?.string()
                        Log.i("MAIN_ACT", "Success: $responseString")
                    } else {
                        Log.i("MAIN_ACT", "Unsuccessfull")
                    }
                }
            })
        }

        signUp.setOnClickListener {
            val intent = Intent(this, SignUpActivity::class.java)
            startActivity(intent)

        }
    }
}