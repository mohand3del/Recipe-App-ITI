package com.example.recipeappiti.core.model.remote

sealed class FailureReason {
    data object NoInternet : FailureReason()
    class UnknownError(val error: String) : FailureReason()
}
