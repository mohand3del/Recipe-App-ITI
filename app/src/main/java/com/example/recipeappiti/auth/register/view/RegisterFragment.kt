package com.example.recipeappiti.auth.register.view

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Button
import android.widget.ScrollView
import androidx.core.widget.addTextChangedListener
import androidx.fragment.app.Fragment
import androidx.fragment.app.viewModels
import androidx.lifecycle.Observer
import androidx.navigation.fragment.findNavController
import com.example.recipeappiti.R
import com.example.recipeappiti.auth.model.ValidateCredentials
import com.example.recipeappiti.auth.register.viewmodel.RegisterViewModel
import com.example.recipeappiti.auth.register.viewmodel.RegisterViewModelFactory
import com.example.recipeappiti.core.model.local.repository.UserRepositoryImpl
import com.example.recipeappiti.core.model.local.source.LocalDataSourceImpl
import com.example.recipeappiti.core.model.local.source.UserDatabase
import com.example.recipeappiti.core.util.AlertUtil
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
    private lateinit var scrollView: ScrollView
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
        scrollView = requireView().findViewById(R.id.registerScrollView)
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
        usernameLayout.editText?.addTextChangedListener(
            onTextChanged = { _, _, _, _ ->
                registerViewModel.validateUsername(usernameField.text.toString())
            }
        )

        emailLayout.editText?.addTextChangedListener(
            onTextChanged = { _, _, _, _ ->
                registerViewModel.validateEmail(emailField.text.toString())
            }
        )

        passwordLayout.editText?.addTextChangedListener(
            onTextChanged = { _, _, _, _ ->
                registerViewModel.validatePassword(passwordField.text.toString())
            }
        )

        passwordConfirmLayout.editText?.addTextChangedListener(
            onTextChanged = { _, _, _, _ ->
                registerViewModel.validatePasswordConfirmation(
                    passwordField.text.toString(),
                    passwordConfirmField.text.toString()
                )
            }
        )

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
        registerViewModel.usernameMessage.observe(viewLifecycleOwner, Observer { result ->
            usernameLayout.helperText = when (result) {
                is ValidateCredentials.Valid -> null
                is ValidateCredentials.InValid -> result.message
            }
        })

        registerViewModel.emailMessage.observe(viewLifecycleOwner, Observer { result ->
            emailLayout.helperText = when (result) {
                is ValidateCredentials.Valid -> null
                is ValidateCredentials.InValid -> result.message
            }
        })

        registerViewModel.passwordMessage.observe(viewLifecycleOwner, Observer { result ->
            passwordLayout.helperText = when (result) {
                is ValidateCredentials.Valid -> null
                is ValidateCredentials.InValid -> result.message
            }
        })

        registerViewModel.confirmPasswordMessage.observe(viewLifecycleOwner, Observer { result ->
            passwordConfirmLayout.helperText = when (result) {
                is ValidateCredentials.Valid -> null
                is ValidateCredentials.InValid -> result.message
            }
        })

        registerViewModel.registerState.observe(viewLifecycleOwner, Observer { result ->
            when (result) {
                is ValidateCredentials.Valid -> {
                    val navController = findNavController()
                    navController.popBackStack()
                }

                is ValidateCredentials.InValid -> {
                    AlertUtil.showAlert(requireContext(), result.message)
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
            is ValidateCredentials.Valid -> {
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