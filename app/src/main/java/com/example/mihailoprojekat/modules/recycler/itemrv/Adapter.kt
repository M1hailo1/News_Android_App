package com.example.mihailoprojekat.modules.recycler.itemrv

import android.content.Intent
import android.view.LayoutInflater
import android.view.ViewGroup
import android.widget.Toast
import androidx.core.content.ContextCompat
import androidx.recyclerview.widget.RecyclerView
import com.example.mihailoprojekat.ArticleDetailsActivity
import com.example.mihailoprojekat.BookmarkManager
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
        val article = itemList[position]
        holder.bind(article)

        BookmarkManager.isBookmarked(article.url) { isBookmarked ->
            if (isBookmarked) {
                holder.bookmarkButton.setImageResource(android.R.drawable.btn_star_big_on)
                holder.bookmarkButton.setColorFilter(
                    ContextCompat.getColor(holder.itemView.context, R.color.accent)
                )
            } else {
                holder.bookmarkButton.setImageResource(android.R.drawable.btn_star_big_off)
                holder.bookmarkButton.setColorFilter(
                    ContextCompat.getColor(holder.itemView.context, R.color.text_secondary)
                )
            }
        }

        holder.bookmarkButton.setOnClickListener {
            BookmarkManager.isBookmarked(article.url) { isBookmarked ->
                if (isBookmarked) {
                    BookmarkManager.removeBookmark(article.url) { success ->
                        if (success) {
                            holder.bookmarkButton.setImageResource(android.R.drawable.btn_star_big_off)
                            holder.bookmarkButton.setColorFilter(
                                ContextCompat.getColor(holder.itemView.context, R.color.text_secondary)
                            )
                            Toast.makeText(holder.itemView.context, "Bookmark removed", Toast.LENGTH_SHORT).show()
                        }
                    }
                } else {
                    BookmarkManager.saveBookmark(article) { success ->
                        if (success) {
                            holder.bookmarkButton.setImageResource(android.R.drawable.btn_star_big_on)
                            holder.bookmarkButton.setColorFilter(
                                ContextCompat.getColor(holder.itemView.context, R.color.accent)
                            )
                            Toast.makeText(holder.itemView.context, "Bookmark saved", Toast.LENGTH_SHORT).show()
                        }
                    }
                }
            }
        }

        holder.itemView.setOnClickListener {
            val context = holder.itemView.context
            val intent = Intent(context, ArticleDetailsActivity::class.java)

            intent.putExtra("title", article.title)
            intent.putExtra("description", article.description)
            intent.putExtra("content", article.content)
            intent.putExtra("image", article.image)
            intent.putExtra("source", article.source.name)
            intent.putExtra("publishedAt", article.publishedAt)
            intent.putExtra("url", article.url)

            context.startActivity(intent)
        }
    }

    override fun getItemCount(): Int = itemList.size
}