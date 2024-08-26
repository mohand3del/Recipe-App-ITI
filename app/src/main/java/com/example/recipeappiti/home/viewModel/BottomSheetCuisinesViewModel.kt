package com.example.recipeappiti.home.viewModel

import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.example.recipeappiti.auth.repository.UserRepository
import com.example.recipeappiti.home.model.FailureReason
import com.example.recipeappiti.home.model.GsonDataArea
import com.example.recipeappiti.home.model.Response
import com.example.recipeappiti.home.repository.MealRepository
import kotlinx.coroutines.launch
import java.io.IOException

class BottomSheetCuisinesViewModel(

    private val userRepository: UserRepository,
    private val mealRepository: MealRepository

) : ViewModel() {

    private val _setCuisinesData = MutableLiveData<Response<Unit>>()
    val setCuisinesData: LiveData<Response<Unit>> get() = _setCuisinesData


    fun setCuisines(cuisines: List<String>) {
        applyResponse(_setCuisinesData){userRepository.updateCuisines(cuisines)}
    }

    private val _allCuisines = MutableLiveData<Response<GsonDataArea>>()
    val allCuisines: LiveData<Response<GsonDataArea>> get() = _allCuisines


    fun getAllCuisines() = applyResponse(_allCuisines) {
        mealRepository.getCuisines()
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