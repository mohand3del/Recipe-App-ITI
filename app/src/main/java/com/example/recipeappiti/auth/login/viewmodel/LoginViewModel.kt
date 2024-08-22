package com.example.recipeappiti.auth.login.viewmodel

import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.example.recipeappiti.auth.model.util.PasswordUtil
import com.example.recipeappiti.auth.model.ValidateCredentials
import com.example.recipeappiti.auth.repository.UserRepositoryImpl
import kotlinx.coroutines.launch

class LoginViewModel(private val userRepository: UserRepositoryImpl) : ViewModel() {
    private val _isUserValid = MutableLiveData<ValidateCredentials?>()
    val isUserValid: LiveData<ValidateCredentials?> = _isUserValid

    fun checkUser(email: String, password: String) {
        viewModelScope.launch {
            val foundPassword = userRepository.getPassword(email)

            if (foundPassword == null) {
                _isUserValid.value = ValidateCredentials.InValid("Incorrect Credentials")
                return@launch
            }
            validateUser(password, foundPassword, email)
        }
    }

    private suspend fun validateUser(password: String, hashedPassword: String, email: String) {
        val isPasswordCorrect = PasswordUtil.checkPassword(password, hashedPassword)
        when (isPasswordCorrect) {
            true -> {
                _isUserValid.value = ValidateCredentials.Valid
                userRepository.updateLogInStatus(true, email)
            }
            false -> {
                _isUserValid.value = ValidateCredentials.InValid("Incorrect Credentials")
            }
        }
    }

    fun resetStates() {
        _isUserValid.value = null
    }
}