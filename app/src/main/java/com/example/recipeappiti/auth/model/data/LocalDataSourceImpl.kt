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
}