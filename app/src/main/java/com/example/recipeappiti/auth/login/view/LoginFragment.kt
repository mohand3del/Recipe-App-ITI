package com.example.recipeappiti.auth.login.view

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Button
import androidx.fragment.app.Fragment
import androidx.fragment.app.viewModels
import androidx.lifecycle.Observer
import com.example.recipeappiti.R
import com.example.recipeappiti.auth.login.viewmodel.LoginViewModel
import com.example.recipeappiti.auth.login.viewmodel.LoginViewModelFactory
import com.example.recipeappiti.auth.model.ValidateCredentials
import com.example.recipeappiti.auth.model.data.LocalDataSourceImpl
import com.example.recipeappiti.auth.repository.UserRepositoryImpl
import com.example.recipeappiti.core.model.local.UserDatabase
import com.google.android.material.textfield.TextInputEditText
import com.google.android.material.textfield.TextInputLayout

class LoginFragment : Fragment() {

    private lateinit var emailField: TextInputEditText
    private lateinit var emailLayout: TextInputLayout
    private lateinit var passwordField: TextInputEditText
    private lateinit var passwordLayout: TextInputLayout
    private lateinit var signInButton: Button

    private val loginViewModel: LoginViewModel by viewModels {
        val userRepository = UserRepositoryImpl(
            LocalDataSourceImpl(
                UserDatabase.getDatabaseInstance(requireContext()).userDao())
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
        observeValidations()
    }

    private fun initViews() {
        emailField = requireView().findViewById(R.id.emailField2)
        emailLayout = requireView().findViewById(R.id.emailLayout2)
        passwordField = requireView().findViewById(R.id.passwordField2)
        passwordLayout = requireView().findViewById(R.id.passwordLayout2)
        signInButton = requireView().findViewById(R.id.signInButton)
    }

    private fun initListeners() {
        emailField.setOnFocusChangeListener { _, hasFocus ->
            if (!hasFocus) {
                loginViewModel.validateEmail(emailField.text.toString())
            }
        }

        passwordField.setOnFocusChangeListener { _, hasFocus ->
            if (!hasFocus) {
                loginViewModel.validatePassword(passwordField.text.toString())
            }
        }

        signInButton.setOnClickListener {
            processLogin()
        }
    }

    private fun processLogin() {
        val isEmailValid = loginViewModel.emailMessage.value == ValidateCredentials.Valid
        val isPasswordValid = loginViewModel.passwordMessage.value == ValidateCredentials.Valid

        if (loginViewModel.validateCredentials(isEmailValid, isPasswordValid)) {
            loginViewModel.getUser(emailField.text.toString(), passwordField.text.toString())
        } else {
            val message = "Please Provide Valid Credentials"
            loginViewModel.callAlert(requireContext(), message)
        }
    }

    private fun observeValidations() {
        loginViewModel.emailMessage.observe(viewLifecycleOwner, Observer { validationResult ->
            emailLayout.helperText = when (validationResult) {
                is ValidateCredentials.Valid -> null
                is ValidateCredentials.Invalid -> validationResult.message
            }
        })

        loginViewModel.passwordMessage.observe(viewLifecycleOwner, Observer { validationResult ->
            passwordLayout.helperText = when (validationResult) {
                is ValidateCredentials.Valid -> null
                is ValidateCredentials.Invalid -> validationResult.message
            }
        })

        loginViewModel.isUserValid.observe(viewLifecycleOwner, Observer { isValid ->
            val message = if (isValid) "Login Successful" else "Login Failed"
            loginViewModel.callAlert(requireContext(), message)
        })
    }


}
