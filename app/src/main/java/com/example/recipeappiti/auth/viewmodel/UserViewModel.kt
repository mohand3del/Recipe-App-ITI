package com.example.recipeappiti.auth.viewmodel

import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.example.recipeappiti.auth.model.User
import com.example.recipeappiti.auth.model.UserRepository
import kotlinx.coroutines.launch

class UserViewModel(private val userRepository: UserRepository): ViewModel() {
    private val _user = MutableLiveData<User?>()
    val user: LiveData<User?> get() = _user

    fun getUser(email: String, password: String) {
        viewModelScope.launch {
            val foundUser = userRepository.getUser(email, password)
            _user.postValue(foundUser)
        }
    }

    fun addUser(user: User) {
        viewModelScope.launch {
            userRepository.addUser(user)
        }
    }
}