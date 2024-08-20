package com.example.recipeappiti.core.repository

import com.example.recipeappiti.layout.home.model.GsonDataArea
import com.example.recipeappiti.layout.home.model.GsonDataCategories
import com.example.recipeappiti.layout.home.model.GsonDataMeal


interface Repository {

    suspend fun getCategories() : GsonDataCategories

    suspend fun getRandomDataMeal() : GsonDataMeal

    suspend fun getFilteredMealsByAreas(area : String) : GsonDataMeal

    suspend fun getAreasOfMeals() : GsonDataArea

}