package com.example.recipeappiti.core.model.remote.source

import com.google.gson.Gson
import com.google.gson.GsonBuilder
import okhttp3.OkHttpClient
import retrofit2.Retrofit
import retrofit2.converter.gson.GsonConverterFactory

object GsonServiceHelper {
    private val gson: Gson = GsonBuilder().serializeNulls().create()

    val retrofit: Retrofit = Retrofit.Builder()
        .baseUrl("https://www.themealdb.com/api/json/v1/1/") // Ensure this URL is correct
        .client(OkHttpClient())
        .addConverterFactory(GsonConverterFactory.create(gson))
        .build()
}
