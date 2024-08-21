package com.example.recipeappiti.auth.repository

import com.example.recipeappiti.core.model.local.User

interface UserRepository {
    suspend fun getUser(email: String): User?

    suspend fun addUser(user: User)
}