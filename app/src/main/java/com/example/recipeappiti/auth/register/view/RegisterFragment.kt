package com.example.recipeappiti.auth.register.view

import android.os.Bundle
import android.text.TextWatcher
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Button
import androidx.fragment.app.Fragment
import androidx.fragment.app.viewModels
import androidx.lifecycle.Observer
import androidx.navigation.fragment.findNavController
import com.example.recipeappiti.R
import com.example.recipeappiti.auth.model.ValidateCredentials
import com.example.recipeappiti.auth.model.data.LocalDataSourceImpl
import com.example.recipeappiti.auth.model.util.AlertUtil
import com.example.recipeappiti.auth.register.viewmodel.RegisterViewModel
import com.example.recipeappiti.auth.register.viewmodel.RegisterViewModelFactory
import com.example.recipeappiti.auth.repository.UserRepositoryImpl
import com.example.recipeappiti.core.model.local.UserDatabase
import com.google.android.material.textfield.TextInputEditText
import com.google.android.material.textfield.TextInputLayout

class RegisterFragment : Fragment() {
    private val registerViewModel: RegisterViewModel by viewModels {
        val userRepository = UserRepositoryImpl(
            LocalDataSourceImpl(
                UserDatabase.getDatabaseInstance(requireContext()).userDao()
            )
        )
        RegisterViewModelFactory(userRepository)
    }
    private lateinit var usernameField: TextInputEditText
    private lateinit var usernameLayout: TextInputLayout
    private lateinit var emailField: TextInputEditText
    private lateinit var emailLayout: TextInputLayout
    private lateinit var passwordField: TextInputEditText
    private lateinit var passwordLayout: TextInputLayout
    private lateinit var passwordConfirmField: TextInputEditText
    private lateinit var passwordConfirmLayout: TextInputLayout
    private lateinit var signUpButton: Button

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        return inflater.inflate(R.layout.fragment_register, container, false)
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        initViews()
        initListeners()
        observeValidations()
    }

    private fun initViews() {
        usernameField = requireView().findViewById(R.id.usernameField)
        usernameLayout = requireView().findViewById(R.id.usernameLayout)
        emailField = requireView().findViewById(R.id.emailField)
        emailLayout = requireView().findViewById(R.id.emailLayout)
        passwordField = requireView().findViewById(R.id.passwordField)
        passwordLayout = requireView().findViewById(R.id.passwordLayout)
        passwordConfirmField = requireView().findViewById(R.id.passwordConfirmField)
        passwordConfirmLayout = requireView().findViewById(R.id.passwordConfirmLayout)
        signUpButton = requireView().findViewById(R.id.signUpButton)
    }

    private fun initListeners() {
        usernameField.setOnFocusChangeListener { _, hasFocus ->
            if (!hasFocus) {
                registerViewModel.validateUsername(usernameField.text.toString())
            }
        }

//        usernameField.addTextChangedListener(
//            onTextChanged(s: CharSequence?, start: Int, before: Int, count: Int) {
//                registerViewModel.validateUsername(usernameField.text.toString())
//            }
//        )

        emailField.setOnFocusChangeListener { _, hasFocus ->
            if (!hasFocus) {
                registerViewModel.validateEmail(emailField.text.toString())
            }
        }

        passwordField.setOnFocusChangeListener { _, hasFocus ->
            if (!hasFocus) {
                registerViewModel.validatePassword(passwordField.text.toString())
            }
        }

        passwordConfirmField.setOnFocusChangeListener { _, hasFocus ->
            if (!hasFocus) {
                registerViewModel.validatePasswordConfirmation(
                    passwordField.text.toString(),
                    passwordConfirmField.text.toString()
                )
            }
        }

        signUpButton.setOnClickListener {
            processRegistration(
                usernameLayout.isHelperTextEnabled,
                emailLayout.isHelperTextEnabled,
                passwordLayout.isHelperTextEnabled,
                passwordConfirmLayout.isHelperTextEnabled,
            )
        }
    }

    private fun observeValidations() {
        registerViewModel.usernameMessage.observe(viewLifecycleOwner, Observer { validationResult ->
            usernameLayout.helperText = when (validationResult) {
                is ValidateCredentials.Valid -> null
                is ValidateCredentials.InValid -> validationResult.message
            }
        })

        registerViewModel.emailMessage.observe(viewLifecycleOwner, Observer { validationResult ->
            emailLayout.helperText = when (validationResult) {
                is ValidateCredentials.Valid -> null
                is ValidateCredentials.InValid -> validationResult.message
            }
        })

        registerViewModel.passwordMessage.observe(viewLifecycleOwner, Observer { validationResult ->
            passwordLayout.helperText = when (validationResult) {
                is ValidateCredentials.Valid -> null
                is ValidateCredentials.InValid -> validationResult.message
            }
        })

        registerViewModel.confirmPasswordMessage.observe(
            viewLifecycleOwner,
            Observer { validationResult ->
                passwordConfirmLayout.helperText = when (validationResult) {
                    is ValidateCredentials.Valid -> null
                    is ValidateCredentials.InValid -> validationResult.message
                }
            })

        registerViewModel.registerState.observe(viewLifecycleOwner, Observer { validationResult ->
            when (validationResult) {
                 is ValidateCredentials.Valid-> {
                    val navController = findNavController()
                    navController.popBackStack()
                }
                is ValidateCredentials.InValid -> {
                    AlertUtil.showAlert(requireContext(), validationResult.message)
                }
            }
        })
    }

    private fun processRegistration(
        usernameValidation: Boolean,
        emailValidation: Boolean,
        passwordValidation: Boolean,
        confirmPasswordValidation: Boolean,
    ) {
        val credentialsStatus = registerViewModel.validateCredentials(
            usernameValidation,
            emailValidation,
            passwordValidation,
            confirmPasswordValidation
        )
        when (credentialsStatus) {
            is ValidateCredentials.Valid-> {
                val username = usernameField.text.toString()
                val email = emailField.text.toString()
                val password = passwordField.text.toString()
                registerViewModel.registerUser(username, email, password)
            }
            is ValidateCredentials.InValid -> {
                AlertUtil.showAlert(requireContext(), credentialsStatus.message)
            }
        }
    }
}