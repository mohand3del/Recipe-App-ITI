package com.example.recipeappiti.profile.view.bottomSheets

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Button
import android.widget.ProgressBar
import androidx.fragment.app.activityViewModels
import androidx.fragment.app.viewModels
import androidx.recyclerview.widget.GridLayoutManager
import androidx.recyclerview.widget.RecyclerView
import com.example.recipeappiti.R
import com.example.recipeappiti.core.model.local.repository.UserRepositoryImpl
import com.example.recipeappiti.core.model.local.source.LocalDataSourceImpl
import com.example.recipeappiti.core.model.local.source.UserDatabase
import com.example.recipeappiti.core.model.remote.Response
import com.example.recipeappiti.core.model.remote.repository.MealRepositoryImpl
import com.example.recipeappiti.core.model.remote.source.RemoteGsonDataImpl
import com.example.recipeappiti.core.util.CreateMaterialAlertDialogBuilder.createFailureResponse
import com.example.recipeappiti.core.viewmodel.DataViewModel
import com.example.recipeappiti.core.viewmodel.DataViewModelFactory
import com.example.recipeappiti.home.viewModel.BottomSheetCuisinesViewModel
import com.example.recipeappiti.home.viewModel.BottomSheetCuisinesViewModelFactory
import com.example.recipeappiti.profile.view.adapters.AdapterRVProfileCuisines
import com.google.android.material.bottomsheet.BottomSheetDialogFragment

class ProfileCuisinesSheet : BottomSheetDialogFragment() {
    private val sheetViewModel: BottomSheetCuisinesViewModel by viewModels {
        val remoteGsonDataSource = RemoteGsonDataImpl()
        val database = UserDatabase.getDatabaseInstance(requireContext())
        val userDao = database.userDao()
        val localDataSource = LocalDataSourceImpl(userDao)
        val mealRepository = MealRepositoryImpl(remoteGsonDataSource)
        val userRepository = UserRepositoryImpl(localDataSource)
        BottomSheetCuisinesViewModelFactory(userRepository, mealRepository)
    }

    private val dataViewModel: DataViewModel by activityViewModels {
        val userRepository = UserRepositoryImpl(
            LocalDataSourceImpl(
                UserDatabase.getDatabaseInstance(requireContext()).userDao()
            )
        )
        val mealRepository = MealRepositoryImpl(RemoteGsonDataImpl())
        DataViewModelFactory(userRepository, mealRepository)
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
        val progressBar: ProgressBar = view.findViewById(R.id.progressBarBottomSheetCuisines)

        recyclerviewCuisines.layoutManager = gridLayoutManager

        sheetViewModel.getAllCuisines()

        sheetViewModel.allCuisines.observe(viewLifecycleOwner) { response ->
            when (response) {
                is Response.Loading -> {
                    progressBar.visibility = View.VISIBLE
                }

                is Response.Success -> {
                    val adapter = AdapterRVProfileCuisines(response.data.meals, { lastCuisine ->
                        dataViewModel.updateMainCuisine(lastCuisine)
                    }, dataViewModel.cuisinesData.value)
                    progressBar.visibility = View.GONE
                    recyclerviewCuisines.adapter = adapter
                    btnDone.setOnClickListener {
                        dataViewModel.setCuisines(adapter.getSelectedCuisines())
                        dismiss()
                    }
                }

                is Response.Failure -> {
                    createFailureResponse(response, requireContext())
                }
            }
        }
        return view
    }

    companion object { const val TAG = "BottomSheetCuisines" }
}