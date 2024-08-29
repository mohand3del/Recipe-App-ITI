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

class NewPasswordFragment : Fragment() {

    // Declare lateinit properties for views


    private lateinit var newPasswordEditText: TextInputEditText
    private lateinit var confirmPasswordEditText: TextInputEditText
    private lateinit var resetPasswordButton: Button
    private lateinit var backToLoginTv: TextView
    private var navController: NavController? = null

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {

        return inflater.inflate(R.layout.fragment_new_password, container, false)
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        initUi(view)

        resetPasswordButton.setOnClickListener {
            backToLoginTv.callOnClick()
        }

        backToLoginTv.setOnClickListener {
            navController?.popBackStack(R.id.loginFragment, false)
        }

    }

    private fun initUi(view: View) {
        newPasswordEditText = view.findViewById(R.id.newPasswordEditText)
        confirmPasswordEditText = view.findViewById(R.id.confirmPasswordEditText)
        resetPasswordButton = view.findViewById(R.id.resetPasswordButton)
        backToLoginTv = view.findViewById(R.id.backToLoginTvNewPassword)


        navController = requireView().findNavController()
    }


}
