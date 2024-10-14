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
import androidx.fragment.app.activityViewModels
import androidx.lifecycle.viewModelScope
import androidx.navigation.NavController
import androidx.navigation.NavOptions
import androidx.navigation.fragment.findNavController
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
import com.example.recipeappiti.core.util.CreateMaterialAlertDialogBuilder.createMaterialAlertDialogBuilderOk
import com.example.recipeappiti.core.util.SystemChecks
import com.example.recipeappiti.core.viewmodel.DataViewModel
import com.example.recipeappiti.core.viewmodel.DataViewModelFactory
import com.example.recipeappiti.search.view.adapters.AdapterRVSearchMeals
import com.example.recipeappiti.search.view.bottomSheets.BottomSheetFilters
import com.example.recipeappiti.search.viewModel.SearchFragmentViewModel
import com.example.recipeappiti.search.viewModel.SearchFragmentViewModelFactory
import com.google.android.material.card.MaterialCardView
import kotlinx.coroutines.launch

class SearchFragment : Fragment() {

    private lateinit var searchBar: EditText
    private lateinit var recyclerviewSearch: RecyclerView
    private var navController: NavController? = null
    private lateinit var filterBtn: MaterialCardView
    private var favouriteState: Boolean? = null

    private val searchViewModel: SearchFragmentViewModel by activityViewModels {
        val remoteGsonDataSource = RemoteGsonDataImpl()
        val mealRepository = MealRepositoryImpl(remoteGsonDataSource)
        SearchFragmentViewModelFactory(mealRepository)
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

    private val navOptions = NavOptions.Builder()
        .setEnterAnim(R.anim.slide_in_right)
        .setPopExitAnim(R.anim.slide_out_right)
        .build()

    override fun onDestroyView() {
        super.onDestroyView()
        dataViewModel.updateSearchCategory(null)
        searchViewModel.updateCuisine(null)
        searchViewModel.updateCategory(null)
    }

    override fun onResume() {
        super.onResume()
        if (SystemChecks.isNetworkAvailable(requireContext())) {
            dataViewModel.categorySearch.observe(viewLifecycleOwner) { category ->

                if (category.isNullOrEmpty()) {
                    searchBar.requestFocus()
                    val inputMethodManager =
                        requireContext().getSystemService(Context.INPUT_METHOD_SERVICE) as InputMethodManager
                    inputMethodManager.showSoftInput(searchBar, InputMethodManager.SHOW_IMPLICIT)
                } else {
                    categoryMeals(category)
                }
            }
        }
    }

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        val view = inflater.inflate(R.layout.fragment_search, container, false)

        initViews(view)
        checkConnection()

        return view
    }

    private fun initViews(view: View) {
        searchBar = view.findViewById(R.id.searchBar)
        recyclerviewSearch = view.findViewById(R.id.recyclerviewSearch)
        recyclerviewSearch.layoutManager = GridLayoutManager(requireContext(), 2)
        navController =
            requireActivity().supportFragmentManager.findFragmentById(R.id.nav_host_fragment)
                ?.findNavController()

        filterBtn = view.findViewById(R.id.filterBtn)
    }

    private fun initObservers() {
        searchViewModel.chosenCuisine.observe(viewLifecycleOwner) { chosenCuisine ->
            chosenCuisine?.let { cuisinesMeals(it) }
        }

        searchViewModel.chosenCategory.observe(viewLifecycleOwner) { chosenCategory ->
            chosenCategory?.let { categoryMeals(it) }
        }

        searchViewModel.dataMeals.observe(viewLifecycleOwner) { response ->
            when (response) {
                is Response.Loading -> {}

                is Response.Success -> {
                    if (response.data.meals.isNullOrEmpty()) {
                        recyclerviewSearch.adapter = null
                    } else {
                        recyclerviewSearch.adapter = AdapterRVSearchMeals(
                            response.data.meals,
                            { id -> goToDetails(id) },
                            { id, isChange, onComplete ->
                                changeFavouriteState(id, isChange, onComplete)
                            }
                        )
                    }
                }

                is Response.Failure -> {
                    createFailureResponse(response, requireContext())
                }
            }
        }

        dataViewModel.isFavourite.observe(viewLifecycleOwner) { isFavourite ->
            favouriteState = isFavourite
        }
    }

    private fun initListeners() {
        val bottomSheetFilters = BottomSheetFilters()

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
                searchViewModel.searchByTitle(title)
            }
        })
    }

    private fun cuisinesMeals(area: String) {
        with(searchViewModel) {
            getCuisinesMeals(area)
            cuisineMeals.observe(viewLifecycleOwner) { response ->
                when (response) {
                    is Response.Loading -> {}

                    is Response.Success -> {
                        recyclerviewSearch.visibility = View.VISIBLE
                        recyclerviewSearch.adapter = AdapterRVSearchMeals(
                            response.data.meals,
                            { id -> goToDetails(id) },
                            { id, isChange, onComplete ->
                                changeFavouriteState(id, isChange, onComplete)
                            }
                        )
                    }

                    is Response.Failure -> {
                        createFailureResponse(response, requireContext())
                    }
                }
            }
        }
    }

    private fun categoryMeals(category: String) {
        with(searchViewModel) {
            getCategoryMeals(category)
            categoryMeals.observe(viewLifecycleOwner) { response ->
                when (response) {
                    is Response.Loading -> {}

                    is Response.Success -> {

                        recyclerviewSearch.visibility = View.VISIBLE
                        recyclerviewSearch.adapter = AdapterRVSearchMeals(
                            response.data.meals,
                            { id -> goToDetails(id) },
                            { id, isChange, onComplete ->
                                changeFavouriteState(id, isChange, onComplete)
                            }
                        )
                    }

                    is Response.Failure -> {
                        createFailureResponse(response, requireContext())
                    }
                }
            }
        }
    }

    private fun goToDetails(id: String) {
        dataViewModel.setItemDetails(id)

        navController?.navigate(R.id.recipeDetailFragment, null, navOptions)
    }

    private fun changeFavouriteState(
        recipeId: String,
        isChange: Boolean,
        onComplete: (Boolean) -> Unit
    ) {
        dataViewModel.viewModelScope.launch {
            dataViewModel.changeFavouriteState(recipeId, isChange)
        }.invokeOnCompletion {
            favouriteState?.let { state -> onComplete(state) }
        }
    }

    private fun checkConnection() {
        if (!SystemChecks.isNetworkAvailable(requireContext())) {
            createMaterialAlertDialogBuilderOk(
                requireContext(),
                "No Internet Connection",
                "Please check your internet connection and try again",
                "Retry",
            ) {
                checkConnection()
            }
        } else {
            initObservers()
            initListeners()
        }
    }
}
