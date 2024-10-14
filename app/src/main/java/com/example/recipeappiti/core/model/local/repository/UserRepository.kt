package com.example.recipeappiti.core.model.local.repository

import com.example.recipeappiti.core.model.local.User

interface UserRepository {
    suspend fun addUser(user: User)

    suspend fun getLoggedInUser(): User?

    suspend fun deleteLoggedInUser()

    suspend fun getPassword(email: String): String?

    suspend fun findLoggedInUser(): Boolean

    suspend fun logInUser(email: String)

    suspend fun logOutUser()

    suspend fun getLoggedInEmail(): String

    suspend fun getLoggedInUsername(): String

    suspend fun getCuisines(): List<String>

    suspend fun updateCuisines(cuisines: List<String>)

    suspend fun updateFavourites(favourites: List<String>)

    suspend fun updateSubscriptionState(isSubscribed: Boolean)

    suspend fun checkSubscriptionState(): Boolean
}