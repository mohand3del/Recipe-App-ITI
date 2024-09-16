package com.example.recipeappiti.core.model.local.repository

import com.example.recipeappiti.core.model.local.User
import com.example.recipeappiti.core.model.local.source.LocalDataSource

class UserRepositoryImpl(private val userDataSource: LocalDataSource) : UserRepository {
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

    override suspend fun getCuisines(): List<String> {
        return userDataSource.getUserCuisines()
    }

    override suspend fun updateCuisines(cuisines: List<String>) {
        userDataSource.updateUserCuisines(cuisines)
    }

    override suspend fun updateFavourites(favourites: List<String>) {
        userDataSource.updateUserFavourites(favourites)
    }

    override suspend fun updateSubscriptionState(isSubscribed: Boolean) {
        userDataSource.updateSubscriptionState(isSubscribed)
    }

    override suspend fun checkSubscriptionState(): Boolean {
        return userDataSource.checkSubscriptionState()
    }
}