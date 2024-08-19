package com.example.recipeappiti.auth.model

import androidx.lifecycle.LiveData

class UserRepository(private val userDao: UserDao) {
    fun getUser(email: String, password: String): LiveData<User?> {
        val user = userDao.getUser(email, password)
        return user
    }

    suspend fun addUser(user: User) {
        userDao.addUser(user)
    }
}