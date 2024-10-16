package com.example.recipeappiti.core.viewmodel

import android.util.Log
import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.example.recipeappiti.core.model.local.repository.UserRepository
import com.example.recipeappiti.core.model.remote.Meal
import com.example.recipeappiti.core.model.remote.repository.MealRepository
import kotlinx.coroutines.launch

class DataViewModel(
    private val userRepository: UserRepository,
    private val mealRepository: MealRepository
) : ViewModel() {
    private val _favouritesList = MutableLiveData<MutableSet<String>>()

    private val _categorySearch = MutableLiveData<String?>()
    val categorySearch: LiveData<String?> get() = _categorySearch

    private val _mainCuisine = MutableLiveData<String?>()
    val mainCuisine: LiveData<String?> get() = _mainCuisine

    private val _itemDetails = MutableLiveData<String>()
    val itemDetails: LiveData<String> get() = _itemDetails

    private val _isFavourite = MutableLiveData<Boolean>()
    val isFavourite: LiveData<Boolean> get() = _isFavourite

    private val _isSubscribed = MutableLiveData<Boolean>()
    val isSubscribed: LiveData<Boolean> get() = _isSubscribed

    private val _recipes = MutableLiveData<MutableList<Meal>>()
    val recipes: LiveData<MutableList<Meal>> get() = _recipes

    private val _cuisinesData = MutableLiveData<List<String>>()
    val cuisinesData: LiveData<List<String>> get() = _cuisinesData


    init {
        _categorySearch.value = null
        _mainCuisine.value = null
        loadFavoriteItems()
        checkSubscription()
    }

    fun setCuisines(cuisines: List<String>) {
        _cuisinesData.value = cuisines
        Log.d("cuisines", _cuisinesData.value.toString())

        viewModelScope.launch {
            userRepository.updateCuisines(cuisines)
        }
    }

    private fun loadFavoriteItems() {
        viewModelScope.launch {
            _favouritesList.value =
                userRepository.getLoggedInUser()?.favourites?.toMutableSet() ?: mutableSetOf()
        }.invokeOnCompletion {
            getMeals()
        }
    }

    private fun getMeals() {
        viewModelScope.launch {
            val updatedMeals = mutableListOf<Meal>()
            _favouritesList.value?.forEach { mealId ->
                val meal = mealRepository.getMealById(mealId)
                updatedMeals.add(meal)
            }
            _recipes.value = updatedMeals
        }
    }

    suspend fun changeFavouriteState(recipeId: String, isChange: Boolean) {
        var currentFavouriteState = _favouritesList.value?.contains(recipeId)
        if (isChange) {
            if (currentFavouriteState == true) {
                _favouritesList.value?.remove(recipeId)
                currentFavouriteState = false
            } else {
                _favouritesList.value?.add(recipeId)
                currentFavouriteState = true
            }
            userRepository.updateFavourites(
                _favouritesList.value?.toMutableList() ?: mutableListOf()
            )
            getMeals()
        }
        _isFavourite.value = currentFavouriteState ?: false
    }

    fun updateSubscriptionState(subscribed: Boolean) {
        viewModelScope.launch {
            userRepository.updateSubscriptionState(subscribed)
        }.invokeOnCompletion {
            checkSubscription()
        }
    }

    fun checkSubscription() {
        viewModelScope.launch {
            val isSubscribed = userRepository.checkSubscriptionState()
            when (isSubscribed) {
                true -> _isSubscribed.value = true
                false -> _isSubscribed.value = false
            }
        }
    }

    fun updateSearchCategory(category: String?) {
        _categorySearch.value = category
    }

    fun updateMainCuisine(cuisine: String?) {
        _mainCuisine.value = cuisine
    }

    fun setItemDetails(id: String) {
        _itemDetails.value = id
    }
}
