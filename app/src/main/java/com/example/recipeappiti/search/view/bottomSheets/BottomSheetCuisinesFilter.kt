package com.example.recipeappiti.search.view.bottomSheets

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.fragment.app.viewModels
import androidx.lifecycle.LiveData
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import com.example.recipeappiti.R
import com.example.recipeappiti.core.model.util.CreateMaterialAlertDialogBuilder
import com.example.recipeappiti.home.data.remote.RemoteGsonDataImpl
import com.example.recipeappiti.home.model.FailureReason
import com.example.recipeappiti.home.model.Response
import com.example.recipeappiti.home.repository.MealRepositoryImpl
import com.example.recipeappiti.search.view.adapters.AdapterRVCuisinesFilters
import com.example.recipeappiti.search.viewModel.BottomSheetCuisinesFilterViewModel
import com.example.recipeappiti.search.viewModel.BottomSheetCuisinesFilterViewModelFactory
import com.facebook.shimmer.ShimmerFrameLayout
import com.google.android.material.bottomsheet.BottomSheetDialogFragment

class BottomSheetCuisinesFilter(

    private val doFilter: (String) -> Unit

) : BottomSheetDialogFragment() {

    private val viewModel: BottomSheetCuisinesFilterViewModel by viewModels {
        val remoteGsonDataSource = RemoteGsonDataImpl()
        val mealRepository = MealRepositoryImpl(remoteGsonDataSource)
        BottomSheetCuisinesFilterViewModelFactory(mealRepository)
    }

    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        val view = inflater.inflate(R.layout.bottom_sheet_cuisines_filters, container, false)

        val recyclerView: RecyclerView = view.findViewById(R.id.recyclerviewCuisinesFilter)

        recyclerView.layoutManager = LinearLayoutManager(requireContext())

        viewModel.getCuisines()

        observeResponse(viewModel.cuisines, null, recyclerView,
            { data ->
                AdapterRVCuisinesFilters(data.meals) { data ->
                    doFilter(
                        data
                    )
                    dismiss()
                }
            }, null
        )

        return view
    }

    companion object {
        const val TAG = "BottomSheetCuisinesFilter"
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