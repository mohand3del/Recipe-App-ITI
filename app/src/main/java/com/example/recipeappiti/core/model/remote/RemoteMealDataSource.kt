package com.example.recipeappiti.core.model.remote

import com.example.recipeappiti.layout.home.model.GsonDataArea
import com.example.recipeappiti.layout.home.model.GsonDataCategories
import com.example.recipeappiti.layout.home.model.GsonDataMeal
import retrofit2.http.GET
import retrofit2.http.Query

interface RemoteMealDataSource {

    @GET("categories.php")
    suspend fun getAllCategories() : GsonDataCategories

    @GET("random.php")
    suspend fun getRandomMeal(): GsonDataMeal

    @GET("filter.php")
    suspend fun getFilteredMealsByAreas(@Query("a") area: String) : GsonDataMeal

    @GET("list.php?a=list")
    suspend fun getAreasOfMeals() : GsonDataArea
}