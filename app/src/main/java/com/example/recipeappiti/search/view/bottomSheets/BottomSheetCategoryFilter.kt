package com.example.recipeappiti.search.view.bottomSheets

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.fragment.app.activityViewModels
import androidx.fragment.app.viewModels
import androidx.lifecycle.LiveData
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import com.example.recipeappiti.R
import com.example.recipeappiti.core.util.CreateMaterialAlertDialogBuilder
import com.example.recipeappiti.core.model.remote.source.RemoteGsonDataImpl
import com.example.recipeappiti.core.model.remote.FailureReason
import com.example.recipeappiti.core.model.remote.Response
import com.example.recipeappiti.core.model.remote.repository.MealRepositoryImpl
import com.example.recipeappiti.search.view.adapters.AdapterRVCategoryFilters
import com.example.recipeappiti.search.viewModel.BottomSheetCategoriesFilterViewModel
import com.example.recipeappiti.search.viewModel.BottomSheetCategoriesFilterViewModelFactory
import com.example.recipeappiti.search.viewModel.SearchFragmentViewModel
import com.example.recipeappiti.search.viewModel.SearchFragmentViewModelFactory
import com.facebook.shimmer.ShimmerFrameLayout
import com.google.android.material.bottomsheet.BottomSheetDialogFragment

class BottomSheetCategoryFilter: BottomSheetDialogFragment() {

    private val viewModel: BottomSheetCategoriesFilterViewModel by viewModels {
        val remoteGsonDataSource = RemoteGsonDataImpl()
        val mealRepository = MealRepositoryImpl(remoteGsonDataSource)
        BottomSheetCategoriesFilterViewModelFactory(mealRepository)
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
        val view = inflater.inflate(R.layout.bottom_sheet_categories_filters, container, false)

        val recyclerView: RecyclerView = view.findViewById(R.id.recyclerviewCategoriesFilter)

        recyclerView.layoutManager = LinearLayoutManager(requireContext())

        viewModel.getCategories()

        observeResponse(viewModel.categories, null, recyclerView,
            { data -> AdapterRVCategoryFilters(data.categories){data->
                searchViewModel.updateCategory(data)
                dismiss()
            } }, null
        )

        return view
    }

    companion object {
        const val TAG = "BottomSheetCategoryFilter"
    }

    private fun <T> observeResponse(
        liveData: LiveData<Response<T>>,
        shimmerView: ShimmerFrameLayout?,
        recyclerView: RecyclerView?,
        adapterFactory: ((T) -> RecyclerView.Adapter<*>)?,
        successFun: ((T) -> Unit)?
    ) {

        liveData.observe(viewLifecycleOwner) { response ->

            when (response) {
                is Response.Loading -> {
                    shimmerView?.startShimmer()
                }

                is Response.Success -> {

                    if (recyclerView != null && adapterFactory != null) {

                        shimmerView?.stopShimmer()
                        shimmerView?.visibility = View.GONE
                        recyclerView.visibility = View.VISIBLE
                        recyclerView.adapter = adapterFactory(response.data)

                    }
                    if (successFun != null)
                        successFun(response.data)


                }

                is Response.Failure -> {
                    when (val failureReason = response.reason) {
                        is FailureReason.NoInternet -> {
                            // Show no internet connection message
                            CreateMaterialAlertDialogBuilder.createMaterialAlertDialogBuilderOkCancel(
                                requireContext(),
                                title = "No Internet Connection",
                                message = "Please check your internet connection and try again.",
                                positiveBtnMsg = "Try again",
                                negativeBtnMsg = "Cancel"
                            ) {
                                //TODO Optionally, define any action to take after the dialog is dismissed
                            }
                        }

                        is FailureReason.UnknownError -> {
                            val errorMessage = failureReason.error
                            CreateMaterialAlertDialogBuilder.createMaterialAlertDialogBuilderOk(
                                requireContext(),
                                title = "Unknown Error",
                                message = "An unknown error occurred: $errorMessage",
                                positiveBtnMsg = "OK"
                            ) {

                            }
                        }
                    }
                }
            }

        }

    }


}