package com.example.recipeappiti.layout.home.data.remote

import com.example.recipeappiti.core.model.remote.GsonApi

class RemoteGsonDataImpl : RemoteGsonData {

    override suspend fun getRemoteGsonDataCategories() = GsonApi.service.getAllCategories()
    override suspend fun getRemoteGsonDataMeal() = GsonApi.service.getRandomMeal()
    override suspend fun getFilteredMealsByAreas(area: String) = GsonApi.service.getFilteredMealsByAreas(area)
    override suspend fun getAreasOfMeals() = GsonApi.service.getAreasOfMeals()

}