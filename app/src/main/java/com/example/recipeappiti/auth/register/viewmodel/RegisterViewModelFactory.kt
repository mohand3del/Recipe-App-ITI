package com.example.recipeappiti.auth.register.viewmodel

import androidx.lifecycle.ViewModel
import androidx.lifecycle.ViewModelProvider
import com.example.recipeappiti.auth.repository.UserRepository
import com.example.recipeappiti.core.repository.Repository

class RegisterViewModelFactory(
    private val userRepository: UserRepository,
    private val recipeRepository: Repository
): ViewModelProvider.Factory {
    override fun <T : ViewModel> create(modelClass: Class<T>): T {
        if (modelClass.isAssignableFrom(RegisterViewModel::class.java)) {
            return RegisterViewModel(userRepository, recipeRepository) as T
        } else {
            throw IllegalArgumentException("Unknown ViewModel class")
        }
    }
}