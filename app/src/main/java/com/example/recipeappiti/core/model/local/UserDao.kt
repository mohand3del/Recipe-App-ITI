package com.example.recipeappiti.core.model.local

import androidx.room.Dao
import androidx.room.Insert
import androidx.room.OnConflictStrategy
import androidx.room.Query

@Dao
interface UserDao {
    @Insert(onConflict = OnConflictStrategy.IGNORE)
    suspend fun addUser(user: User)

    @Query("SELECT * FROM user WHERE isLoggedIn = 1 LIMIT 1")
    suspend fun getLoggedInUser(): User?

    @Query("DELETE FROM user WHERE isLoggedIn = 1")
    suspend fun deleteLoggedInUser()

    @Query("SELECT password FROM user WHERE email = :email LIMIT 1")
    suspend fun getPassword(email: String): String?

    @Query("SELECT isLoggedIn FROM user WHERE isLoggedIn = 1 LIMIT 1")
    suspend fun findLoggedInUser(): Boolean

    @Query("UPDATE user SET isLoggedIn = 1 WHERE email = :email")
    suspend fun logInUser(email: String)

    @Query("UPDATE user SET isLoggedIn = 0 WHERE isLoggedIn = 1")
    suspend fun logOutUser()

    @Query("SELECT email FROM user WHERE isLoggedIn = 1 LIMIT 1")
    suspend fun getLoggedInEmail(): String

    @Query("SELECT username FROM user WHERE isLoggedIn = 1 LIMIT 1")
    suspend fun getLoggedInUsername(): String

    @Query("SELECT cuisines FROM user WHERE isLoggedIn = 1")
    suspend fun getLoggedInUserCuisines(): List<String>

    @Query("UPDATE user SET cuisines = :cuisines WHERE isLoggedIn = 1")
    suspend fun updateLoggedInUserCuisines(cuisines: List<String>)

    @Query("UPDATE user SET favourites = :favourites WHERE isLoggedIn = 1")
    suspend fun updateLoggedInUserFavourites(favourites: List<String>)

    @Query("UPDATE user SET isSubscribed = 1 WHERE isLoggedIn = 1")
    suspend fun updateSubscriptionState()

    @Query("SELECT isSubscribed FROM user WHERE isLoggedIn = 1")
    suspend fun checkSubscriptionState(): Boolean
}