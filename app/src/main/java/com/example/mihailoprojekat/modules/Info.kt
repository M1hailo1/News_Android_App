    package com.example.mihailoprojekat.modules


    data class Info (
        val title: String,
        val description: String?,
        val content: String?,
        val url: String,
        val image: String?,  // GNews uses "image" instead of "urlToImage"
        val publishedAt: String,
        val source: Source
    )

    data class Response (
        val totalArticles: Int,
        val articles: List<Info>
    )

    data class Source(
        val name: String,
        val url: String
    )