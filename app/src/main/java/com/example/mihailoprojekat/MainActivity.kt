package com.example.mihailoprojekat

import android.os.Bundle
import android.util.Log
import android.widget.ImageView
import android.widget.Toast
import androidx.activity.enableEdgeToEdge
import androidx.appcompat.app.AlertDialog
import androidx.appcompat.app.AppCompatActivity
import androidx.core.view.ViewCompat
import androidx.core.view.WindowInsetsCompat
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import com.example.mihailoprojekat.modules.Country
import com.example.mihailoprojekat.modules.Info
import com.example.mihailoprojekat.modules.Response
import com.example.mihailoprojekat.modules.recycler.country.CountryAdapter
import com.example.mihailoprojekat.modules.recycler.itemrv.Adapter
import com.example.mihailoprojekat.services.RetrofitClient
import retrofit2.Call
import retrofit2.Callback

class MainActivity : AppCompatActivity() {
    lateinit var itemRV: RecyclerView
    lateinit var flagButton: ImageView
    var list: MutableList<Info> = mutableListOf()

    private val API_KEY = BuildConfig.GNEWS_API_KEY
    private var currentCountry = "us"
    private var currentFlagResId = R.drawable.earth

    private val countries = listOf(
        Country("Worldwide", "us", R.drawable.earth, null),
        Country("Canada", "ca", R.drawable.canada, "Canada"),
        Country("China", "cn", R.drawable.china, "China"),
        Country("France", "fr", R.drawable.france, "France"),
        Country("Japan", "jp", R.drawable.japan, "Japan"),
        Country("Serbia", "rs", R.drawable.serbia, "Serbia"),
        Country("United Kingdom", "gb", R.drawable.unitedkingdom, "UK OR Britain"),
        Country("United States", "us", R.drawable.unitedstates, "USA OR America")
    )

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
        flagButton = findViewById(R.id.flag_button)

        itemRV.layoutManager = LinearLayoutManager(this)

        flagButton.setImageResource(currentFlagResId)

        flagButton.setOnClickListener {
            showCountryPicker()
        }

        fetchNews(currentCountry, null)
    }

    private fun showCountryPicker() {
        val dialogView = layoutInflater.inflate(R.layout.dialog_country_picker, null)
        val recyclerView = dialogView.findViewById<RecyclerView>(R.id.country_recycler)

        val dialog = AlertDialog.Builder(this)
            .setView(dialogView)
            .create()

        recyclerView.layoutManager = LinearLayoutManager(this)
        recyclerView.adapter = CountryAdapter(countries) { selectedCountry ->
            currentCountry = selectedCountry.code
            currentFlagResId = selectedCountry.flagResId
            flagButton.setImageResource(currentFlagResId)
            fetchNews(selectedCountry.code, selectedCountry.searchQuery)
            dialog.dismiss()
        }

        dialog.show()
    }

    private fun fetchNews(country: String, searchQuery: String? = null) {
        RetrofitClient.instance.getTopHeadlines(
            category = "general",
            lang = "en",
            country = country,
            query = searchQuery,
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
                        val countryName = countries.find { it.code == country }?.name ?: "Unknown"
                        Toast.makeText(
                            this@MainActivity,
                            "Loaded ${list.size} articles from $countryName",
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