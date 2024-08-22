package com.example.recipeappiti.core.model.local

import androidx.room.Entity
import androidx.room.PrimaryKey

@Entity(tableName = "user")
data class User(
    @PrimaryKey(autoGenerate = true)
    val id: Int = 0,
    val username: String,
    val email: String,
    val password: String,
    val cuisine: String?,
    val isLoggedIn : Boolean = false
)
