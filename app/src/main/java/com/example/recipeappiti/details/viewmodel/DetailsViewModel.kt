package com.example.recipeappiti.details.viewmodel

import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.example.recipeappiti.auth.repository.UserRepository
import com.example.recipeappiti.home.repository.MealRepository
import com.example.recipeappiti.home.model.Meal
import com.example.recipeappiti.home.model.getIngredientsWithMeasurements
import kotlinx.coroutines.launch

class DetailsViewModel(
    private val mealRepository: MealRepository,
    private val userRepository: UserRepository
) : ViewModel() {
    private var favourites: MutableList<String> = mutableListOf()

    private val _isFavourite = MutableLiveData<Boolean>()
    val isFavourite: LiveData<Boolean> get() = _isFavourite

    private val _recipe = MutableLiveData<Meal>()
    val recipe: LiveData<Meal> get() = _recipe

    init {
        viewModelScope.launch {
            favourites = userRepository.getLoggedInUser()?.favourites?.toMutableList() ?: mutableListOf()
            println(favourites)
        }
    }

    fun getMealDetails(mealId: String) {
        viewModelScope.launch {
            val meal = mealRepository.getMealById(mealId)
            meal.let {
                it.listIngredientsWithMeasures = it.getIngredientsWithMeasurements()
                _recipe.value = it
            }
        }
    }

    fun changeFavouriteState(recipeId: String) {
        viewModelScope.launch {
            val currentFavouriteState = favourites.contains(recipeId)
            if (currentFavouriteState) {
                favourites.remove(recipeId)
            } else {
                favourites.add(recipeId)
                println(favourites)
            }
            println(favourites)
            userRepository.updateFavourites(favourites)
            _isFavourite.value = !currentFavouriteState
        }
    }

    fun checkFavouriteState(recipeId: String) {
        _isFavourite.value = favourites.contains(recipeId)
    }

    fun extractYouTubeVideoId(url: String): String {
        val regex = ".*(?:youtu.be/|v=|embed/|watch\\?v=)([^\"&?\\s]{11}).*".toRegex()
        val match = regex.find(url)
        return match?.groupValues?.get(1) ?: ""
    }
}