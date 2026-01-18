package com.example.mihailoprojekat.modules

data class Country(
    val name: String,
    val code: String,
    val flagResId: Int,
    val searchQuery: String? = null
)