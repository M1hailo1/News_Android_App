package com.example.mihailoprojekat.modules.recycler.itemrv

import android.view.View
import android.widget.ImageButton
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
    val bookmarkButton: ImageButton = itemView.findViewById(R.id.bookmark_button)

    fun bind(info: Info) {
        titleText.text = info.title
        descriptionText.text = info.description ?: "No description available"

        Glide.with(itemView.context)
            .load(info.image)
            .placeholder(R.drawable.ic_launcher_foreground)
            .error(R.drawable.ic_launcher_foreground)
            .into(newsImage)
    }
}