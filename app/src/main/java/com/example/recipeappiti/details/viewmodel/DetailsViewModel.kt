package com.example.recipeappiti.details.viewmodel

import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.example.recipeappiti.core.model.remote.Meal
import com.example.recipeappiti.core.model.remote.getIngredientsWithMeasurements
import com.example.recipeappiti.core.model.remote.repository.MealRepository
import kotlinx.coroutines.launch

class DetailsViewModel(private val mealRepository: MealRepository) : ViewModel() {
    private val _recipe = MutableLiveData<Meal>()
    val recipe: LiveData<Meal> get() = _recipe

    fun getMealDetails(mealId: String) {
        viewModelScope.launch {
            val meal = mealRepository.getMealById(mealId)
            meal.let {
                it.listIngredientsWithMeasures = it.getIngredientsWithMeasurements()
                _recipe.value = it
            }
        }
    }

    fun extractYouTubeVideoId(url: String): String {
        val regex = ".*(?:youtu.be/|v=|embed/|watch\\?v=)([^\"&?\\s]{11}).*".toRegex()
        val match = regex.find(url)
        return match?.groupValues?.get(1) ?: ""
    }
}