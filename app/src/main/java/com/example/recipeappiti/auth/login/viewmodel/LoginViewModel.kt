package com.example.recipeappiti.auth.login.viewmodel

import android.util.Patterns
import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.example.recipeappiti.auth.model.util.PasswordUtil
import com.example.recipeappiti.auth.model.ValidateCredentials
import com.example.recipeappiti.core.model.local.User
import com.example.recipeappiti.auth.repository.UserRepositoryImpl
import kotlinx.coroutines.launch

class LoginViewModel(private val userRepository: UserRepositoryImpl) : ViewModel() {
    private val _user = MutableLiveData<User?>()
    val user: LiveData<User?> get() = _user

    private val _emailMessage = MutableLiveData<ValidateCredentials>()
    val emailMessage: LiveData<ValidateCredentials> = _emailMessage

    private val _isUserValid = MutableLiveData<Boolean>()
    val isUserValid: LiveData<Boolean> = _isUserValid

    private val _passwordMessage = MutableLiveData<ValidateCredentials>()
    val passwordMessage: LiveData<ValidateCredentials> = _passwordMessage

    fun checkUser(email: String, password: String) {
        viewModelScope.launch {
            val foundUser = userRepository.getUser(email)
            _user.postValue(foundUser)
            when (foundUser) {
                null -> _isUserValid.postValue(false)
                else -> validateUser(password, foundUser.password)
            }
        }
    }

    private fun validateUser(password: String, hashedPassword: String) {
        val isPasswordCorrect = PasswordUtil.checkPassword(password, hashedPassword)
        _isUserValid.postValue(isPasswordCorrect)
    }

    fun validateEmail(email: String) {
        _emailMessage.value = when {
            email.isEmpty() -> ValidateCredentials.InValid("Email cannot be empty")
            !Patterns.EMAIL_ADDRESS.matcher(email).matches() -> ValidateCredentials.InValid("Invalid email address")
            else -> ValidateCredentials.Valid
        }
    }

    fun validatePassword(password: String) {
        _passwordMessage.value = when {
            password.isEmpty() -> ValidateCredentials.InValid("Password cannot be empty")
            else -> ValidateCredentials.Valid
        }
    }

    fun validateCredentials(
        isEmailValid: Boolean,
        isPasswordValid: Boolean
    ): Boolean {
        return isEmailValid && isPasswordValid
    }
}