package com.example.recipeappiti.home.view.fragments

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Toast
import androidx.fragment.app.Fragment
import androidx.fragment.app.viewModels
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import com.example.recipeappiti.R
import com.example.recipeappiti.auth.model.data.LocalDataSourceImpl
import com.example.recipeappiti.auth.repository.UserRepositoryImpl
import com.example.recipeappiti.home.model.FailureReason
import com.example.recipeappiti.home.model.Response
import com.example.recipeappiti.core.model.local.UserDatabase
import com.example.recipeappiti.home.repository.MealRepositoryImpl
import com.example.recipeappiti.home.data.remote.RemoteGsonDataImpl
import com.example.recipeappiti.home.view.adapter.AdapterRVCategories
import com.example.recipeappiti.home.view.adapter.AdapterRVItemMeal
import com.example.recipeappiti.home.viewModel.HomeFragmentViewModel
import com.example.recipeappiti.home.viewModel.HomeFragmentViewModelFactory
import com.facebook.shimmer.ShimmerFrameLayout


class HomeFragment : Fragment() {


    private val viewModel: HomeFragmentViewModel by viewModels {
        val remoteGsonDataSource = RemoteGsonDataImpl()

        val database = UserDatabase.getDatabaseInstance(requireContext())
        val userDao = database.userDao()
        val localDataSource = LocalDataSourceImpl(userDao)

        val mealRepository = MealRepositoryImpl(remoteGsonDataSource)
        val userRepository = UserRepositoryImpl(localDataSource)

        HomeFragmentViewModelFactory(mealRepository, userRepository)
    }

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {

        val view = inflater.inflate(R.layout.fragment_home, container, false)

        val recyclerViewCategories = view.findViewById<RecyclerView>(R.id.recyclerview_categories)

        recyclerViewCategories.layoutManager =
            LinearLayoutManager(requireContext(), LinearLayoutManager.HORIZONTAL, false)

        val recyclerViewRecommendations =
            view.findViewById<RecyclerView>(R.id.recyclerview_recommendations)
        recyclerViewRecommendations.layoutManager =
            LinearLayoutManager(requireContext(), LinearLayoutManager.HORIZONTAL, false)

        val recyclerViewArea = view.findViewById<RecyclerView>(R.id.recyclerview_meal_by_area)
        recyclerViewArea.layoutManager =
            LinearLayoutManager(requireContext(), LinearLayoutManager.HORIZONTAL, false)

        val shimmerCategories: ShimmerFrameLayout = view.findViewById(R.id.shimmer_categories)
        val shimmerRecommendations: ShimmerFrameLayout =
            view.findViewById(R.id.shimmer_recommendations)
        val shimmerMealByArea: ShimmerFrameLayout = view.findViewById(R.id.shimmer_meal_by_area)

        viewModel.getCategories()

        viewModel.dataCategories.observe(viewLifecycleOwner) { response ->
            when (response) {
                is Response.Loading -> {
                    // Show loading indicator
                    shimmerCategories.startShimmer()
                }

                is Response.Success -> {
                    // Update UI with the categories
                    shimmerCategories.stopShimmer()
                    shimmerCategories.visibility = View.GONE
                    val categories = response.data
                    val adapter = AdapterRVCategories(categories.categories)
                    recyclerViewCategories.adapter = adapter

                }

                is Response.Failure -> {
                    when (val failureReason = response.reason) {
                        is FailureReason.NoInternet -> {
                            // Show no internet connection message
                        }

                        is FailureReason.UnknownError -> {
                            // Show unknown error message with the error details
                            val errorMessage = failureReason.error

                        }
                    }
                }
            }
        }

        viewModel.getSomeRandomMeal(10)

        viewModel.someRandomMeal.observe(viewLifecycleOwner) { response ->

            when (response) {
                is Response.Loading -> {
                    shimmerRecommendations.startShimmer()
                }

                is Response.Success -> {

                    shimmerRecommendations.stopShimmer()
                    shimmerRecommendations.visibility = View.GONE
                    val adapter = AdapterRVItemMeal(response.data)
                    recyclerViewRecommendations.adapter = adapter

                }

                is Response.Failure -> {

                    when (val failureReason = response.reason) {
                        is FailureReason.NoInternet -> {
                            // Show no internet connection message
                        }

                        is FailureReason.UnknownError -> {
                            // Show unknown error message with the error details
                            val errorMessage = failureReason.error

                        }
                    }
                }
            }
        }

        //viewModel.getCuisine("ibrahim@gmail.com")

//        viewModel.userCuisine.observe(viewLifecycleOwner) { response ->
//
//            when (response) {
//                is Response.Loading -> {
//                    shimmerMealByArea.startShimmer()
//                }
//
//                is Response.Success -> {
//
//                    if (response.data != null) {
//                        viewModel.getFilteredMealsByAreas(response.data)
//                        viewModel.filteredMealsByAreas.observe(viewLifecycleOwner) { response ->
//
//                            when (response) {
//                                is Response.Loading -> {
//                                    //shimmerMealByArea.startShimmer()
//                                }
//
//                                is Response.Success -> {
//
//                                    shimmerMealByArea.stopShimmer()
//                                    shimmerMealByArea.visibility = View.GONE
//                                    val adapter = AdapterRVItemMeal(response.data.meals)
//                                    recyclerViewArea.adapter = adapter
//
//                                }
//
//                                is Response.Failure -> {
//
//                                    when (val failureReason = response.reason) {
//                                        is FailureReason.NoInternet -> {
//                                            // Show no internet connection message
//                                        }
//
//                                        is FailureReason.UnknownError -> {
//                                            // Show unknown error message with the error details
//                                            val errorMessage = failureReason.error
//
//                                        }
//                                    }
//                                }
//                            }
//                        }
//                    } else {
//                        //show null data message
//                    }
//
//                }
//
//                is Response.Failure -> {
//
//                    Toast.makeText(requireContext(), "Error", Toast.LENGTH_LONG).show()
//                    when (val failureReason = response.reason) {
//                        is FailureReason.NoInternet -> {
//                            // Show no internet connection message
//                        }
//
//                        is FailureReason.UnknownError -> {
//                            // Show unknown error message with the error details
//                            val errorMessage = failureReason.error
//
//                        }
//                    }
//                }
//            }
//        }

        return view
    }
}