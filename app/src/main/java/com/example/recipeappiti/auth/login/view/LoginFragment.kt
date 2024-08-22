package com.example.recipeappiti.auth.login.view

import android.content.Context
import android.content.Intent
import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Button
import android.widget.TextView
import androidx.fragment.app.Fragment
import androidx.fragment.app.viewModels
import androidx.lifecycle.Observer
import androidx.navigation.NavController
import androidx.navigation.findNavController
import com.example.recipeappiti.R
import com.example.recipeappiti.auth.login.viewmodel.LoginViewModel
import com.example.recipeappiti.auth.login.viewmodel.LoginViewModelFactory
import com.example.recipeappiti.auth.model.util.AlertUtil
import com.example.recipeappiti.auth.model.ValidateCredentials
import com.example.recipeappiti.auth.model.data.LocalDataSourceImpl
import com.example.recipeappiti.core.model.local.User
import com.example.recipeappiti.core.model.local.UserDatabase
import com.example.recipeappiti.auth.repository.UserRepositoryImpl
import com.example.recipeappiti.RecipeActivity
import com.example.recipeappiti.auth.model.util.PasswordUtil
import com.google.android.material.textfield.TextInputEditText
import com.google.android.material.textfield.TextInputLayout

class LoginFragment : Fragment() {

    private lateinit var emailField: TextInputEditText
    private lateinit var emailLayout: TextInputLayout
    private lateinit var passwordField: TextInputEditText
    private lateinit var passwordLayout: TextInputLayout
    private lateinit var navController: NavController
    private lateinit var signInButton: Button
    private lateinit var signUpText: TextView
    private var user: User? = null

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
        initObservers()
    }

    private fun initViews() {
        emailField = requireView().findViewById(R.id.emailField2)
        emailLayout = requireView().findViewById(R.id.emailLayout2)
        passwordField = requireView().findViewById(R.id.passwordField2)
        passwordLayout = requireView().findViewById(R.id.passwordLayout2)
        navController = requireView().findNavController()
        signInButton = requireView().findViewById(R.id.signInButton)
        signUpText = requireView().findViewById(R.id.signUpText)
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

        signUpText.setOnClickListener {
            navController.navigate(R.id.action_loginFragment_to_registerFragment)
        }
    }

    private fun initObservers() {
        loginViewModel.emailMessage.observe(viewLifecycleOwner, Observer { validationResult ->
            emailLayout.helperText = when (validationResult) {
                is ValidateCredentials.Valid -> null
                is ValidateCredentials.InValid -> validationResult.message
            }
        })

        loginViewModel.passwordMessage.observe(viewLifecycleOwner, Observer { validationResult ->
            passwordLayout.helperText = when (validationResult) {
                is ValidateCredentials.Valid -> null
                is ValidateCredentials.InValid -> validationResult.message
            }
        })

        loginViewModel.isUserValid.observe(viewLifecycleOwner, Observer { isValid ->
            if (isValid) {
                navigateToRecipeActivity()
            } else {
                AlertUtil.showAlert(requireContext(), "Login Failed")
            }
        })

        loginViewModel.user.observe(viewLifecycleOwner, Observer { user ->
            this.user = user
        })
    }

    private fun processLogin() {
        val isEmailValid = !emailLayout.isHelperTextEnabled
        val isPasswordValid = !passwordLayout.isHelperTextEnabled

        if (loginViewModel.validateCredentials(isEmailValid, isPasswordValid)) {
            loginViewModel.checkUser(emailField.text.toString(), passwordField.text.toString())
        } else {
            val message = "Please Provide Valid Credentials"
            AlertUtil.showAlert(requireContext(), message)
        }
    }

    private fun navigateToRecipeActivity() {
//        val sharedPreferences = requireContext().getSharedPreferences("user_info", Context.MODE_PRIVATE)
//        val editPrefs = sharedPreferences.edit()
//        user?.id?.let { editPrefs.putInt("user_id", it).apply() }
//
//        val intent = Intent(requireContext(), com.example.recipeappiti.RecipeActivity::class.java)
//        startActivity(intent)
//        requireActivity().finish()
    }
}
