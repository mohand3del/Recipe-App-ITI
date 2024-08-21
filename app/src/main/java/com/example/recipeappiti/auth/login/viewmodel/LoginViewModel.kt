package com.example.recipeappiti.auth.login.viewmodel

import android.content.Context
import android.util.Log
import android.util.Patterns
import androidx.appcompat.app.AlertDialog
import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.example.recipeappiti.auth.model.PasswordUtil
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

    private val _isPasswordValid = MutableLiveData<Boolean>()
    val isPasswordValid: LiveData<Boolean> = _isPasswordValid

    private val _passwordMessage = MutableLiveData<ValidateCredentials>()
    val passwordMessage: LiveData<ValidateCredentials> = _passwordMessage

    fun getUser(email: String, password: String) {
        viewModelScope.launch {
            val foundUser = userRepository.getUser(email)
            _user.postValue(foundUser)
            if (foundUser != null) {
                validatePassword(password, foundUser.password)
            } else {
                _isUserValid.postValue(false)
            }
        }
    }

    private fun validatePassword(password: String, hashedPassword: String) {
        val isPasswordCorrect = PasswordUtil.checkPassword(password, hashedPassword)
        _isPasswordValid.postValue(isPasswordCorrect)
        _isUserValid.postValue(isPasswordCorrect)
        Log.d("moon", _isUserValid.toString())
    }
    fun validateEmail(email: String) {
        _emailMessage.value = when {
            email.isEmpty() -> ValidateCredentials.Invalid("Email cannot be empty")
            !Patterns.EMAIL_ADDRESS.matcher(email).matches() -> ValidateCredentials.Invalid("Invalid email address")
            else -> ValidateCredentials.Valid
        }
    }

    fun validatePassword(password: String) {
        _passwordMessage.value = when {
            password.isEmpty() -> ValidateCredentials.Invalid("Password cannot be empty")
            else -> ValidateCredentials.Valid
        }
    }

    fun validateCredentials(
        isEmailValid: Boolean,
        isPasswordValid: Boolean
    ): Boolean {
        return isEmailValid && isPasswordValid
    }

    fun callAlert(context: Context, message: String) {
        if (context != null) {
            AlertDialog.Builder(context)
                .setTitle("Login Failed")
                .setMessage(message)
                .setPositiveButton("OK") { dialog, _ ->
                    dialog.dismiss()
                }
                .show()
        }
    }
}