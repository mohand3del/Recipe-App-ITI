package com.example.recipeappiti.auth.repository

import com.example.recipeappiti.auth.model.data.LocalDataSource
import com.example.recipeappiti.core.model.local.User

class UserRepositoryImpl(private val userDataSource: LocalDataSource): UserRepository {
    override suspend fun addUser(user: User) {
        userDataSource.addUserToDB(user)
    }

    override suspend fun getLoggedInUser(): User? {
        return userDataSource.getLoggedInUser()
    }

    override suspend fun deleteLoggedInUser() {
        userDataSource.deleteLoggedInUser()
    }

    override suspend fun getPassword(email: String): String? {
        return userDataSource.getPassword(email)
    }

    override suspend fun findLoggedInUser(): Boolean {
        return userDataSource.findLoggedInUser()
    }

    override suspend fun logInUser(email: String) {
        userDataSource.logInUser(email)
    }

    override suspend fun logOutUser() {
        userDataSource.logOutUser()
    }

    override suspend fun getLoggedInEmail(): String {
        return userDataSource.getLoggedInEmail()
    }

    override suspend fun getLoggedInUsername(): String {
        return userDataSource.getLoggedInUsername()
    }

    override suspend fun getCuisines():List<String> {
        return userDataSource.getUserCuisines()
    }

    override suspend fun updateCuisines(cuisines: List<String>) {
        userDataSource.updateUserCuisines(cuisines)
    }
}