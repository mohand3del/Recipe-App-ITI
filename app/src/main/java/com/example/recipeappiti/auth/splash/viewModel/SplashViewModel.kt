package com.example.recipeappiti.auth.splash.viewModel

import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.example.recipeappiti.core.model.local.repository.UserRepository
import com.example.recipeappiti.core.model.remote.FailureReason
import com.example.recipeappiti.core.model.remote.Response
import kotlinx.coroutines.launch
import java.io.IOException

class SplashViewModel(

    private val userRepository: UserRepository

) : ViewModel() {


    private val _loggedInUser = MutableLiveData<Response<Boolean>>()
    val loggedInUser: LiveData<Response<Boolean>> get() = _loggedInUser

    fun getLoggedInUser() = applyResponse(_loggedInUser) {
        userRepository.findLoggedInUser()
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