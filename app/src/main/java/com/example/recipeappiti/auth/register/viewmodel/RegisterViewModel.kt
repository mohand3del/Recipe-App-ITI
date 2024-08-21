package com.example.recipeappiti.auth.register.viewmodel

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
import com.example.recipeappiti.auth.repository.UserRepository
import com.example.recipeappiti.core.repository.Repository
import com.example.recipeappiti.layout.home.model.Area
import kotlinx.coroutines.launch

class RegisterViewModel(
    private val userRepository: UserRepository,
    private val recipeRepository: Repository
): ViewModel() {
    private val _registerState = MutableLiveData<Boolean>()
    val registerState: LiveData<Boolean> get() = _registerState

    private val _cuisines = MutableLiveData<List<Area>>()
    val cuisines: LiveData<List<Area>> get() = _cuisines

    private val _usernameMessage = MutableLiveData<ValidateCredentials>()
    val usernameMessage: LiveData<ValidateCredentials> = _usernameMessage

    private val _emailMessage = MutableLiveData<ValidateCredentials>()
    val emailMessage: LiveData<ValidateCredentials> = _emailMessage

    private val _passwordMessage = MutableLiveData<ValidateCredentials>()
    val passwordMessage: LiveData<ValidateCredentials> = _passwordMessage

    private val _confirmPasswordMessage = MutableLiveData<ValidateCredentials>()
    val confirmPasswordMessage: LiveData<ValidateCredentials> = _confirmPasswordMessage

    private val _cuisineMessage = MutableLiveData<ValidateCredentials>()
    val cuisineMessage: LiveData<ValidateCredentials> = _cuisineMessage

    fun registerUser(
        username: String,
        email: String,
        password: String,
        cuisine: String
    ) {
        val hashedPassword = PasswordUtil.hashPassword(password)
        val createdUser = User(username = username, email = email, password = hashedPassword, cuisine = cuisine)
        addUser(createdUser)
    }

    private fun addUser(user: User) {
        viewModelScope.launch {
            userRepository.addUser(user)
        }.invokeOnCompletion {
            _registerState.postValue(true)
        }
    }

    fun getCuisines() {
        viewModelScope.launch {
            val cuisines = recipeRepository.getAreasOfMeals()
            _cuisines.postValue(cuisines.meals)
        }
    }

    fun validateUsername(username: String) {
        _usernameMessage.value = when {
            username.isEmpty() -> ValidateCredentials.Invalid("Username cannot be empty")
            username.length < 6 -> ValidateCredentials.Invalid("Minimum 6 characters long")
            else -> ValidateCredentials.Valid
        }
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
            password.length < 8 -> ValidateCredentials.Invalid("Minimum 8 characters long")
            !password.matches(".*[0-9].*".toRegex()) -> ValidateCredentials.Invalid("Minimum one number")
            !password.matches(".*[A-Z].*".toRegex()) -> ValidateCredentials.Invalid("Minimum one uppercase letter")
            !password.matches(".*[a-z].*".toRegex()) -> ValidateCredentials.Invalid("Minimum one lowercase letter")
            !password.matches(".*[!@#$%^&*()_+].*".toRegex()) -> ValidateCredentials.Invalid("Minimum one special character")
            else -> ValidateCredentials.Valid
        }
    }

    fun validatePasswordConfirmation(password: String, confirmPassword: String) {
        _confirmPasswordMessage.value = when {
            confirmPassword.isEmpty() -> ValidateCredentials.Invalid("Confirmation cannot be empty")
            confirmPassword != password -> ValidateCredentials.Invalid("Passwords do not match")
            else -> ValidateCredentials.Valid
        }
    }

    fun validateCuisine(cuisine: String) {
        _cuisineMessage.value = when {
            cuisine.isEmpty() -> ValidateCredentials.Invalid("Cuisine cannot be empty")
            else -> ValidateCredentials.Valid
        }
    }

    fun validateCredentials(
        usernameMessage: Boolean,
        emailMessage: Boolean,
        passwordMessage: Boolean,
        confirmPasswordMessage: Boolean,
        cuisineMessage: Boolean
    ): Boolean {
        return !usernameMessage
                && !emailMessage
                && !passwordMessage
                && !confirmPasswordMessage
                && !cuisineMessage
    }

    fun callAlert(context: Context, message: String) {
        AlertDialog.Builder(context)
            .setTitle("Registration Failed")
            .setMessage(message)
            .setPositiveButton("OK") { dialog, _ ->
                dialog.dismiss()
            }
            .show()
    }
}