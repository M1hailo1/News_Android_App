package com.example.mihailoprojekat.modules.recycler.itemrv

import android.view.View
import android.widget.ImageView
import android.widget.TextView
import androidx.recyclerview.widget.RecyclerView
import com.bumptech.glide.Glide
import com.example.mihailoprojekat.R
import com.example.mihailoprojekat.modules.Info

class ViewHolder(view: View): RecyclerView.ViewHolder(view) {
    val titleText: TextView = itemView.findViewById(R.id.title_text)
    val descriptionText: TextView = itemView.findViewById(R.id.description_text)
    val newsImage: ImageView = itemView.findViewById(R.id.urlToImage_Image)

    fun bind(info: Info) {
        titleText.text = info.title
        descriptionText.text = info.description ?: "No description available"

        Glide.with(itemView.context)
            .load(info.image)
            .placeholder(R.drawable.ic_launcher_foreground) // Placeholder while loading
            .error(R.drawable.ic_launcher_foreground) // Show if image fails to load
            .into(newsImage)
    }
}