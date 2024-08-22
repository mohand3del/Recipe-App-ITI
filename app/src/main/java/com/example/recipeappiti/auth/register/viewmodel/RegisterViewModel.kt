package com.example.recipeappiti.auth.register.viewmodel

import android.util.Patterns
import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.example.recipeappiti.auth.model.ValidateCredentials
import com.example.recipeappiti.auth.model.util.PasswordUtil
import com.example.recipeappiti.auth.repository.UserRepository
import com.example.recipeappiti.core.model.local.User
import com.example.recipeappiti.home.model.Area
import com.example.recipeappiti.home.repository.MealRepository
import kotlinx.coroutines.launch

class RegisterViewModel(
    private val userRepository: UserRepository,
) : ViewModel() {
    private val _registerState = MutableLiveData<ValidateCredentials>()
    val registerState: LiveData<ValidateCredentials> get() = _registerState

    private val _usernameMessage = MutableLiveData<ValidateCredentials>()
    val usernameMessage: LiveData<ValidateCredentials> = _usernameMessage

    private val _emailMessage = MutableLiveData<ValidateCredentials>()
    val emailMessage: LiveData<ValidateCredentials> = _emailMessage

    private val _passwordMessage = MutableLiveData<ValidateCredentials>()
    val passwordMessage: LiveData<ValidateCredentials> = _passwordMessage

    private val _confirmPasswordMessage = MutableLiveData<ValidateCredentials>()
    val confirmPasswordMessage: LiveData<ValidateCredentials> = _confirmPasswordMessage

    fun registerUser(
        username: String,
        email: String,
        password: String,
    ) {
        val hashedPassword = PasswordUtil.hashPassword(password)
        val createdUser =
            User(username = username, email = email, password = hashedPassword)
        addUser(createdUser)
    }

    private fun addUser(user: User) {
        viewModelScope.launch {
            userRepository.addUser(user)
        }.invokeOnCompletion {
            _registerState.value = ValidateCredentials.Valid
        }
    }

    fun validateUsername(username: String) {
        _usernameMessage.value = when {
            username.isEmpty() -> ValidateCredentials.InValid("Username cannot be empty")
            username.length < 6 -> ValidateCredentials.InValid("Minimum 6 characters long")

            else -> ValidateCredentials.Valid
        }
    }

    fun validateEmail(email: String) {
        _emailMessage.value = when {
            email.isEmpty() -> ValidateCredentials.InValid("Email cannot be empty")
            !Patterns.EMAIL_ADDRESS.matcher(email)
                .matches() -> ValidateCredentials.InValid("Invalid email address")

            else -> ValidateCredentials.Valid
        }
    }

    fun validatePassword(password: String) {
        _passwordMessage.value = when {
            password.isEmpty() -> ValidateCredentials.InValid("Password cannot be empty")
            password.length < 8 -> ValidateCredentials.InValid("Minimum 8 characters long")
            !password.matches(".*[0-9].*".toRegex()) -> ValidateCredentials.InValid("Minimum one number")
            !password.matches(".*[A-Z].*".toRegex()) -> ValidateCredentials.InValid("Minimum one uppercase letter")
            !password.matches(".*[a-z].*".toRegex()) -> ValidateCredentials.InValid("Minimum one lowercase letter")
            !password.matches(".*[!@#$%^&*()_+].*".toRegex()) -> ValidateCredentials.InValid("Minimum one special character")

            else -> ValidateCredentials.Valid
        }
    }

    fun validatePasswordConfirmation(password: String, confirmPassword: String) {
        _confirmPasswordMessage.value = when {
            confirmPassword.isEmpty() -> ValidateCredentials.InValid("Confirmation cannot be empty")
            confirmPassword != password -> ValidateCredentials.InValid("Passwords do not match")

            else -> ValidateCredentials.Valid
        }
    }

    fun validateCredentials(
        usernameMessage: Boolean,
        emailMessage: Boolean,
        passwordMessage: Boolean,
        confirmPasswordMessage: Boolean
    ): ValidateCredentials {
        return when {
            !usernameMessage && !emailMessage && !passwordMessage && !confirmPasswordMessage -> {
                ValidateCredentials.Valid
            }

            else -> {
                ValidateCredentials.InValid("Please fill in all fields")
            }
        }
    }
}