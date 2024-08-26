package com.example.recipeappiti.home.data.remote

import com.example.recipeappiti.home.model.GsonDataArea
import com.example.recipeappiti.home.model.GsonDataCategories
import com.example.recipeappiti.home.model.GsonDataMeal
import com.example.recipeappiti.home.model.Meal

interface RemoteGsonData {

    suspend fun getRemoteGsonDataCategories(): GsonDataCategories

    suspend fun getRemoteGsonDataMeal(): GsonDataMeal

    suspend fun getCuisinesMeals(area: String): GsonDataMeal

    suspend fun getCuisines(): GsonDataArea

    suspend fun getMealsBySearch(title: String): GsonDataMeal

    suspend fun getCategoryMeals(category: String): GsonDataMeal

    suspend fun getMealById(id: String): Meal
}