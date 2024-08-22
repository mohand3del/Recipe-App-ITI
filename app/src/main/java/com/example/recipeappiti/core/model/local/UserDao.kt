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

    @Query("SELECT cuisine FROM user WHERE email = :email")
    suspend fun getCuisine(email: String) : String?
}