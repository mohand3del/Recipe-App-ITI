package com.example.recipeappiti.details.viewmodel

import androidx.lifecycle.ViewModel
import androidx.lifecycle.ViewModelProvider
import com.example.recipeappiti.auth.repository.UserRepository
import com.example.recipeappiti.home.repository.MealRepository

class DetailsViewModelFactory(
    private val mealRepository: MealRepository,
    private val userRepository: UserRepository
) : ViewModelProvider.Factory {

    override fun <T : ViewModel> create(modelClass: Class<T>): T {

        if (modelClass.isAssignableFrom(DetailsViewModel::class.java))

            return DetailsViewModel(mealRepository, userRepository) as T
        else
            throw IllegalArgumentException("Unknown view model")

    }
}