package com.example.recipeappiti.home.view.fragments

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Button
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
import androidx.navigation.NavOptions
import androidx.navigation.fragment.findNavController
import androidx.recyclerview.widget.LinearLayoutManager
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
import com.example.recipeappiti.core.util.CreateMaterialAlertDialogBuilder.createMaterialAlertDialogBuilderOkCancel
import com.example.recipeappiti.core.util.SystemChecks
import com.example.recipeappiti.core.viewmodel.DataViewModel
import com.example.recipeappiti.core.viewmodel.DataViewModelFactory
import com.example.recipeappiti.home.view.adapter.AdapterRVCategories
import com.example.recipeappiti.home.view.adapter.AdapterRVItemMeal
import com.example.recipeappiti.home.view.bottomSheets.BottomSheetCuisines
import com.example.recipeappiti.home.viewModel.HomeFragmentViewModel
import com.example.recipeappiti.home.viewModel.HomeFragmentViewModelFactory
import com.example.recipeappiti.main.viewModel.RecipeActivityViewModel
import com.example.recipeappiti.main.viewModel.RecipeActivityViewModelFactory
import com.facebook.shimmer.ShimmerFrameLayout
import com.google.android.material.card.MaterialCardView


class HomeFragment : Fragment() {

    private val homeViewModel: HomeFragmentViewModel by viewModels {
        val remoteGsonDataSource = RemoteGsonDataImpl()
        val database = UserDatabase.getDatabaseInstance(requireContext())
        val userDao = database.userDao()
        val localDataSource = LocalDataSourceImpl(userDao)

        val mealRepository = MealRepositoryImpl(remoteGsonDataSource)
        val userRepository = UserRepositoryImpl(localDataSource)
        HomeFragmentViewModelFactory(mealRepository, userRepository)
    }

    private val recipeViewModel: RecipeActivityViewModel by activityViewModels {
        val database = UserDatabase.getDatabaseInstance(requireContext())
        val userDao = database.userDao()
        val localDataSource = LocalDataSourceImpl(userDao)
        val userRepository = UserRepositoryImpl(localDataSource)
        RecipeActivityViewModelFactory(userRepository)
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

    private lateinit var recyclerViewCategories: RecyclerView
    private lateinit var recyclerViewRecommendations: RecyclerView
    private lateinit var recyclerViewCuisine: RecyclerView
    private lateinit var shimmerCategories: ShimmerFrameLayout
    private lateinit var shimmerRecommendations: ShimmerFrameLayout
    private lateinit var shimmerCuisine: ShimmerFrameLayout
    private lateinit var btnDrawer: ImageView
    private lateinit var textViewCuisines: TextView
    private lateinit var constraintLayoutGold: ConstraintLayout
    private lateinit var cardViewFreeTrial: MaterialCardView
    private lateinit var shimmerGold: ShimmerFrameLayout
    private lateinit var recyclerViewGold: RecyclerView
    private lateinit var btnFreeTrial: Button
    private lateinit var btnCuisines: MaterialCardView
    private lateinit var popup: PopupMenu
    private lateinit var drawer: DrawerLayout
    private lateinit var searchBarHome: TextView

    private val navOptions = NavOptions.Builder()
        .setEnterAnim(R.anim.slide_in_right)
        .setPopExitAnim(R.anim.slide_out_right)
        .build()

    private var navController: NavController? = null
    private var chosenCuisine: String? = null

    private lateinit var categoriesAdapter: AdapterRVCategories
    private lateinit var recommendationsAdapter: AdapterRVItemMeal
    private lateinit var cuisineAdapter: AdapterRVItemMeal
    private lateinit var goldAdapter: AdapterRVItemMeal

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        val view = inflater.inflate(R.layout.fragment_home, container, false)

        initializeUi(view)
        checkConnection()

        searchBarHome.setOnClickListener {
            recipeViewModel.navigateTo(R.id.action_search)
        }

        btnFreeTrial.setOnClickListener {
            createMaterialAlertDialogBuilderOkCancel(
                requireContext(),
                "Start Free Trial",
                "Would you like to start a free trial to access premium recipes?",
                "Start Free Trial",
                "Cancel"
            ) {
                dataViewModel.updateSubscriptionState(true)
            }
        }

        btnCuisines.setOnClickListener {
            popup.show()
        }

        return view
    }

