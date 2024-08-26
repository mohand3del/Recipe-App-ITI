package com.example.recipeappiti.splash.viewModel

import androidx.lifecycle.ViewModel
import androidx.lifecycle.ViewModelProvider
import com.example.recipeappiti.auth.repository.UserRepository

class SplashViewModelFactory(

    private val userRepository: UserRepository

) : ViewModelProvider.Factory {

    override fun <T : ViewModel> create(modelClass: Class<T>): T {
        return if (modelClass.isAssignableFrom(SplashViewModel::class.java))
            SplashViewModel(userRepository) as T
        else
            throw IllegalArgumentException("unknown view model")
    }

}