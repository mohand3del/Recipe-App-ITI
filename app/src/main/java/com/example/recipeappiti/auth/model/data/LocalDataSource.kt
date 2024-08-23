package com.example.recipeappiti.auth.model.data

import com.example.recipeappiti.core.model.local.User

interface LocalDataSource {
    suspend fun addUserToDB(user: User)

    suspend fun getLoggedInUser(): User?

    suspend fun deleteLoggedInUser()

    suspend fun getPassword(email: String): String?

    suspend fun findLoggedInUser(): Boolean

    suspend fun logInUser(email: String)

    suspend fun logOutUser()

    suspend fun getLoggedInEmail(): String

    suspend fun getLoggedInUsername(): String

    suspend fun getUserCuisines(): List<String>

    suspend fun updateUserCuisines(cuisines: List<String>)
}