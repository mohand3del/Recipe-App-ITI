package com.example.recipeappiti.home.viewModel

import androidx.lifecycle.ViewModel
import androidx.lifecycle.ViewModelProvider
import com.example.recipeappiti.auth.repository.UserRepository
import com.example.recipeappiti.home.repository.MealRepository

class HomeFragmentViewModelFactory(
    private val mealRepository: MealRepository,
    private val userRepository: UserRepository
) : ViewModelProvider.Factory {

    override fun <T : ViewModel> create(modelClass: Class<T>): T {

        if (modelClass.isAssignableFrom(HomeFragmentViewModel::class.java))

            return HomeFragmentViewModel(mealRepository, userRepository) as T
        else
            throw IllegalArgumentException("Unknown view model")

    }
}