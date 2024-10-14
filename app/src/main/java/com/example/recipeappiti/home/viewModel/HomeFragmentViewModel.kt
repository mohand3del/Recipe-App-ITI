package com.example.recipeappiti.home.viewModel

import android.util.Log
import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.example.recipeappiti.core.model.local.repository.UserRepository
import com.example.recipeappiti.core.model.remote.FailureReason
import com.example.recipeappiti.core.model.remote.GsonDataCategories
import com.example.recipeappiti.core.model.remote.GsonDataMeal
import com.example.recipeappiti.core.model.remote.Meal
import com.example.recipeappiti.core.model.remote.Response
import com.example.recipeappiti.core.model.remote.repository.MealRepository
import kotlinx.coroutines.CoroutineExceptionHandler
import kotlinx.coroutines.Deferred
import kotlinx.coroutines.async
import kotlinx.coroutines.awaitAll
import kotlinx.coroutines.launch
import retrofit2.HttpException
import java.io.IOException

class HomeFragmentViewModel(
    private val mealRepository: MealRepository,
    private val userRepository: UserRepository
) : ViewModel() {

    private var _dataCategories: MutableLiveData<Response<GsonDataCategories>> = MutableLiveData()
    val dataCategories: LiveData<Response<GsonDataCategories>> get() = _dataCategories

    fun getCategories() {
        applyResponse(_dataCategories) {
            mealRepository.getCategories()
        }
    }

    private val _userCuisines: MutableLiveData<Response<List<String>?>> = MutableLiveData()
    val userCuisines: LiveData<Response<List<String>?>> get() = _userCuisines

    fun getUserCuisines() {
        applyResponse(_userCuisines) {
            userRepository.getLoggedInUser()?.cuisines
        }
    }

    private var _filteredMealsByAreas: MutableLiveData<Response<GsonDataMeal>> = MutableLiveData()
    val filteredMealsByAreas: LiveData<Response<GsonDataMeal>> get() = _filteredMealsByAreas

    fun getFilteredMealsByAreas(area: String) {
        applyResponse(_filteredMealsByAreas) {
            mealRepository.getCuisinesMeals(area)
        }
    }

    private val _someGoldMeals = MutableLiveData<Response<MutableList<Meal>>>()
    val someGoldMeals: LiveData<Response<MutableList<Meal>>> get() = _someGoldMeals

    private val _someRecommendedMeals = MutableLiveData<Response<MutableList<Meal>>>()
    val someRecommendedMeals: LiveData<Response<MutableList<Meal>>> get() = _someRecommendedMeals

    fun getRandomMeals(much: Int, isGold: Boolean = false) {
        val responseHandler = MutableLiveData<Response<MutableList<Meal>>>()
        responseHandler.observeForever { response ->
            when (isGold) {
                true -> _someGoldMeals.value = response
                false -> _someRecommendedMeals.value = response
            }
        }
        responseHandler.value = Response.Loading

        val handler = CoroutineExceptionHandler { _, exception ->
            responseHandler.value = Response.Failure(
                FailureReason.UnknownError(
                    error = exception.message ?: "Unknown error occurred"
                )
            )
            Log.e("HomeFragmentViewModel", "Unknown: ${exception.message}")
        }

        viewModelScope.launch(handler) {
            val foundMeals = mutableListOf<Deferred<Meal>>().apply {
                repeat(much) {
                    add(
                        async() {
                            mealRepository.getRandomDataMeal().meals.first()
                        }
                    )
                }
            }
            val meals = foundMeals.awaitAll().toMutableList()
            responseHandler.value = Response.Success(meals)
            responseHandler.removeObserver { }
        }
    }

    private fun <T> applyResponse(
        liveData: MutableLiveData<Response<T>>,
        dataFetch: suspend () -> T
    ) {
        liveData.value = Response.Loading

        viewModelScope.launch {
            try {
                val result = dataFetch()
                liveData.value = Response.Success(result)
            } catch (e: IOException) {
                liveData.value = Response.Failure(FailureReason.NoInternet)
            } catch (e: Exception) {
                liveData.value = Response.Failure(
                    FailureReason.UnknownError(
                        error = e.message ?: "Unknown error occurred"
                    )
                )
            }
        }
    }
}