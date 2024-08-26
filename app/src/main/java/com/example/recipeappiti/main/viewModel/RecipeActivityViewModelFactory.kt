package com.example.recipeappiti.main.viewModel

import androidx.lifecycle.ViewModel
import androidx.lifecycle.ViewModelProvider
import com.example.recipeappiti.auth.repository.UserRepository

class RecipeActivityViewModelFactory(private val userRepository: UserRepository) :
    ViewModelProvider.Factory {

    override fun <T : ViewModel> create(modelClass: Class<T>): T {
        return if (modelClass.isAssignableFrom(RecipeActivityViewModel::class.java)) RecipeActivityViewModel(
            userRepository
        ) as T
        else
            throw IllegalArgumentException("unknown view model")
    }

}