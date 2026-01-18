package com.example.mihailoprojekat.services

import com.example.mihailoprojekat.modules.Response
import retrofit2.Call
import retrofit2.http.GET
import retrofit2.http.Query

interface ApiService {
    @GET("v4/top-headlines")
    fun getTopHeadlines(
        @Query("category") category: String = "general",
        @Query("lang") lang: String = "en",
        @Query("country") country: String,
        @Query("q") query: String? = null,
        @Query("max") max: Int = 10,
        @Query("apikey") apiKey: String
    ): Call<Response>
}