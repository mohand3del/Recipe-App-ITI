package com.example.recipeappiti.search.viewModel

import androidx.lifecycle.ViewModel
import androidx.lifecycle.ViewModelProvider
import com.example.recipeappiti.core.model.remote.repository.MealRepository

class BottomSheetCuisinesFilterViewModelFactory(
    private val mealRepository: MealRepository
) : ViewModelProvider.Factory {

    override fun <T : ViewModel> create(modelClass: Class<T>): T {


        if (modelClass.isAssignableFrom(BottomSheetCuisinesFilterViewModel::class.java))
            return BottomSheetCuisinesFilterViewModel(
                mealRepository
            ) as T
        else throw IllegalArgumentException("view model unknown")
    }

}