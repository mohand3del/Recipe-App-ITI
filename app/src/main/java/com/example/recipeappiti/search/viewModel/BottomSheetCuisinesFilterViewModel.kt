package com.example.recipeappiti.search.viewModel

import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.example.recipeappiti.home.model.FailureReason
import com.example.recipeappiti.home.model.GsonDataArea
import com.example.recipeappiti.home.model.GsonDataMeal
import com.example.recipeappiti.home.model.Response
import com.example.recipeappiti.home.repository.MealRepository
import kotlinx.coroutines.launch
import java.io.IOException

class BottomSheetCuisinesFilterViewModel(

    private val mealRepository: MealRepository

) : ViewModel() {

    private val _cuisines: MutableLiveData<Response<GsonDataArea>> = MutableLiveData()
    val cuisines: LiveData<Response<GsonDataArea>> get() = _cuisines


    fun getCuisines() = applyResponse(_cuisines){
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