package com.example.recipeappiti.home.viewModel

import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.example.recipeappiti.home.model.FailureReason
import com.example.recipeappiti.home.model.Response
import com.example.recipeappiti.auth.repository.UserRepository
import com.example.recipeappiti.home.repository.MealRepository
import com.example.recipeappiti.home.model.GsonDataArea
import com.example.recipeappiti.home.model.GsonDataCategories
import com.example.recipeappiti.home.model.GsonDataMeal
import com.example.recipeappiti.home.model.Meal
import kotlinx.coroutines.Deferred
import kotlinx.coroutines.async
import kotlinx.coroutines.awaitAll
import kotlinx.coroutines.launch
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

    private val _randomMeal: MutableLiveData<Response<GsonDataMeal>> = MutableLiveData()
    val randomMeal: LiveData<Response<GsonDataMeal>> get() = _randomMeal

    fun getRandomMeal() {

        applyResponse(_randomMeal) {
            mealRepository.getRandomDataMeal()
        }

    }

    private val _someRandomMeal = MutableLiveData<Response<MutableList<Meal>>>()
    val someRandomMeal: LiveData<Response<MutableList<Meal>>> get() = _someRandomMeal

    fun getSomeRandomMeal(much: Int) {
        _someRandomMeal.value = Response.Loading

        viewModelScope.launch {
            try {

                val meals = mutableListOf<Meal>()

                val meals2 = mutableListOf<Deferred<Meal>>().apply {
                    repeat(much) {

                        add(
                            async {

                                mealRepository.getRandomDataMeal().meals.first()
                            }
                        )
                    }
                }
                meals.addAll(meals2.awaitAll())





                _someRandomMeal.value = Response.Success(meals)
            } catch (e: IOException) { // Example for no internet
                _someRandomMeal.value = Response.Failure(FailureReason.NoInternet)
            } catch (e: Exception) {
                _someRandomMeal.value = Response.Failure(
                    FailureReason.UnknownError(
                        error = e.message ?: "Unknown error occurred"
                    )
                )
            }
        }
    }

    private var _filteredMealsByAreas: MutableLiveData<Response<GsonDataMeal>> = MutableLiveData()
    val filteredMealsByAreas: LiveData<Response<GsonDataMeal>> get() = _filteredMealsByAreas

    fun getFilteredMealsByAreas(area: String) {
        applyResponse(_filteredMealsByAreas) {
            mealRepository.getFilteredMealsByAreas(area)
        }
    }

    private var _areasOfMeals: MutableLiveData<Response<GsonDataArea>> = MutableLiveData()
    val areasOfMeals: LiveData<Response<GsonDataArea>> get() = _areasOfMeals

    fun getAreasOfMeals() {
        applyResponse(_areasOfMeals) {
            mealRepository.getAreasOfMeals()
        }
    }

    private var _userCuisine: MutableLiveData<Response<String?>> = MutableLiveData()
    val userCuisine: LiveData<Response<String?>> get() = _userCuisine

    fun getCuisine(email: List<String>) {

//        applyResponse(_userCuisine) {
//            userRepository.getCuisines(email)
//        }

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