package com.example.mihailoprojekat.modules.recycler.itemrv

import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.recyclerview.widget.RecyclerView
import com.example.mihailoprojekat.R
import com.example.mihailoprojekat.modules.Info

class Adapter(
    private val itemList: List<Info>
) : RecyclerView.Adapter<ViewHolder>() {

    override fun onCreateViewHolder(
        parent: ViewGroup,
        viewType: Int
    ): ViewHolder {
        val view = LayoutInflater.from(parent.context)
            .inflate(R.layout.item_fact, parent, false)
        return ViewHolder(view)
    }

    override fun onBindViewHolder(
        holder: ViewHolder,
        position: Int
    ) {
        holder.bind(itemList[position])
    }

    override fun getItemCount(): Int = itemList.size
}