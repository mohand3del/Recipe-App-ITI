package com.example.recipeappiti.core.viewmodel

import androidx.lifecycle.ViewModel
import androidx.lifecycle.ViewModelProvider
import com.example.recipeappiti.core.model.local.repository.UserRepository
import com.example.recipeappiti.core.model.remote.repository.MealRepository

class DataViewModelFactory(
    private val userRepository: UserRepository,
    private val mealRepository: MealRepository
) : ViewModelProvider.Factory {
    override fun <T : ViewModel> create(modelClass: Class<T>): T {
        if (modelClass.isAssignableFrom(DataViewModel::class.java)) {
            @Suppress("UNCHECKED_CAST")
            return DataViewModel(userRepository, mealRepository) as T
        }
        throw IllegalArgumentException("Unknown ViewModel class")
    }
}
