package com.example.recipeappiti.home.view.fragments

import android.os.Bundle
import android.view.LayoutInflater
import android.view.MenuItem
import android.view.View
import android.view.ViewGroup
import android.widget.Button
import android.widget.EditText
import android.widget.ImageView
import android.widget.PopupMenu
import android.widget.TextView
import androidx.constraintlayout.widget.ConstraintLayout
import androidx.core.view.GravityCompat
import androidx.drawerlayout.widget.DrawerLayout
import androidx.fragment.app.Fragment
import androidx.fragment.app.activityViewModels
import androidx.fragment.app.viewModels
import androidx.navigation.NavController
import androidx.navigation.fragment.findNavController
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import com.example.recipeappiti.R
import com.example.recipeappiti.auth.model.data.LocalDataSourceImpl
import com.example.recipeappiti.auth.repository.UserRepositoryImpl
import com.example.recipeappiti.core.model.local.UserDatabase
import com.example.recipeappiti.core.model.util.CreateMaterialAlertDialogBuilder
import com.example.recipeappiti.home.data.remote.RemoteGsonDataImpl
import com.example.recipeappiti.home.model.FailureReason
import com.example.recipeappiti.home.model.Response
import com.example.recipeappiti.home.repository.MealRepositoryImpl
import com.example.recipeappiti.home.view.adapter.AdapterRVCategories
import com.example.recipeappiti.home.view.adapter.AdapterRVItemMeal
import com.example.recipeappiti.home.view.bottomSheets.BottomSheetCuisines
import com.example.recipeappiti.home.viewModel.HomeFragmentViewModel
import com.example.recipeappiti.home.viewModel.HomeFragmentViewModelFactory
import com.example.recipeappiti.main.viewModel.RecipeActivityViewModel
import com.example.recipeappiti.main.viewModel.RecipeActivityViewModelFactory
import com.facebook.shimmer.ShimmerFrameLayout
import com.google.android.material.bottomnavigation.BottomNavigationView
import com.google.android.material.card.MaterialCardView


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

    private val sharedViewModel: RecipeActivityViewModel by activityViewModels {

        val database = UserDatabase.getDatabaseInstance(requireContext())
        val userDao = database.userDao()
        val localDataSource = LocalDataSourceImpl(userDao)

        val userRepository = UserRepositoryImpl(localDataSource)

        RecipeActivityViewModelFactory(userRepository)

    }

    private lateinit var recyclerViewCategories: RecyclerView
    private lateinit var recyclerViewRecommendations: RecyclerView
    private lateinit var recyclerViewCuisine: RecyclerView
    private lateinit var shimmerCategories: ShimmerFrameLayout
    private lateinit var shimmerRecommendations: ShimmerFrameLayout
    private lateinit var shimmerMealByArea: ShimmerFrameLayout
    private lateinit var btnDrawer: ImageView
    private lateinit var textViewCuisines: TextView
    private lateinit var constraintLayoutGold: ConstraintLayout
    private lateinit var cardViewFreeTrial: MaterialCardView
    private lateinit var shimmerGold: ShimmerFrameLayout
    private lateinit var recyclerViewGold: RecyclerView
    private lateinit var btnFreeTrial: Button
    private lateinit var btnCuisines: MaterialCardView
    private lateinit var popup: PopupMenu
    private lateinit var searchBar_home: EditText
    private lateinit var bottomNavigationView: BottomNavigationView
    private lateinit var drawer: DrawerLayout
    private var navController : NavController? = null

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {

        val view = inflater.inflate(R.layout.fragment_home, container, false)

        initializeUi(view)

        initUi()

        searchBar_home.setOnClickListener {

        }

        popup.setOnMenuItemClickListener { item: MenuItem ->

            homeFavCuisines(item.title.toString())

            true
        }

        btnFreeTrial.setOnClickListener {

            CreateMaterialAlertDialogBuilder.createMaterialAlertDialogBuilderOkCancel(
                requireContext(),
                "Start Free Trial",
                "Would you like to start a free trial to access premium recipes?",
                "Start Free Trial",
                "Cancel"
            ) {
                cardViewFreeTrial.visibility = View.GONE

                constraintLayoutGold.visibility = View.VISIBLE
            }

        }


        btnCuisines.setOnClickListener {
            popup.show()
        }

        return view
    }


    private fun initUi() {

        with(viewModel) {

            getCategories()
            dataCategories.observe(viewLifecycleOwner) { response ->

                when (response) {
                    is Response.Loading -> {
                        shimmerCategories.startShimmer()
                    }

                    is Response.Success -> {
                        shimmerCategories.stopShimmer()
                        shimmerCategories.visibility = View.GONE
                        val adapter =
                            AdapterRVCategories(response.data.categories) { name -> goToSearchCategories(name) }
                        recyclerViewCategories.adapter = adapter
                    }

                    is Response.Failure -> {
                        failureResponse(response)
                    }
                }

            }
        }

        with(viewModel) {

            getSomeRandomMeal(10)
            someRandomMeal.observe(viewLifecycleOwner) { response ->

                when (response) {
                    is Response.Loading -> {
                        shimmerRecommendations.startShimmer()
                    }

                    is Response.Success -> {
                        shimmerRecommendations.stopShimmer()
                        shimmerRecommendations.visibility = View.GONE
                        val adapter = AdapterRVItemMeal(response.data) { id -> goToDetails(id) }
                        recyclerViewRecommendations.adapter = adapter
                    }

                    is Response.Failure -> {
                        failureResponse(response)
                    }
                }

            }
        }

        with(viewModel) {

            getSomeRandomMeal(5)
            someRandomMeal.observe(viewLifecycleOwner) { response ->

                when (response) {
                    is Response.Loading -> {
                        shimmerGold.startShimmer()
                    }

                    is Response.Success -> {
                        shimmerGold.stopShimmer()
                        shimmerGold.visibility = View.GONE
                        val adapter = AdapterRVItemMeal(response.data) { id -> goToDetails(id) }
                        recyclerViewCuisine.adapter = adapter
                    }

                    is Response.Failure -> {
                        failureResponse(response)
                    }
                }

            }
        }

        viewModel.getUserCuisines()

        with(viewModel) {
            getUserCuisines()
            userCuisines.observe(viewLifecycleOwner) { response ->

                when (response) {
                    is Response.Loading -> {
                        shimmerMealByArea.startShimmer()
                    }

                    is Response.Success -> {
                        val data = response.data

                        if (!data.isNullOrEmpty() && data[0].isNotEmpty()) {
                            homeFavCuisines(data[0])

                            data.forEachIndexed { index, item ->
                                popup.menu.add(0, index, 0, item)
                            }

                        } else {
                            val bottomSheetCuisines = BottomSheetCuisines()
                            bottomSheetCuisines.show(
                                requireActivity().supportFragmentManager,
                                BottomSheetCuisines.TAG
                            )
                            //TODO refresh after choose cuisines
                        }
                    }

                    is Response.Failure -> {
                        failureResponse(response)
                    }
                }

            }
        }

    }

    private fun initializeUi(view: View) {
        recyclerViewCategories = view.findViewById(R.id.recyclerview_categories)
        recyclerViewRecommendations = view.findViewById(R.id.recyclerview_recommendations)
        recyclerViewCuisine = view.findViewById(R.id.recyclerview_meal_by_fav_cuisine)
        recyclerViewGold = view.findViewById(R.id.recyclerviewGold)

        shimmerCategories = view.findViewById(R.id.shimmer_categories)
        shimmerRecommendations = view.findViewById(R.id.shimmer_recommendations)
        shimmerMealByArea = view.findViewById(R.id.shimmer_meal_by_fav_cuisine)
        shimmerGold = view.findViewById(R.id.shimmerGold)

        constraintLayoutGold = view.findViewById(R.id.constraintlayoutGold)
        cardViewFreeTrial = view.findViewById(R.id.cardViewFreeTrial)

        btnFreeTrial = view.findViewById(R.id.btnFreeTrial)

        btnDrawer = view.findViewById(R.id.btnHomeDrawer)

        textViewCuisines = view.findViewById(R.id.textViewCuisines)

        searchBar_home = view.findViewById(R.id.searchBar_home)

        recyclerViewCategories.layoutManager =
            LinearLayoutManager(requireContext(), LinearLayoutManager.HORIZONTAL, false)
        recyclerViewRecommendations.layoutManager =
            LinearLayoutManager(requireContext(), LinearLayoutManager.HORIZONTAL, false)
        recyclerViewCuisine.layoutManager =
            LinearLayoutManager(requireContext(), LinearLayoutManager.HORIZONTAL, false)
        recyclerViewGold.layoutManager =
            LinearLayoutManager(requireContext(), LinearLayoutManager.HORIZONTAL, false)

        btnCuisines = view.findViewById(R.id.btnCuisines)

        popup = PopupMenu(requireContext(), btnCuisines)

        drawer = requireActivity().findViewById(R.id.drawer)

        bottomNavigationView = requireActivity().findViewById(R.id.bottom_navigation)

        btnDrawer.setOnClickListener {

            drawer.openDrawer(GravityCompat.START)

        }

        navController =  requireActivity().supportFragmentManager.findFragmentById(R.id.nav_host_fragment)?.findNavController()
    }

    private fun homeFavCuisines(
        cuisine: String
    ) {
        textViewCuisines.text = "$cuisine Recipes for you"

        viewModel.getFilteredMealsByAreas(cuisine)

        viewModel.filteredMealsByAreas.observe(viewLifecycleOwner) { response ->
            when (response) {
                is Response.Loading -> {
                    shimmerMealByArea.startShimmer()
                }

                is Response.Success -> {
                    shimmerMealByArea.stopShimmer()
                    shimmerMealByArea.visibility = View.GONE
                    val adapter = AdapterRVItemMeal(response.data.meals) { id -> goToDetails(id) }
                    recyclerViewCuisine.adapter = adapter
                }

                is Response.Failure -> {
                    failureResponse(response)
                }
            }
        }
    }


    private fun failureResponse(response: Response.Failure) {
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

    private fun goToDetails(id: String) {

        sharedViewModel.setItemDetails(id)

        navController?.navigate(R.id.recipeDetailFragment)

    }

    private fun goToSearchCategories(name: String) {

        sharedViewModel.updateSearchCategory(name)

        sharedViewModel.navigateTo(R.id.action_search)

    }

}