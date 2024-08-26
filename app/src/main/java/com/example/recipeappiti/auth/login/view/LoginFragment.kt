package com.example.recipeappiti.auth.login.view

import android.content.Intent
import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Button
import android.widget.ScrollView
import android.widget.TextView
import androidx.fragment.app.Fragment
import androidx.fragment.app.viewModels
import androidx.lifecycle.Observer
import androidx.navigation.NavController
import androidx.navigation.findNavController
import com.example.recipeappiti.R
import com.example.recipeappiti.auth.login.viewmodel.LoginViewModel
import com.example.recipeappiti.auth.login.viewmodel.LoginViewModelFactory
import com.example.recipeappiti.auth.model.ValidateCredentials
import com.example.recipeappiti.auth.model.data.LocalDataSourceImpl
import com.example.recipeappiti.auth.model.util.AlertUtil
import com.example.recipeappiti.auth.repository.UserRepositoryImpl
import com.example.recipeappiti.core.model.local.UserDatabase
import com.example.recipeappiti.main.view.activities.RecipeActivity
import com.google.android.material.textfield.TextInputEditText
import com.google.android.material.textfield.TextInputLayout

class LoginFragment : Fragment() {
    private lateinit var scrollView: ScrollView
    private lateinit var emailField: TextInputEditText
    private lateinit var emailLayout: TextInputLayout
    private lateinit var passwordField: TextInputEditText
    private lateinit var passwordLayout: TextInputLayout
    private lateinit var navController: NavController
    private lateinit var signInButton: Button
    private lateinit var signUpText: TextView

    private val loginViewModel: LoginViewModel by viewModels {
        val userRepository = UserRepositoryImpl(
            LocalDataSourceImpl(
                UserDatabase.getDatabaseInstance(requireContext()).userDao()
            )
        )
        LoginViewModelFactory(userRepository)
    }

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        // Inflate the layout for this fragment
        return inflater.inflate(R.layout.fragment_login, container, false)
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        initViews()
        initListeners()
        initObservers()
    }

    private fun initViews() {
        scrollView = requireView().findViewById(R.id.loginScrollView)
        emailField = requireView().findViewById(R.id.emailField2)
        emailLayout = requireView().findViewById(R.id.emailLayout2)
        passwordField = requireView().findViewById(R.id.passwordField2)
        passwordLayout = requireView().findViewById(R.id.passwordLayout2)
        navController = requireView().findNavController()
        signInButton = requireView().findViewById(R.id.signInButton)
        signUpText = requireView().findViewById(R.id.signUpText)
    }

    private fun initListeners() {
        signInButton.setOnClickListener {
            processLogin()
        }

        signUpText.setOnClickListener {
            loginViewModel.resetStates()
            navController.navigate(R.id.action_loginFragment_to_registerFragment)
        }
    }

    private fun initObservers() {
        loginViewModel.isUserValid.observe(viewLifecycleOwner, Observer { result ->
            when (result) {
                is ValidateCredentials.Valid -> navigateToRecipeActivity()
                is ValidateCredentials.InValid -> AlertUtil.showAlert(
                    requireContext(),
                    result.message
                )

                null -> null
            }
        })
    }

    private fun processLogin() {
        val email = emailField.text.toString()
        val password = passwordField.text.toString()
        val trimmedEmail = email.trim()
        val trimmedPassword = password.trim()

        if (email.isBlank() || password.isBlank() || trimmedPassword != password || trimmedEmail != email) {
            AlertUtil.showAlert(requireContext(), getString(R.string.login_alert))
            return
        } else {
            loginViewModel.checkUser(email, password)
        }
    }

    private fun navigateToRecipeActivity() {
        startActivity(Intent(requireContext(), RecipeActivity::class.java))
        requireActivity().finish()
    }
}
