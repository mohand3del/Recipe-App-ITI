package com.example.recipeappiti.auth.model.data

import com.example.recipeappiti.core.model.local.User

interface LocalDataSource {
    suspend fun getUserFromDB(email: String): User?

    suspend fun addUserToDB(user: User)

    suspend fun getUserCuisines(): List<String>?

    suspend fun updateUserCuisines(cuisines: List<String>)

    suspend fun updateUserLogInStatus(isLoggedIn: Boolean, email: String)

    suspend fun getLoggedInUser(): User?
}