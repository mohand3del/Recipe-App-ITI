package com.example.recipeappiti.auth.model

sealed class ValidateCredentials {
    data object Valid: ValidateCredentials()
    data class Invalid(val message: String): ValidateCredentials()
}