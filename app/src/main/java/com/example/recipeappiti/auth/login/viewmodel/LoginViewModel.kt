package com.example.recipeappiti.auth.login.viewmodel

import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.example.recipeappiti.core.model.local.User
import com.example.recipeappiti.auth.repository.UserRepositoryImpl
import kotlinx.coroutines.launch

class LoginViewModel(private val userRepository: UserRepositoryImpl): ViewModel() {
    private val _user = MutableLiveData<User?>()
    val user: LiveData<User?> get() = _user

    fun getUser(email: String, password: String) {
        viewModelScope.launch {
            val foundUser = userRepository.getUser(email)
            _user.postValue(foundUser)
        }
    }
}