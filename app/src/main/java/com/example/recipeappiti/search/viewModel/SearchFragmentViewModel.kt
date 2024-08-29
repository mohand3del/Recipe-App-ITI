package com.example.recipeappiti.search.viewModel

import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.example.recipeappiti.core.model.remote.FailureReason
import com.example.recipeappiti.core.model.remote.GsonDataMeal
import com.example.recipeappiti.core.model.remote.Response
import com.example.recipeappiti.core.model.remote.repository.MealRepository
import kotlinx.coroutines.launch
import java.io.IOException

class SearchFragmentViewModel(private val mealRepository: MealRepository) : ViewModel() {

    private val _dataMeals: MutableLiveData<Response<GsonDataMeal>> = MutableLiveData()
    val dataMeals: LiveData<Response<GsonDataMeal>> get() = _dataMeals

    fun searchByTitle(title: String) = applyResponse(_dataMeals) {
        mealRepository.getMealsBySearch(title)
    }


    private val _categoryMeals: MutableLiveData<Response<GsonDataMeal>> = MutableLiveData()
    val categoryMeals: LiveData<Response<GsonDataMeal>> get() = _categoryMeals

    fun getCategoryMeals(category: String) = applyResponse(_categoryMeals) {
        mealRepository.getCategoryMeals(category)
    }


    private val _cuisineMeals: MutableLiveData<Response<GsonDataMeal>> = MutableLiveData()
    val cuisineMeals: LiveData<Response<GsonDataMeal>> get() = _cuisineMeals

    fun getCuisinesMeals(area: String) = applyResponse(_cuisineMeals) {
        mealRepository.getCuisinesMeals(area)
    }


    private val _chosenCuisine: MutableLiveData<String?> = MutableLiveData()
    val chosenCuisine: LiveData<String?> get() = _chosenCuisine

    fun updateCuisine(cuisine: String?) {
        _chosenCuisine.value = cuisine
    }

    private val _chosenCategory: MutableLiveData<String?> = MutableLiveData()
    val chosenCategory: LiveData<String?> get() = _chosenCategory

    fun updateCategory(category: String?) {
        _chosenCategory.value = category
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