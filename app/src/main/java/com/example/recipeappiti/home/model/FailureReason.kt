package com.example.recipeappiti.home.model

sealed class FailureReason {
    data object NoInternet : FailureReason()
    class UnknownError(val error: String) : FailureReason()
}
