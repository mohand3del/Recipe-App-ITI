package com.example.recipeappiti.home.viewModel

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

    private val _someRandomMeal = MutableLiveData<Response<MutableList<Meal>>>()
    val someRandomMeal: LiveData<Response<MutableList<Meal>>> get() = _someRandomMeal

    fun getSomeRandomMeal(much: Int) {
        _someRandomMeal.value = Response.Loading

        viewModelScope.launch {
            try {

                val meals = mutableListOf<Meal>()

                val meals2 = mutableListOf<Meal>().apply {
                    repeat(much) {

                        add(


                            mealRepository.getRandomDataMeal().meals.first()

                        )
                    }
                }
                meals.addAll(meals2)

//                val meals = mutableListOf<Meal>()
//
//                val meals2 = mutableListOf<Deferred<Meal>>().apply {
//                    repeat(much) {
//
//                        add(
//                            async {
//
//                                mealRepository.getRandomDataMeal().meals.first()
//                            }
//                        )
//                    }
//                }
//                meals.addAll(meals2.awaitAll())
                //StandaloneCoroutine is cancelling
                //kotlinx.coroutines.JobCancellationException: StandaloneCoroutine is cancelling; job=StandaloneCoroutine{Cancelling}@f71d6d

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

//    fun getSomeRandomMeal(much: Int) {
//        _someRandomMeal.value = Response.Loading
//
//        viewModelScope.launch {
//            try {
//                val meals = withContext(Dispatchers.IO) {
//                    List(much) {
//                        mealRepository.getRandomDataMeal().meals.first()
//                    }
//                }
//                _someRandomMeal.value = Response.Success(meals.toMutableList())
//            } catch (e: IOException) {
//                _someRandomMeal.value = Response.Failure(FailureReason.NoInternet)
//            } catch (e: Exception) {
//                _someRandomMeal.value = Response.Failure(
//                    FailureReason.UnknownError(
//                        error = e.message ?: "Unknown error occurred"
//                    )
//                )
//            }
//        }
//    }


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