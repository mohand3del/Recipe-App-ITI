package com.example.recipeappiti.auth.newPassword

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Button
import android.widget.TextView
import androidx.fragment.app.Fragment
import androidx.navigation.NavController
import androidx.navigation.findNavController
import com.example.recipeappiti.R
import com.google.android.material.textfield.TextInputEditText

class EmailVerificationFragment : Fragment() {

    private lateinit var emailEditText: TextInputEditText
    private lateinit var buttonResetPassword: Button
    private lateinit var buttonBackToLogin: TextView
    private var navController: NavController? = null
    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {

        return inflater.inflate(R.layout.fragment_email_verification, container, false)
    }


    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        initUi(view)

        buttonResetPassword.setOnClickListener {
            navController?.navigate(R.id.action_emailVerificationFragment2_to_checkEmailFragment)
        }

        buttonBackToLogin.setOnClickListener {
            navController?.popBackStack(R.id.loginFragment, false)
        }

    }

    private fun initUi(view: View){

        emailEditText = view.findViewById(R.id.emailEditText)
        buttonResetPassword = view.findViewById(R.id.buttonResetPassword)
        buttonBackToLogin = view.findViewById(R.id.buttonBackToLoginVerification)

        navController = requireView().findNavController()
    }

}