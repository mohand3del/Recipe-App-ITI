package com.example.recipeappiti.core.model.local

import androidx.room.Dao
import androidx.room.Insert
import androidx.room.OnConflictStrategy
import androidx.room.Query

@Dao
interface UserDao {
    @Insert(onConflict = OnConflictStrategy.IGNORE)
    suspend fun addUser(user: User)

    @Query("SELECT * FROM user WHERE email = :email LIMIT 1")
    suspend fun getUser(email: String): User?

    @Query("SELECT cuisine FROM user WHERE isLoggedIn = 1")
    suspend fun getCuisines(): List<String>?

    @Query("UPDATE user SET cuisine = :cuisine WHERE isLoggedIn = 1")
    suspend fun updateCuisines(cuisine: List<String>)

    @Query("UPDATE user SET isLoggedIn = :isLoggedIn WHERE email = :email")
    suspend fun updateLogInStatus(email: String, isLoggedIn: Boolean)

    @Query("SELECT * FROM user WHERE isLoggedIn = 1 LIMIT 1")
    suspend fun getLoggedInUser(): User?
}