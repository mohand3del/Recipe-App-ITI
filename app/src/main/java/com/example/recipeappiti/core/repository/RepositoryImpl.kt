package com.example.recipeappiti.core.repository

import com.example.recipeappiti.layout.home.data.remote.RemoteGsonData
import com.example.recipeappiti.layout.home.model.GsonDataArea


class RepositoryImpl(

    private val remoteGsonDataSource: RemoteGsonData

) : Repository {


    override suspend fun getCategories() = remoteGsonDataSource.getRemoteGsonDataCategories()

    override suspend fun getRandomDataMeal() = remoteGsonDataSource.getRemoteGsonDataMeal()

    override suspend fun getFilteredMealsByAreas(area: String) =
        remoteGsonDataSource.getFilteredMealsByAreas(area)

    override suspend fun getAreasOfMeals() = remoteGsonDataSource.getAreasOfMeals()

}