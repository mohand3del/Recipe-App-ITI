package com.example.recipeappiti.auth.model

import androidx.lifecycle.LiveData

class UserRepository(private val userDao: UserDao) {
    suspend fun getUser(email: String, password: String): User? {
        val user = userDao.getUser(email, password)
        return user
    }

    suspend fun addUser(user: User) {
        userDao.addUser(user)
    }
}