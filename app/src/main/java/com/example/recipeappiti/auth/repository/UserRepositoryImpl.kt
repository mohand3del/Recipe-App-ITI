package com.example.recipeappiti.auth.repository

import com.example.recipeappiti.auth.model.data.LocalDataSource
import com.example.recipeappiti.core.model.local.User

class UserRepositoryImpl(private val userDataSource: LocalDataSource): UserRepository {
    override suspend fun getUser(email: String): User? {
        val user = userDataSource.getUserFromDB(email)
        return user
    }

    override suspend fun addUser(user: User) {
        userDataSource.addUserToDB(user)
    }

    override suspend fun getCuisine(email: String) = userDataSource.getCuisine(email)
}