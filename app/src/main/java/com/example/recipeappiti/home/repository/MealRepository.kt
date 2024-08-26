package com.example.recipeappiti.home.repository

import com.example.recipeappiti.home.model.GsonDataArea
import com.example.recipeappiti.home.model.GsonDataCategories
import com.example.recipeappiti.home.model.GsonDataMeal
import com.example.recipeappiti.home.model.Meal


interface MealRepository {

    suspend fun getCategories(): GsonDataCategories

    suspend fun getRandomDataMeal(): GsonDataMeal

    suspend fun getCuisinesMeals(area: String): GsonDataMeal

    suspend fun getCuisines(): GsonDataArea

    suspend fun getMealsBySearch(title: String) : GsonDataMeal

    suspend fun getCategoryMeals(category: String) : GsonDataMeal

    suspend fun getMealById(id : String) : Meal

}