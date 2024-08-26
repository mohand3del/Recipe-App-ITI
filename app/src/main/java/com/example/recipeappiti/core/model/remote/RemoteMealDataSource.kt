package com.example.recipeappiti.core.model.remote

import com.example.recipeappiti.home.model.GsonDataArea
import com.example.recipeappiti.home.model.GsonDataCategories
import com.example.recipeappiti.home.model.GsonDataMeal
import retrofit2.http.GET
import retrofit2.http.Query

interface RemoteMealDataSource {

    @GET("categories.php")
    suspend fun getAllCategories(): GsonDataCategories

    @GET("random.php")
    suspend fun getRandomMeal(): GsonDataMeal

    @GET("filter.php")
    suspend fun getCuisinesMeals(@Query("a") area: String): GsonDataMeal

    @GET("search.php")
    suspend fun getMealsBySearch(@Query("s") title: String): GsonDataMeal

    @GET("list.php?a=list")
    suspend fun getCuisines(): GsonDataArea

    @GET("filter.php")
    suspend fun getCategoryMeals(@Query("c") category: String): GsonDataMeal

    @GET("lookup.php")
    suspend fun getMealById(@Query("i") id: String): GsonDataMeal

}