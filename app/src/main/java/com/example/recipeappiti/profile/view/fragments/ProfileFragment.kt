package com.example.recipeappiti.profile.view.fragments

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.ImageView
import android.widget.TextView
import androidx.fragment.app.Fragment
import androidx.fragment.app.activityViewModels
import androidx.navigation.NavController
import androidx.navigation.fragment.findNavController
import com.example.recipeappiti.R
import com.example.recipeappiti.core.model.local.repository.UserRepositoryImpl
import com.example.recipeappiti.core.model.local.source.LocalDataSourceImpl
import com.example.recipeappiti.core.model.local.source.UserDatabase
import com.example.recipeappiti.core.model.remote.Response
import com.example.recipeappiti.main.viewModel.RecipeActivityViewModel
import com.example.recipeappiti.main.viewModel.RecipeActivityViewModelFactory
import com.example.recipeappiti.profile.view.bottomSheets.ProfileCuisinesSheet
import com.google.android.material.bottomnavigation.BottomNavigationView

class ProfileFragment : Fragment() {
    private val recipeViewModel: RecipeActivityViewModel by activityViewModels {
        val database = UserDatabase.getDatabaseInstance(requireContext())
        val userDao = database.userDao()
        val localDataSource = LocalDataSourceImpl(userDao)
        val userRepository = UserRepositoryImpl(localDataSource)
        RecipeActivityViewModelFactory(userRepository)
    }

    private lateinit var bottomNavigationView: BottomNavigationView
    private lateinit var navController: NavController
    private lateinit var userNameText: TextView
    private lateinit var userEmailText: TextView
    private lateinit var userEditCuisines: TextView
    private lateinit var profileBackBtn: ImageView

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        return inflater.inflate(R.layout.fragment_profile, container, false)
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        initViews(view)
        initObservers()
    }

    private fun initViews(view: View) {
        bottomNavigationView = requireActivity().findViewById(R.id.bottom_navigation)
        bottomNavigationView.visibility = View.GONE
        navController = findNavController()

        userNameText = view.findViewById(R.id.userNameProfile)
        userEmailText = view.findViewById(R.id.userEmailProfile)

        userEditCuisines = view.findViewById(R.id.userEditCuisines)

        userEditCuisines.setOnClickListener {
            val cuisinesSheet = ProfileCuisinesSheet()
            cuisinesSheet.show(requireActivity().supportFragmentManager, ProfileCuisinesSheet.TAG)
        }

        profileBackBtn = view.findViewById(R.id.profileBackBtn)
        profileBackBtn.setOnClickListener {
            navController.popBackStack()
        }
    }

    private fun initObservers() {
        recipeViewModel.userName.observe(viewLifecycleOwner) { response ->
            response as Response.Success
            userNameText.text = response.data
        }

        recipeViewModel.userEmail.observe(viewLifecycleOwner) { response ->
            response as Response.Success
            userEmailText.text = response.data
        }
    }
    override fun onDestroyView() {
        super.onDestroyView()
        bottomNavigationView.visibility = View.VISIBLE
    }
}