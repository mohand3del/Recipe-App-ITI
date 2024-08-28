package com.example.recipeappiti.splash.view

import android.content.Intent
import android.os.Bundle
import android.os.Handler
import android.os.Looper
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.fragment.app.Fragment
import androidx.fragment.app.viewModels
import androidx.navigation.fragment.findNavController
import com.example.recipeappiti.R
import com.example.recipeappiti.core.model.local.source.LocalDataSourceImpl
import com.example.recipeappiti.core.model.local.repository.UserRepositoryImpl
import com.example.recipeappiti.core.model.local.source.UserDatabase
import com.example.recipeappiti.core.model.remote.FailureReason
import com.example.recipeappiti.core.model.remote.Response
import com.example.recipeappiti.main.view.RecipeActivity
import com.example.recipeappiti.splash.viewModel.SplashViewModel
import com.example.recipeappiti.splash.viewModel.SplashViewModelFactory

class SplashFragment : Fragment() {

    private val viewModel: SplashViewModel by viewModels {

        val database = UserDatabase.getDatabaseInstance(requireContext())
        val userDao = database.userDao()
        val localDataSource = LocalDataSourceImpl(userDao)
        val userRepository = UserRepositoryImpl(localDataSource)
        SplashViewModelFactory(userRepository)

    }

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        return inflater.inflate(R.layout.fragment_splash, container, false)
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        viewModel.getLoggedInUser()

        viewModel.loggedInUser.observe(viewLifecycleOwner) { response ->
            when (response) {
                is Response.Loading -> {

                }

                is Response.Success -> {


                    Handler(Looper.getMainLooper()).postDelayed({
                        if (response.data) {
                            val intent = Intent(requireContext(), RecipeActivity::class.java)
                            startActivity(intent)
                            requireActivity().finish()
                        } else {
                            findNavController().navigate(R.id.action_splashFragment_to_loginFragment)
                        }
                    }, 3000)
                }

                is Response.Failure -> {
                    when (val failureReason = response.reason) {
                        is FailureReason.NoInternet -> {

                        }

                        is FailureReason.UnknownError -> {
                            val errorMessage = failureReason.error

                        }
                    }
                }
            }
        }
    }
}