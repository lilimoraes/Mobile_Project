package com.example.mobileapps1

import android.content.Intent
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.os.Handler
import android.os.Looper
import android.util.Log
import android.widget.Button
import android.widget.EditText
import android.widget.Toast
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import com.example.mobileapps1.models.Auth
import com.example.mobileapps1.models.Token
import com.google.gson.Gson
import okhttp3.*
import okhttp3.Request
import okhttp3.RequestBody.Companion.toRequestBody
import java.io.IOException

class MainActivity : AppCompatActivity() {
    private val client = OkHttpClient()
    private val gson = Gson()
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
            val auth = Auth(usernameString, passwordString)
            val request = getTokenAttachedRequestBuilder()
                .url("https://fakestoreapi.com/auth/login")
                .post(gson.toJson(auth).toRequestBody())
                .header("Content-Type", "application/json")
                .build()


            client.newCall(request).enqueue(object : Callback {
                override fun onFailure(call: Call, e: IOException) {
                    Log.i("MAIN_ACT", "Failure: $e")
                    }
                override fun onResponse(call: Call, response: Response) {
                    if (response.isSuccessful) {
                        val responseString = response.body?.string()
                        Log.i("MAIN_ACT", "Success: $responseString")
                        val tokenObject = gson.fromJson(responseString, Token::class.java)
                        Log.i("MAIN_ACT", "Token: ${tokenObject.token}")
                        val intent = Intent(this@MainActivity, ProductCategoriesActivity::class.java)
                        startActivity(intent)

                    } else {
                        Handler(Looper.getMainLooper()).post {
                            Toast.makeText(this@MainActivity, "Unsuccessful ${response.code}", Toast.LENGTH_LONG).show()
                        }
                        Log.i("MAIN_ACT", "Unsuccessfully ${response.code}")
                    }
                }
            })
        }

        signUp.setOnClickListener {
            val intent = Intent(this, SignUpActivity::class.java)
            startActivity(intent)

        }
    }

    private fun getTokenAttachedRequestBuilder(): Request.Builder {
        val token = "YourShared preferences code"
        val requestBuilder = Request.Builder()
        if (token != "")
            return requestBuilder.header("Authorization", "Bearer $token")
        else return requestBuilder

    }
}