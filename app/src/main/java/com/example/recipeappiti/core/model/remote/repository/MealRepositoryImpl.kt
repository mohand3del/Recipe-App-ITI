package com.example.recipeappiti.core.model.remote.repository

import com.example.recipeappiti.core.model.remote.source.RemoteGsonData


class MealRepositoryImpl(

    private val remoteGsonDataSource: RemoteGsonData

) : MealRepository {


    override suspend fun getCategories() = remoteGsonDataSource.getRemoteGsonDataCategories()

    override suspend fun getRandomDataMeal() = remoteGsonDataSource.getRemoteGsonDataMeal()

    override suspend fun getCuisinesMeals(area: String) =
        remoteGsonDataSource.getCuisinesMeals(area)

    override suspend fun getCuisines() = remoteGsonDataSource.getCuisines()

    override suspend fun getMealsBySearch(title: String) =
        remoteGsonDataSource.getMealsBySearch(title)

    override suspend fun getCategoryMeals(category: String) =
        remoteGsonDataSource.getCategoryMeals(category)

    override suspend fun getMealById(id: String) = remoteGsonDataSource.getMealById(id)

}