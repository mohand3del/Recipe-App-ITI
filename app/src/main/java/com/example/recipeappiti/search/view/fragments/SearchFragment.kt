package com.example.recipeappiti.search.view.fragments

import android.content.Context
import android.os.Bundle
import android.text.Editable
import android.text.TextWatcher
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.view.inputmethod.InputMethodManager
import android.widget.EditText
import androidx.fragment.app.Fragment
import androidx.fragment.app.viewModels
import androidx.lifecycle.LiveData
import androidx.recyclerview.widget.GridLayoutManager
import androidx.recyclerview.widget.RecyclerView
import com.example.recipeappiti.R
import com.example.recipeappiti.core.model.util.CreateMaterialAlertDialogBuilder
import com.example.recipeappiti.home.data.remote.RemoteGsonDataImpl
import com.example.recipeappiti.home.model.FailureReason
import com.example.recipeappiti.home.model.Response
import com.example.recipeappiti.home.repository.MealRepositoryImpl
import com.example.recipeappiti.search.view.adapters.AdapterRVSearchMeals
import com.example.recipeappiti.search.view.bottomSheets.BottomSheetFilters
import com.example.recipeappiti.search.viewModel.SearchFragmentViewModel
import com.example.recipeappiti.search.viewModel.SearchFragmentViewModelFactory
import com.facebook.shimmer.ShimmerFrameLayout
import com.google.android.material.card.MaterialCardView

class SearchFragment : Fragment() {

    private lateinit var searchBar: EditText
    private lateinit var recyclerviewSearch: RecyclerView

    private val viewModel: SearchFragmentViewModel by viewModels {
        val remoteGsonDataSource = RemoteGsonDataImpl()
        val mealRepository = MealRepositoryImpl(remoteGsonDataSource)
        SearchFragmentViewModelFactory(mealRepository)
    }

    override fun onResume() {
        super.onResume()

        searchBar.requestFocus()

        val inputMethodManager = requireContext().getSystemService(Context.INPUT_METHOD_SERVICE) as InputMethodManager
        inputMethodManager.showSoftInput(searchBar, InputMethodManager.SHOW_IMPLICIT)

    }

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        val view = inflater.inflate(R.layout.fragment_search, container, false)

        searchBar = view.findViewById(R.id.searchBar)
        recyclerviewSearch = view.findViewById(R.id.recyclerviewSearch)
        recyclerviewSearch.layoutManager = GridLayoutManager(requireContext(), 2)

        val filterBtn: MaterialCardView = view.findViewById(R.id.filterBtn)

        val bottomSheetFilters = BottomSheetFilters({data-> cuisinesMeals(data) },
            {data-> categoryMeals(data) })

        filterBtn.setOnClickListener {

            bottomSheetFilters.show(
                requireActivity().supportFragmentManager,
                BottomSheetFilters.TAG
            )
        }

        searchBar.addTextChangedListener(object : TextWatcher {
            override fun afterTextChanged(s: Editable?) {}

            override fun beforeTextChanged(s: CharSequence?, start: Int, count: Int, after: Int) {}

            override fun onTextChanged(s: CharSequence?, start: Int, before: Int, count: Int) {
                val title = s.toString()
                viewModel.searchByTitle(title)
            }
        })

        viewModel.dataMeals.observe(viewLifecycleOwner) { response ->

            when (response) {
                is Response.Loading -> {

                }

                is Response.Success -> {

                    if (response.data.meals.isNullOrEmpty()){
                        recyclerviewSearch.adapter = null
                    }else
                        recyclerviewSearch.adapter = AdapterRVSearchMeals(response.data.meals)


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

        return view
    }

    private fun cuisinesMeals(area: String) {

        viewModel.getCuisinesMeals(area)

        observeResponse(
            viewModel.cuisineMeals, null, recyclerviewSearch,

            { data -> AdapterRVSearchMeals(data.meals) }, null
        )

    }

    private fun categoryMeals(category: String) {

        viewModel.getCategoryMeals(category)

        observeResponse(
            viewModel.categoryMeals, null, recyclerviewSearch,

            { data -> AdapterRVSearchMeals(data.meals) }, null
        )

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
