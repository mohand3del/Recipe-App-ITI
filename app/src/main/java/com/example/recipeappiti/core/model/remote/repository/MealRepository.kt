package com.example.recipeappiti.core.model.remote.repository

import com.example.recipeappiti.core.model.remote.GsonDataArea
import com.example.recipeappiti.core.model.remote.GsonDataCategories
import com.example.recipeappiti.core.model.remote.GsonDataMeal
import com.example.recipeappiti.core.model.remote.Meal


interface MealRepository {

    suspend fun getCategories(): GsonDataCategories

    suspend fun getRandomDataMeal(): GsonDataMeal

    suspend fun getCuisinesMeals(area: String): GsonDataMeal

    suspend fun getCuisines(): GsonDataArea

    suspend fun getMealsBySearch(title: String) : GsonDataMeal

    suspend fun getCategoryMeals(category: String) : GsonDataMeal

    suspend fun getMealById(id : String) : Meal

}