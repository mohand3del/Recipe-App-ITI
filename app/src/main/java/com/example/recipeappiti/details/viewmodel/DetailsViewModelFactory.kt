package com.example.recipeappiti.details.viewmodel

import androidx.lifecycle.ViewModel
import androidx.lifecycle.ViewModelProvider
import com.example.recipeappiti.core.model.local.repository.UserRepository
import com.example.recipeappiti.core.model.remote.repository.MealRepository

class DetailsViewModelFactory(private val mealRepository: MealRepository) : ViewModelProvider.Factory {

    override fun <T : ViewModel> create(modelClass: Class<T>): T {

        if (modelClass.isAssignableFrom(DetailsViewModel::class.java))

            return DetailsViewModel(mealRepository) as T
        else
            throw IllegalArgumentException("Unknown view model")
    }
}