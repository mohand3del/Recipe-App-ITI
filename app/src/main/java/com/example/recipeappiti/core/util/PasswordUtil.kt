package com.example.recipeappiti.core.util

import org.mindrot.jbcrypt.BCrypt

object PasswordUtil {
    fun hashPassword(password: String): String {
        return BCrypt.hashpw(password, BCrypt.gensalt())
    }

    fun checkPassword(password: String, hashed: String): Boolean {
        return BCrypt.checkpw(password, hashed)
    }
}