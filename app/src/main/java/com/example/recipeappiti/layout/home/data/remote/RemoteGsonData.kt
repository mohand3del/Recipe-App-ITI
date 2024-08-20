package com.example.recipeappiti.layout.home.data.remote

import com.example.recipeappiti.layout.home.model.GsonDataArea
import com.example.recipeappiti.layout.home.model.GsonDataCategories
import com.example.recipeappiti.layout.home.model.GsonDataMeal

interface RemoteGsonData {

    suspend fun getRemoteGsonDataCategories() : GsonDataCategories

    suspend fun getRemoteGsonDataMeal() : GsonDataMeal

    suspend fun getFilteredMealsByAreas(area : String) : GsonDataMeal

    suspend fun getAreasOfMeals() : GsonDataArea

}