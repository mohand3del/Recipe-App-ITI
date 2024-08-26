package com.example.recipeappiti.home.data.remote

import com.example.recipeappiti.core.model.remote.GsonApi
import com.example.recipeappiti.home.model.Meal

class RemoteGsonDataImpl : RemoteGsonData {

    override suspend fun getRemoteGsonDataCategories() = GsonApi.service.getAllCategories()
    override suspend fun getRemoteGsonDataMeal() = GsonApi.service.getRandomMeal()
    override suspend fun getCuisinesMeals(area: String) = GsonApi.service.getCuisinesMeals(area)
    override suspend fun getCuisines() = GsonApi.service.getCuisines()
    override suspend fun getMealsBySearch(title: String) = GsonApi.service.getMealsBySearch(title)
    override suspend fun getCategoryMeals(category: String) =
        GsonApi.service.getCategoryMeals(category)

    override suspend fun getMealById(id: String): Meal = GsonApi.service.getMealById(id).meals[0]

}