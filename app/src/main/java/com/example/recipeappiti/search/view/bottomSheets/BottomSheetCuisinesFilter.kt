package com.example.recipeappiti.search.view.bottomSheets

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.ProgressBar
import androidx.fragment.app.activityViewModels
import androidx.fragment.app.viewModels
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import com.example.recipeappiti.R
import com.example.recipeappiti.core.model.remote.Response
import com.example.recipeappiti.core.model.remote.repository.MealRepositoryImpl
import com.example.recipeappiti.core.model.remote.source.RemoteGsonDataImpl
import com.example.recipeappiti.core.util.CreateMaterialAlertDialogBuilder.createFailureResponse
import com.example.recipeappiti.search.view.adapters.AdapterRVCuisinesFilters
import com.example.recipeappiti.search.viewModel.BottomSheetCuisinesFilterViewModel
import com.example.recipeappiti.search.viewModel.BottomSheetCuisinesFilterViewModelFactory
import com.example.recipeappiti.search.viewModel.SearchFragmentViewModel
import com.example.recipeappiti.search.viewModel.SearchFragmentViewModelFactory
import com.google.android.material.bottomsheet.BottomSheetDialogFragment

class BottomSheetCuisinesFilter : BottomSheetDialogFragment() {

    private val viewModel: BottomSheetCuisinesFilterViewModel by viewModels {
        val remoteGsonDataSource = RemoteGsonDataImpl()
        val mealRepository = MealRepositoryImpl(remoteGsonDataSource)
        BottomSheetCuisinesFilterViewModelFactory(mealRepository)
    }

    private val searchViewModel: SearchFragmentViewModel by activityViewModels {
        val remoteGsonDataSource = RemoteGsonDataImpl()
        val mealRepository = MealRepositoryImpl(remoteGsonDataSource)
        SearchFragmentViewModelFactory(mealRepository)
    }

    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        val view = inflater.inflate(R.layout.bottom_sheet_cuisines_filters, container, false)

        val recyclerView: RecyclerView = view.findViewById(R.id.recyclerviewCuisinesFilter)
        val progressBar: ProgressBar = view.findViewById(R.id.progressBarBottomSheetCuisinesFilter)

        recyclerView.layoutManager = LinearLayoutManager(requireContext())

        viewModel.getCuisines()

        viewModel.cuisines.observe(viewLifecycleOwner) { response ->

            when (response) {
                is Response.Loading -> {
                    progressBar.visibility = View.VISIBLE
                }

                is Response.Success -> {
                    val adapter = AdapterRVCuisinesFilters(response.data.meals) { data ->
                        searchViewModel.updateCuisine(data)
                        dismiss()
                    }
                    progressBar.visibility = View.GONE
                    recyclerView.adapter = adapter
                }

                is Response.Failure -> {
                    createFailureResponse(response, requireContext())
                }
            }

        }

        return view
    }

    companion object {
        const val TAG = "BottomSheetCuisinesFilter"
    }

}