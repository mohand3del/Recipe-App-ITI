package com.example.recipeappiti.auth.model

sealed class ValidateCredentials {
    data object Valid: ValidateCredentials()
    data class InValid(val message: String): ValidateCredentials()
}