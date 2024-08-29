package com.example.recipeappiti.auth.newPassword

import android.os.Bundle
import android.text.Editable
import android.text.TextWatcher
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Button
import android.widget.EditText
import android.widget.TextView
import androidx.fragment.app.Fragment
import androidx.navigation.NavController
import androidx.navigation.findNavController
import com.example.recipeappiti.R

class CheckEmailFragment : Fragment() {

    private lateinit var pinDigitEditText1: EditText
    private lateinit var pinDigitEditText2: EditText
    private lateinit var pinDigitEditText3: EditText
    private lateinit var pinDigitEditText4: EditText
    private lateinit var continueButton: Button
    private lateinit var clickHereTv: TextView
    private lateinit var backToLoginTv: TextView
    private var navController: NavController? = null

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {

        return inflater.inflate(R.layout.fragment_check_email, container, false)
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        initUi(view)

        continueButton.setOnClickListener {
            navController?.navigate(R.id.action_checkEmailFragment_to_newPasswordFragment)
        }

        backToLoginTv.setOnClickListener {
            navController?.popBackStack(R.id.loginFragment, false)
        }

        pinDigitEditText1.addTextChangedListener(object : TextWatcher {
            override fun beforeTextChanged(s: CharSequence?, start: Int, count: Int, after: Int) {}
            override fun onTextChanged(s: CharSequence?, start: Int, before: Int, count: Int) {}
            override fun afterTextChanged(s: Editable?) {

            }
        })


    }

    private fun initUi(view: View) {
        pinDigitEditText1 = view.findViewById(R.id.pinDigitEditText1)
        pinDigitEditText2 = view.findViewById(R.id.pinDigitEditText2)
        pinDigitEditText3 = view.findViewById(R.id.pinDigitEditText3)
        pinDigitEditText4 = view.findViewById(R.id.pinDigitEditText4)
        continueButton = view.findViewById(R.id.continueButton)
        clickHereTv = view.findViewById(R.id.clickHereTv)
        backToLoginTv = view.findViewById(R.id.backToLoginTvFromReset)

        navController = requireView().findNavController()

    }

}