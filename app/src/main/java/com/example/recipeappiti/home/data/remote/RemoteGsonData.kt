package com.example.recipeappiti.home.data.remote

import com.example.recipeappiti.home.model.GsonDataArea
import com.example.recipeappiti.home.model.GsonDataCategories
import com.example.recipeappiti.home.model.GsonDataMeal

interface RemoteGsonData {

    suspend fun getRemoteGsonDataCategories() : GsonDataCategories

    suspend fun getRemoteGsonDataMeal() : GsonDataMeal

    suspend fun getFilteredMealsByAreas(area : String) : GsonDataMeal

    suspend fun getAreasOfMeals() : GsonDataArea

}