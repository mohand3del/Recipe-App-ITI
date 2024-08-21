package com.example.recipeappiti.auth.model.data

import com.example.recipeappiti.core.model.local.User

interface LocalDataSource {
    suspend fun getUserFromDB(email: String): User?

    suspend fun addUserToDB(user: User)
}