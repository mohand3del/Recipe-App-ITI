package com.example.recipeappiti.auth.model

sealed class ValidateCredentials {
    object Valid: ValidateCredentials()
    data class InValid(val message: String): ValidateCredentials()
}