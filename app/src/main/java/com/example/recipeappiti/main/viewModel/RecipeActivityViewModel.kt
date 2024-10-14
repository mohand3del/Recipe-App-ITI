package com.example.recipeappiti.main.viewModel

import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.example.recipeappiti.core.model.local.repository.UserRepository
import com.example.recipeappiti.core.model.remote.FailureReason
import com.example.recipeappiti.core.model.remote.Response
import kotlinx.coroutines.launch
import java.io.IOException

class RecipeActivityViewModel(

    private val userRepository: UserRepository

) : ViewModel() {

    private val _navigateToFragment = MutableLiveData<Int>()
    val navigateToFragment: LiveData<Int> get() = _navigateToFragment

    fun navigateTo(fragmentName: Int) {
        _navigateToFragment.value = fragmentName
    }

    private val _deletedAccount = MutableLiveData<Response<Unit>>()
    val deletedAccount: LiveData<Response<Unit>> get() = _deletedAccount

    fun deleteAccount() = applyResponse(_deletedAccount) {
        userRepository.deleteLoggedInUser()
    }

    private val _loggedOut = MutableLiveData<Response<Unit>>()
    val loggedOut: LiveData<Response<Unit>> get() = _loggedOut


    fun logOut() = applyResponse(_loggedOut) {
        userRepository.logOutUser()
    }

    private val _userName = MutableLiveData<Response<String>>()
    val userName: LiveData<Response<String>> get() = _userName

    fun getUserName() = applyResponse(_userName) {
        userRepository.getLoggedInUsername()
    }

    private val _userEmail = MutableLiveData<Response<String>>()
    val userEmail: LiveData<Response<String>> get() = _userEmail

    fun getUserEmail() = applyResponse(_userEmail) {
        userRepository.getLoggedInEmail()
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