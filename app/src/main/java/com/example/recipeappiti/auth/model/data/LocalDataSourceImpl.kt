package com.example.recipeappiti.auth.model.data

import com.example.recipeappiti.core.model.local.User
import com.example.recipeappiti.core.model.local.UserDao

class LocalDataSourceImpl(private val userDao: UserDao): LocalDataSource {

    override suspend fun getUserFromDB(email: String): User? {
        return userDao.getUser(email)
    }

    override suspend fun addUserToDB(user: User) {
        userDao.addUser(user)
    }

    override suspend fun getUserCuisines(): List<String>? {
        return userDao.getCuisines()
    }

    override suspend fun updateUserCuisines(cuisines: List<String>) {
        userDao.updateCuisines(cuisines)
    }

    override suspend fun updateUserLogInStatus(isLoggedIn: Boolean, email: String) {
        userDao.updateLogInStatus(email, isLoggedIn)
    }

    override suspend fun getLoggedInUser(): User? {
        return userDao.getLoggedInUser()
    }


}