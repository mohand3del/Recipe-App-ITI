package com.example.recipeappiti.home.view.fragments

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Button
import android.widget.ImageView
import android.widget.PopupMenu
import android.widget.TextView
import android.widget.Toast
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
import com.example.recipeappiti.core.model.local.repository.UserRepositoryImpl
import com.example.recipeappiti.core.model.local.source.LocalDataSourceImpl
import com.example.recipeappiti.core.model.local.source.UserDatabase
import com.example.recipeappiti.core.model.remote.Response
import com.example.recipeappiti.core.model.remote.repository.MealRepositoryImpl
import com.example.recipeappiti.core.model.remote.source.RemoteGsonDataImpl
import com.example.recipeappiti.core.util.CreateMaterialAlertDialogBuilder.createFailureResponse
import com.example.recipeappiti.core.util.CreateMaterialAlertDialogBuilder.createMaterialAlertDialogBuilderOkCancel
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
    private lateinit var bottomNavigationView: BottomNavigationView
    private lateinit var drawer: DrawerLayout
    private lateinit var searchBarHome: TextView

    private var navController: NavController? = null

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {

        val view = inflater.inflate(R.layout.fragment_home, container, false)

        initializeUi(view)

        initUi()

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

        with(dataViewModel) {

            mainCuisine.observe(viewLifecycleOwner) { mainCuisine ->

                mainCuisine?.let { homeFavCuisines(it) }

            }

        }

        with(dataViewModel){

            cuisinesData.observe(viewLifecycleOwner){data->

                data?.forEachIndexed { index, item ->
                    popup.menu.add(0, index, 0, item)
                }

            }

        }

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
                            AdapterRVCategories(response.data.categories) { name ->
                                goToSearchCategories(
                                    name
                                )
                            }
                        recyclerViewCategories.adapter = adapter
                    }

                    is Response.Failure -> {
                        createFailureResponse(response, requireContext())
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
                        createFailureResponse(response, requireContext())
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
                        recyclerViewGold.adapter = adapter
                    }

                    is Response.Failure -> {
                        createFailureResponse(response, requireContext())
                    }
                }

            }
        }

        with(viewModel) {
            getUserCuisines()
            userCuisines.observe(viewLifecycleOwner) { response ->

                when (response) {
                    is Response.Loading -> {
                        shimmerCuisine.startShimmer()
                    }

                    is Response.Success -> {
                        val data = response.data

                        if (!data.isNullOrEmpty() && data[0].isNotEmpty()) {

                            popup.menu.clear()

                            with(dataViewModel) {

                                updateMainCuisine(data[0])

                            }


                        } else {
                            val bottomSheetCuisines = BottomSheetCuisines()
                            bottomSheetCuisines.show(
                                requireActivity().supportFragmentManager,
                                BottomSheetCuisines.TAG
                            )

                        }

                    }

                    is Response.Failure -> {
                        createFailureResponse(response, requireContext())
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

        bottomNavigationView = requireActivity().findViewById(R.id.bottom_navigation)

        btnDrawer.setOnClickListener {
            drawer.openDrawer(GravityCompat.START)
        }

        navController = requireActivity().supportFragmentManager.findFragmentById(R.id.nav_host_fragment)?.findNavController()
    }

    private fun setupRecyclerView(recyclerView: RecyclerView) {
        recyclerView.layoutManager = LinearLayoutManager(requireContext(), LinearLayoutManager.HORIZONTAL, false)
    }


    private fun homeFavCuisines(
        cuisine: String
    ) {
        textViewCuisines.text = "$cuisine Recipes for you"

        viewModel.getFilteredMealsByAreas(cuisine)

        viewModel.filteredMealsByAreas.observe(viewLifecycleOwner) { response ->
            when (response) {
                is Response.Loading -> {
                    shimmerCuisine.startShimmer()
                }

                is Response.Success -> {
                    shimmerCuisine.stopShimmer()
                    shimmerCuisine.visibility = View.GONE
                    val adapter = AdapterRVItemMeal(response.data.meals) { id -> goToDetails(id) }
                    recyclerViewCuisine.adapter = adapter
                }

                is Response.Failure -> {
                    createFailureResponse(response, requireContext())
                }
            }
        }
    }

    private fun goToDetails(id: String) {

        dataViewModel.setItemDetails(id)

        navController?.navigate(R.id.recipeDetailFragment)

    }

    private fun goToSearchCategories(name: String) {

        dataViewModel.updateSearchCategory(name)

        recipeViewModel.navigateTo(R.id.action_search)

    }

}