package com.example.recipeappiti.auth.register.viewmodel

import androidx.lifecycle.ViewModel
import androidx.lifecycle.ViewModelProvider
import com.example.recipeappiti.auth.repository.UserRepository
import com.example.recipeappiti.home.repository.MealRepository

class RegisterViewModelFactory(
    private val userRepository: UserRepository,
    private val recipeMealRepository: MealRepository
): ViewModelProvider.Factory {
    override fun <T : ViewModel> create(modelClass: Class<T>): T {
        if (modelClass.isAssignableFrom(RegisterViewModel::class.java)) {
            return RegisterViewModel(userRepository, recipeMealRepository) as T
        } else {
            throw IllegalArgumentException("Unknown ViewModel class")
        }
    }
}