    private fun initObservers() {
        with(dataViewModel) {
            mainCuisine.observe(viewLifecycleOwner) { mainCuisine ->
                mainCuisine?.let { homeFavCuisines(it) }
            }
        }

        with(dataViewModel) {
            isSubscribed.observe(viewLifecycleOwner) { subscribed ->
                subscribed?.let {
                    when (it) {
                        true -> {
                            cardViewFreeTrial.visibility = View.GONE
                            constraintLayoutGold.visibility = View.VISIBLE
                        }
                        false -> {
                            cardViewFreeTrial.visibility = View.VISIBLE
                            constraintLayoutGold.visibility = View.GONE
                        }
                    }
                }
            }
        }

        with(dataViewModel) {
            cuisinesData.observe(viewLifecycleOwner) { data ->
                data?.forEachIndexed { index, item ->
                    popup.menu.add(0, index, 0, item)
                }
            }
        }

        // Handle categories
        with(homeViewModel) {
            getCategories()
            dataCategories.observe(viewLifecycleOwner) { response ->
                when (response) {
                    is Response.Loading -> shimmerCategories.startShimmer()
                    is Response.Success -> {
                        shimmerCategories.stopShimmer()
                        shimmerCategories.visibility = View.GONE
                        categoriesAdapter.submitList(response.data.categories)
                    }
                    is Response.Failure -> createFailureResponse(response, requireContext()) {
                        getCategories()
                    }
                }
            }
        }

        // Handle cuisine meals
        with(homeViewModel) {
            chosenCuisine?.let { getFilteredMealsByAreas(it) }
            filteredMealsByAreas.observe(viewLifecycleOwner) { response ->
                when (response) {
                    is Response.Loading -> shimmerCuisine.startShimmer()
                    is Response.Success -> {
                        shimmerCuisine.stopShimmer()
                        shimmerCuisine.visibility = View.GONE
                        cuisineAdapter.submitList(response.data.meals)
                    }
                    is Response.Failure -> createFailureResponse(response, requireContext()) {
                        chosenCuisine?.let { getFilteredMealsByAreas(it) }
                    }
                }
            }
        }

        // Handle random meals for recommendations
        with(homeViewModel) {
            getRandomMeals(10)
            someRecommendedMeals.observe(viewLifecycleOwner) { response ->
                when (response) {
                    is Response.Loading -> shimmerRecommendations.startShimmer()
                    is Response.Success -> {
                        shimmerRecommendations.stopShimmer()
                        shimmerRecommendations.visibility = View.GONE
                        recommendationsAdapter.submitList(response.data)
                    }
                    is Response.Failure -> createFailureResponse(response, requireContext()) {
                        getRandomMeals(10)
                    }
                }
            }
        }

        // Handle random meals for gold
        with(homeViewModel)
        {
            getRandomMeals(5, true)
            someGoldMeals.observe(viewLifecycleOwner) { response ->
                when (response) {
                    is Response.Loading -> shimmerGold.startShimmer()
                    is Response.Success -> {
                        shimmerGold.stopShimmer()
                        shimmerGold.visibility = View.GONE
                        goldAdapter.submitList(response.data)
                    }
                    is Response.Failure -> createFailureResponse(response, requireContext()) {
                        getRandomMeals(5, true)
                    }
                }
            }
        }

        // Handle user cuisines
        with(homeViewModel) {
            getUserCuisines()
            userCuisines.observe(viewLifecycleOwner) { response ->
                when (response) {
                    is Response.Loading -> shimmerCuisine.startShimmer()
                    is Response.Success -> {
                        val data = response.data
                        if (!data.isNullOrEmpty()) {
                            popup.menu.clear()
                            dataViewModel.updateMainCuisine(data[0])
                            dataViewModel.setCuisines(data)
                        } else {
                            val bottomSheetCuisines = BottomSheetCuisines()
                            bottomSheetCuisines.show(
                                requireActivity().supportFragmentManager,
                                BottomSheetCuisines.TAG
                            )
                        }
                    }

                    is Response.Failure -> createFailureResponse(response, requireContext()) {
                        getUserCuisines()
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
        shimmerCuisine = view.findViewById(R.id.shimmer_meal_by_fav_cuisine)
        shimmerGold = view.findViewById(R.id.shimmerGold)

        constraintLayoutGold = view.findViewById(R.id.constraintlayoutGold)
        cardViewFreeTrial = view.findViewById(R.id.cardViewFreeTrial)

        btnFreeTrial = view.findViewById(R.id.btnFreeTrial)
        btnDrawer = view.findViewById(R.id.btnHomeDrawer)
        textViewCuisines = view.findViewById(R.id.textViewCuisines)

        categoriesAdapter = AdapterRVCategories(listOf()) { name -> goToSearchCategories(name) }
        recommendationsAdapter = AdapterRVItemMeal(listOf()) { id -> goToDetails(id) }
        cuisineAdapter = AdapterRVItemMeal(listOf()) { id -> goToDetails(id) }
        goldAdapter = AdapterRVItemMeal(listOf()) { id -> goToDetails(id) }

        recyclerViewCategories.adapter = categoriesAdapter
        recyclerViewRecommendations.adapter = recommendationsAdapter
        recyclerViewCuisine.adapter = cuisineAdapter
        recyclerViewGold.adapter = goldAdapter

        setupRecyclerView(recyclerViewCategories)
        setupRecyclerView(recyclerViewRecommendations)
        setupRecyclerView(recyclerViewCuisine)
        setupRecyclerView(recyclerViewGold)

        btnCuisines = view.findViewById(R.id.btnCuisines)

        popup = PopupMenu(requireContext(), btnCuisines).apply {
            setOnMenuItemClickListener { item ->
                homeFavCuisines(item.title.toString())
                true
            }
        }

        drawer = requireActivity().findViewById(R.id.drawer)
        searchBarHome = view.findViewById(R.id.searchBar_home)

        btnDrawer.setOnClickListener {
            drawer.openDrawer(GravityCompat.START)
        }

        navController =
            requireActivity().supportFragmentManager.findFragmentById(R.id.nav_host_fragment)
                ?.findNavController()
    }

    private fun setupRecyclerView(recyclerView: RecyclerView) {
        recyclerView.layoutManager =
            LinearLayoutManager(requireContext(), LinearLayoutManager.HORIZONTAL, false)
    }

    private fun homeFavCuisines(cuisine: String) {
        chosenCuisine = cuisine
        homeViewModel.getFilteredMealsByAreas(cuisine)
        textViewCuisines.text = "$cuisine Recipes for you"
    }

    private fun goToDetails(id: String) {
        dataViewModel.setItemDetails(id)
        navController?.navigate(R.id.recipeDetailFragment, null, navOptions)
    }

    private fun goToSearchCategories(name: String) {
        dataViewModel.updateSearchCategory(name)
        recipeViewModel.navigateTo(R.id.action_search)
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
        }
    }
}