package com.example.recipeappiti.auth.repository

import com.example.recipeappiti.core.model.local.User

interface UserRepository {
    suspend fun getUser(email: String): User?

    suspend fun addUser(user: User)

    suspend fun getPassword(email: String): String?

    suspend fun getLoggedInUser(): User?

    suspend fun getCuisines(): List<String>?

    suspend fun updateCuisines(cuisines: List<String>)

    suspend fun updateLogInStatus(isLoggedIn: Boolean, email: String)
}