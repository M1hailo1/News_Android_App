package com.example.mihailoprojekat

import android.os.Bundle
import android.os.Handler
import android.os.Looper
import android.util.Log
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.ImageView
import android.widget.Toast
import androidx.appcompat.app.AlertDialog
import androidx.appcompat.widget.SearchView
import androidx.fragment.app.Fragment
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import androidx.swiperefreshlayout.widget.SwipeRefreshLayout
import com.example.mihailoprojekat.modules.Country
import com.example.mihailoprojekat.modules.Info
import com.example.mihailoprojekat.modules.Response
import com.example.mihailoprojekat.modules.recycler.country.CountryAdapter
import com.example.mihailoprojekat.modules.recycler.itemrv.Adapter
import com.example.mihailoprojekat.services.RetrofitClient
import com.google.android.material.chip.ChipGroup
import retrofit2.Call
import retrofit2.Callback

class HomeFragment : Fragment() {

    lateinit var itemRV: RecyclerView
    lateinit var flagButton: ImageView
    lateinit var swipeRefreshLayout: SwipeRefreshLayout
    lateinit var categoryChipGroup: ChipGroup
    lateinit var searchView: SearchView
    var list: MutableList<Info> = mutableListOf()

    private val API_KEY = BuildConfig.GNEWS_API_KEY
    private var currentCountry = "us"
    private var currentFlagResId = R.drawable.earth
    private var currentSearchQuery: String? = null
    private var currentCategory = "general"

    private val searchHandler = Handler(Looper.getMainLooper())
    private var searchRunnable: Runnable? = null

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

    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        val view = inflater.inflate(R.layout.fragment_home, container, false)

        itemRV = view.findViewById(R.id.itemRV)
        flagButton = view.findViewById(R.id.flag_button)
        swipeRefreshLayout = view.findViewById(R.id.swipeRefreshLayout)
        categoryChipGroup = view.findViewById(R.id.categoryChipGroup)
        searchView = view.findViewById(R.id.searchView)

        itemRV.layoutManager = LinearLayoutManager(requireContext())

        flagButton.setImageResource(currentFlagResId)

        flagButton.setOnClickListener {
            showCountryPicker()
        }

        searchView.setOnQueryTextListener(object : SearchView.OnQueryTextListener {
            override fun onQueryTextSubmit(query: String?): Boolean {
                return false
            }

            override fun onQueryTextChange(newText: String?): Boolean {
                searchRunnable?.let { searchHandler.removeCallbacks(it) }

                searchRunnable = Runnable {
                    currentSearchQuery = if (newText.isNullOrBlank()) null else newText
                    fetchNews(currentCountry, currentSearchQuery, currentCategory)
                }

                searchHandler.postDelayed(searchRunnable!!, 800)
                return true
            }
        })

        categoryChipGroup.setOnCheckedStateChangeListener { group, checkedIds ->
            if (checkedIds.isNotEmpty()) {
                val checkedId = checkedIds[0]
                currentCategory = when (checkedId) {
                    R.id.chip_general -> "general"
                    R.id.chip_business -> "business"
                    R.id.chip_entertainment -> "entertainment"
                    R.id.chip_health -> "health"
                    R.id.chip_science -> "science"
                    R.id.chip_sports -> "sports"
                    R.id.chip_technology -> "technology"
                    else -> "general"
                }
                fetchNews(currentCountry, currentSearchQuery, currentCategory)
            }
        }

        swipeRefreshLayout.setOnRefreshListener {
            shuffleAndRefresh()
        }

        swipeRefreshLayout.setColorSchemeResources(
            android.R.color.holo_blue_bright,
            android.R.color.holo_green_light,
            android.R.color.holo_orange_light,
            android.R.color.holo_red_light
        )

        fetchNews(currentCountry, currentSearchQuery, currentCategory)

        return view
    }

    private fun showCountryPicker() {
        val dialogView = layoutInflater.inflate(R.layout.dialog_country_picker, null)
        val recyclerView = dialogView.findViewById<RecyclerView>(R.id.country_recycler)

        val dialog = AlertDialog.Builder(requireContext())
            .setView(dialogView)
            .create()

        recyclerView.layoutManager = LinearLayoutManager(requireContext())
        recyclerView.adapter = CountryAdapter(countries) { selectedCountry ->
            currentCountry = selectedCountry.code
            currentFlagResId = selectedCountry.flagResId
            flagButton.setImageResource(currentFlagResId)
            fetchNews(currentCountry, currentSearchQuery, currentCategory)
            dialog.dismiss()
        }

        dialog.show()
    }

    private fun fetchNews(country: String, searchQuery: String? = null, category: String = "general") {
        swipeRefreshLayout.isRefreshing = true

        RetrofitClient.instance.getTopHeadlines(
            category = category,
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
                swipeRefreshLayout.isRefreshing = false

                if (response.isSuccessful) {
                    response.body()?.let { newsResponse ->
                        list = newsResponse.articles.toMutableList()
                        setupRV(list)

                        val searchText = if (searchQuery != null) " matching '$searchQuery'" else ""
                        val countryName = countries.find { it.code == country }?.name ?: "Unknown"
                        Toast.makeText(
                            requireContext(),
                            "Loaded ${list.size} $category articles$searchText from $countryName",
                            Toast.LENGTH_SHORT
                        ).show()
                    }
                } else {
                    Toast.makeText(
                        requireContext(),
                        "Error: ${response.code()}",
                        Toast.LENGTH_SHORT
                    ).show()
                    Log.e("HomeFragment", "Error: ${response.errorBody()?.string()}")
                }
            }

            override fun onFailure(call: Call<Response>, t: Throwable) {
                swipeRefreshLayout.isRefreshing = false

                Toast.makeText(
                    requireContext(),
                    "Failed to fetch news: ${t.message}",
                    Toast.LENGTH_SHORT
                ).show()
                Log.e("HomeFragment", "onFailure: ${t.message}", t)
            }
        })
    }

    private fun shuffleAndRefresh() {
        if (list.isNotEmpty()) {
            list.shuffle()
            setupRV(list)
            Toast.makeText(requireContext(), "Articles shuffled!", Toast.LENGTH_SHORT).show()
        }
        swipeRefreshLayout.isRefreshing = false
    }

    fun setupRV(articles: List<Info>) {
        itemRV.adapter = Adapter(articles)
    }
}