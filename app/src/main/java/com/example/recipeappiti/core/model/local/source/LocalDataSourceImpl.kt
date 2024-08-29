package com.example.recipeappiti.core.model.local.source

import com.example.recipeappiti.core.model.local.User

class LocalDataSourceImpl(private val userDao: UserDao) : LocalDataSource {
    override suspend fun addUserToDB(user: User) {
        userDao.addUser(user)
    }

    override suspend fun getLoggedInUser(): User? {
        return userDao.getLoggedInUser()
    }

    override suspend fun deleteLoggedInUser() {
        userDao.deleteLoggedInUser()
    }

    override suspend fun getPassword(email: String): String? {
        return userDao.getPassword(email)
    }

    override suspend fun findLoggedInUser(): Boolean {
        return userDao.findLoggedInUser()
    }

    override suspend fun logInUser(email: String) {
        userDao.logInUser(email)
    }

    override suspend fun logOutUser() {
        userDao.logOutUser()
    }

    override suspend fun getLoggedInEmail(): String {
        return userDao.getLoggedInEmail()
    }

    override suspend fun getLoggedInUsername(): String {
        return userDao.getLoggedInUsername()
    }

    override suspend fun getUserCuisines(): List<String> {
        return userDao.getLoggedInUserCuisines()
    }

    override suspend fun updateUserCuisines(cuisines: List<String>) {
        userDao.updateLoggedInUserCuisines(cuisines)
    }

    override suspend fun updateUserFavourites(favourites: List<String>) {
        userDao.updateLoggedInUserFavourites(favourites)
    }

    override suspend fun updateSubscriptionState() {
        userDao.updateSubscriptionState()
    }

    override suspend fun checkSubscriptionState(): Boolean {
        return userDao.checkSubscriptionState()
    }
}