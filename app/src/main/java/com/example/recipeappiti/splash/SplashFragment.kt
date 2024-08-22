package com.example.recipeappiti.splash

import android.content.Context
import android.content.Intent
import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.fragment.app.Fragment
import androidx.navigation.fragment.findNavController
import com.example.recipeappiti.R
import com.example.recipeappiti.RecipeActivity

class SplashFragment : Fragment() {

    private var userId: Int = -1

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        return inflater.inflate(R.layout.fragment_splash, container, false)
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        val sharedPreferences =
            requireContext().getSharedPreferences("user_info", Context.MODE_PRIVATE)

        with(sharedPreferences) {
            userId = getInt("user_id", -1)
            if (userId == -1) {
                findNavController().navigate(R.id.action_splashFragment_to_loginFragment)
            } else {
                val intent = Intent(requireContext(), RecipeActivity::class.java)
                startActivity(intent)
                requireActivity().finish()
            }
        }
    }
}