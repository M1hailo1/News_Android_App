package com.example.mihailoprojekat

import android.content.Intent
import android.net.Uri
import android.os.Bundle
import android.widget.Button
import android.widget.ImageView
import android.widget.TextView
import androidx.appcompat.app.AppCompatActivity
import com.bumptech.glide.Glide

class ArticleDetailsActivity : AppCompatActivity() {

    lateinit var articleImage: ImageView
    lateinit var articleTitle: TextView
    lateinit var articleSource: TextView
    lateinit var articleDate: TextView
    lateinit var articleDescription: TextView
    lateinit var articleContent: TextView
    lateinit var readFullButton: Button

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_article_details)

        articleImage = findViewById(R.id.article_image)
        articleTitle = findViewById(R.id.article_title)
        articleSource = findViewById(R.id.article_source)
        articleDate = findViewById(R.id.article_date)
        articleDescription = findViewById(R.id.article_description)
        articleContent = findViewById(R.id.article_content)
        readFullButton = findViewById(R.id.read_full_article_button)

        val title = intent.getStringExtra("title")
        val description = intent.getStringExtra("description")
        val content = intent.getStringExtra("content")
        val imageUrl = intent.getStringExtra("image")
        val sourceName = intent.getStringExtra("source")
        val publishedAt = intent.getStringExtra("publishedAt")
        val articleUrl = intent.getStringExtra("url")

        articleTitle.text = title
        articleDescription.text = description ?: "No description available"
        articleContent.text = content ?: "No content available"
        articleSource.text = "Source: $sourceName"
        articleDate.text = publishedAt

        Glide.with(this)
            .load(imageUrl)
            .placeholder(R.drawable.ic_launcher_foreground)
            .error(R.drawable.ic_launcher_foreground)
            .into(articleImage)

        readFullButton.setOnClickListener {
            val intent = Intent(Intent.ACTION_VIEW, Uri.parse(articleUrl))
            startActivity(intent)
        }
    }
}