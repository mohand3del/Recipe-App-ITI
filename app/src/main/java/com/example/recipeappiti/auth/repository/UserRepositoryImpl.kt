package com.example.recipeappiti.auth.repository

import com.example.recipeappiti.auth.model.data.LocalDataSource
import com.example.recipeappiti.core.model.local.User

class UserRepositoryImpl(private val userDataSource: LocalDataSource): UserRepository {
    override suspend fun getUser(email: String): User? {
        return userDataSource.getUserFromDB(email)
    }

    override suspend fun addUser(user: User) {
        userDataSource.addUserToDB(user)
    }

    override suspend fun getPassword(email: String): String? {
        return userDataSource.getPassword(email)
    }

    override suspend fun getLoggedInUser(): User? {
        return userDataSource.getLoggedInUser()
    }

    override suspend fun getCuisines():List<String>? {
        return userDataSource.getUserCuisines()
    }

    override suspend fun updateCuisines(cuisines: List<String>) {
        userDataSource.updateUserCuisines(cuisines)
    }

    override suspend fun updateLogInStatus(isLoggedIn: Boolean, email: String) {
        userDataSource.updateUserLogInStatus(isLoggedIn, email)
    }
}