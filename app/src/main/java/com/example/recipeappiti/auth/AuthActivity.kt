package com.example.recipeappiti.auth

import android.os.Bundle
import androidx.activity.enableEdgeToEdge
import androidx.activity.viewModels
import androidx.appcompat.app.AppCompatActivity
import androidx.core.view.ViewCompat
import androidx.core.view.WindowInsetsCompat
import com.example.recipeappiti.R
import com.example.recipeappiti.auth.model.UserRepository
import com.example.recipeappiti.auth.viewmodel.UserViewModel
import com.example.recipeappiti.auth.viewmodel.UserViewModelFactory
import com.example.recipeappiti.core.model.UserDatabase

class AuthActivity : AppCompatActivity() {
    private val userViewModel: UserViewModel by viewModels {
        UserViewModelFactory(
            UserRepository(
                UserDatabase.getDatabaseInstance(applicationContext
                ).userDao()
            )
        )
    }

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        enableEdgeToEdge()
        setContentView(R.layout.activity_auth)
        ViewCompat.setOnApplyWindowInsetsListener(findViewById(R.id.main)) { v, insets ->
            val systemBars = insets.getInsets(WindowInsetsCompat.Type.systemBars())
            v.setPadding(systemBars.left, systemBars.top, systemBars.right, systemBars.bottom)
            insets
        }
    }
}