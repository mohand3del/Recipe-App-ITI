package com.example.recipeappiti.home.repository

import com.example.recipeappiti.home.model.GsonDataArea
import com.example.recipeappiti.home.model.GsonDataCategories
import com.example.recipeappiti.home.model.GsonDataMeal


interface MealRepository {

    suspend fun getCategories(): GsonDataCategories

    suspend fun getRandomDataMeal(): GsonDataMeal

    suspend fun getFilteredMealsByAreas(area: String): GsonDataMeal

    suspend fun getAreasOfMeals(): GsonDataArea

}