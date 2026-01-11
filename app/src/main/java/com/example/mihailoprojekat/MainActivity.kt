package com.example.mihailoprojekat

import android.os.Bundle
import android.util.Log
import android.widget.Toast
import androidx.activity.enableEdgeToEdge
import androidx.appcompat.app.AppCompatActivity
import androidx.core.view.ViewCompat
import androidx.core.view.WindowInsetsCompat
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import com.example.mihailoprojekat.modules.Info
import com.example.mihailoprojekat.modules.Response
import com.example.mihailoprojekat.modules.recycler.itemrv.Adapter
import com.example.mihailoprojekat.services.RetrofitClient
import retrofit2.Call
import retrofit2.Callback

class MainActivity : AppCompatActivity() {
    lateinit var itemRV: RecyclerView
    var list: MutableList<Info> = mutableListOf()

    private val API_KEY = BuildConfig.GNEWS_API_KEY

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        enableEdgeToEdge()
        setContentView(R.layout.activity_main)

        ViewCompat.setOnApplyWindowInsetsListener(findViewById(R.id.main)) { v, insets ->
            val systemBars = insets.getInsets(WindowInsetsCompat.Type.systemBars())
            v.setPadding(systemBars.left, systemBars.top, systemBars.right, systemBars.bottom)
            insets
        }

        itemRV = findViewById(R.id.itemRV)
        itemRV.layoutManager = LinearLayoutManager(this)

        fetchNews()
    }

    private fun fetchNews() {
        RetrofitClient.instance.getTopHeadlines(
            category = "general",
            lang = "en",
            country = "us",
            max = 10,
            apiKey = API_KEY
        ).enqueue(object : Callback<Response> {
            override fun onResponse(
                call: Call<Response>,
                response: retrofit2.Response<Response>
            ) {
                if (response.isSuccessful) {
                    response.body()?.let { newsResponse ->
                        list = newsResponse.articles.toMutableList()
                        setupRV(list)
                        Toast.makeText(
                            this@MainActivity,
                            "Loaded ${list.size} articles",
                            Toast.LENGTH_SHORT
                        ).show()
                    }
                } else {
                    Toast.makeText(
                        this@MainActivity,
                        "Error: ${response.code()}",
                        Toast.LENGTH_SHORT
                    ).show()
                    Log.e("MainActivity", "Error: ${response.errorBody()?.string()}")
                }
            }

            override fun onFailure(call: Call<Response>, t: Throwable) {
                Toast.makeText(
                    this@MainActivity,
                    "Failed to fetch news: ${t.message}",
                    Toast.LENGTH_SHORT
                ).show()
                Log.e("MainActivity", "onFailure: ${t.message}", t)
            }
        })
    }

    fun setupRV(facts: List<Info>) {
        itemRV.adapter = Adapter(facts)
    }
}