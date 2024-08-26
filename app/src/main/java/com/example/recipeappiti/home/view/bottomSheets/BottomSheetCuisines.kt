package com.example.recipeappiti.home.view.bottomSheets

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Button
import androidx.fragment.app.viewModels
import androidx.recyclerview.widget.GridLayoutManager
import androidx.recyclerview.widget.RecyclerView
import com.example.recipeappiti.R
import com.example.recipeappiti.auth.model.data.LocalDataSourceImpl
import com.example.recipeappiti.auth.repository.UserRepositoryImpl
import com.example.recipeappiti.core.model.local.UserDatabase
import com.example.recipeappiti.home.data.remote.RemoteGsonDataImpl
import com.example.recipeappiti.home.model.FailureReason
import com.example.recipeappiti.home.model.Response
import com.example.recipeappiti.home.repository.MealRepositoryImpl
import com.example.recipeappiti.home.view.adapter.AdapterRVCuisines
import com.example.recipeappiti.home.viewModel.BottomSheetCuisinesViewModel
import com.example.recipeappiti.home.viewModel.BottomSheetCuisinesViewModelFactory
import com.google.android.material.bottomsheet.BottomSheetDialogFragment

class BottomSheetCuisines : BottomSheetDialogFragment() {

    private val viewModel: BottomSheetCuisinesViewModel by viewModels {

        val remoteGsonDataSource = RemoteGsonDataImpl()

        val database = UserDatabase.getDatabaseInstance(requireContext())
        val userDao = database.userDao()
        val localDataSource = LocalDataSourceImpl(userDao)

        val mealRepository = MealRepositoryImpl(remoteGsonDataSource)
        val userRepository = UserRepositoryImpl(localDataSource)
        BottomSheetCuisinesViewModelFactory(userRepository, mealRepository)

    }

    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        val view = inflater.inflate(R.layout.bottom_sheet_cuisines, container, false)
        val btnDone: Button = view.findViewById(R.id.buttonDone)
        val recyclerviewCuisines = view.findViewById<RecyclerView>(R.id.recyclerviewCuisines)

        val gridLayoutManager = GridLayoutManager(requireContext(), 3)
        recyclerviewCuisines.layoutManager = gridLayoutManager

        viewModel.getAllCuisines()

        viewModel.allCuisines.observe(viewLifecycleOwner) { response ->

            when (response) {
                is Response.Loading -> {

                }

                is Response.Success -> {

                    val adapter = AdapterRVCuisines(response.data.meals)
                    recyclerviewCuisines.adapter = adapter

                    btnDone.setOnClickListener {

                        viewModel.setCuisines(adapter.getSelectedCuisines())

                        viewModel.setCuisinesData.observe(viewLifecycleOwner) { response ->

                            when (response) {

                                is Response.Loading -> {

                                }

                                is Response.Success -> {

                                    dismiss()

                                }

                                is Response.Failure -> {

                                }
                            }

                        }


                    }

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



        return view
    }

    companion object {
        const val TAG = "BottomSheetCuisines"
    }


}