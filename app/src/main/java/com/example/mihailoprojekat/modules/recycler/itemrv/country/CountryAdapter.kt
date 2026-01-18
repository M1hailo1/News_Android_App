package com.example.mihailoprojekat.modules.recycler.country

import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.ImageView
import android.widget.TextView
import androidx.recyclerview.widget.RecyclerView
import com.example.mihailoprojekat.R
import com.example.mihailoprojekat.modules.Country

class CountryAdapter(
    private val countries: List<Country>,
    private val onCountryClick: (Country) -> Unit
) : RecyclerView.Adapter<CountryAdapter.CountryViewHolder>() {

    class CountryViewHolder(view: View) : RecyclerView.ViewHolder(view) {
        val flag: ImageView = view.findViewById(R.id.country_flag)
        val name: TextView = view.findViewById(R.id.country_name)
    }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): CountryViewHolder {
        val view = LayoutInflater.from(parent.context)
            .inflate(R.layout.item_country, parent, false)
        return CountryViewHolder(view)
    }

    override fun onBindViewHolder(holder: CountryViewHolder, position: Int) {
        val country = countries[position]
        holder.flag.setImageResource(country.flagResId)
        holder.name.text = country.name

        holder.itemView.setOnClickListener {
            onCountryClick(country)
        }
    }

    override fun getItemCount() = countries.size
}