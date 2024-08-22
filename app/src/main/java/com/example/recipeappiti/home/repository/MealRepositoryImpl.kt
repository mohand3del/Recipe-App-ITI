package com.example.recipeappiti.home.repository

import com.example.recipeappiti.home.data.remote.RemoteGsonData


class MealRepositoryImpl(

    private val remoteGsonDataSource: RemoteGsonData

) : MealRepository {


    override suspend fun getCategories() = remoteGsonDataSource.getRemoteGsonDataCategories()

    override suspend fun getRandomDataMeal() = remoteGsonDataSource.getRemoteGsonDataMeal()

    override suspend fun getFilteredMealsByAreas(area: String) =
        remoteGsonDataSource.getFilteredMealsByAreas(area)

    override suspend fun getAreasOfMeals() = remoteGsonDataSource.getAreasOfMeals()

}