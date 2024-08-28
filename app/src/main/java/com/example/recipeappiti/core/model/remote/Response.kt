package com.example.recipeappiti.core.model.remote

sealed class Response<out T> {
    data object Loading : Response<Nothing>()
    data class Success<out T>(val data: T) : Response<T>()
    data class Failure(val reason: FailureReason) : Response<Nothing>()
}

