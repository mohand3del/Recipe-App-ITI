package com.example.recipeappiti.home.view.fragments

import android.content.Context
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
import androidx.fragment.app.Fragment
import androidx.fragment.app.viewModels
import androidx.lifecycle.LiveData
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
import com.example.recipeappiti.main.view.interfaces.OnActionListener
import com.facebook.shimmer.ShimmerFrameLayout
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

    private var listener: OnActionListener? = null

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


    override fun onAttach(context: Context) {
        super.onAttach(context)
        if (context is OnActionListener) {
            listener = context
        } else {
            throw RuntimeException("$context must implement OnButtonClickListener")
        }
    }

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
        viewModel.getCategories()

        observeResponse(
            liveData = viewModel.dataCategories,
            shimmerView = shimmerCategories,
            recyclerView = recyclerViewCategories,
            adapterFactory = { data -> AdapterRVCategories(data.categories) }
        ) {}



        viewModel.getSomeRandomMeal(10)
        observeResponse(
            liveData = viewModel.someRandomMeal,
            shimmerView = shimmerRecommendations,
            recyclerView = recyclerViewRecommendations,
            adapterFactory = { data -> AdapterRVItemMeal(data) }
        ) {}

        viewModel.getSomeRandomMeal(5)

        observeResponse(
            liveData = viewModel.someRandomMeal,
            shimmerView = shimmerGold,
            recyclerView = recyclerViewGold,
            adapterFactory = { data -> AdapterRVItemMeal(data) }
        ) {}

        viewModel.getUserCuisines()

        observeResponse(
            liveData = viewModel.userCuisines,
            shimmerView = shimmerMealByArea,
            recyclerView = null,
            adapterFactory = null
        ) { data ->
            if (data != null && data[0].isNotEmpty()) {
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

        btnDrawer.setOnClickListener {

            listener?.onActionListener()

        }
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
                    val adapter = AdapterRVItemMeal(response.data.meals)
                    recyclerViewCuisine.adapter = adapter
                }

                is Response.Failure -> {
                    when (val failureReason = response.reason) {
                        is FailureReason.NoInternet -> {
                            // Show no internet connection message
                            CreateMaterialAlertDialogBuilder.createMaterialAlertDialogBuilderOk(
                                requireContext(),
                                title = "No Internet Connection",
                                message = "Please check your internet connection and try again.",
                                positiveBtnMsg = "OK"
                            ) {
                                // Define any action if needed after the dialog is dismissed
                            }
                        }

                        is FailureReason.UnknownError -> {
                            // Show unknown error message with the error details
                            val errorMessage = failureReason.error
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
                    }
                }
            }
        }
    }

    private fun <T> observeResponse(
        liveData: LiveData<Response<T>>,
        shimmerView: ShimmerFrameLayout,
        recyclerView: RecyclerView?,
        adapterFactory: ((T) -> RecyclerView.Adapter<*>)?,
        successFun: ((T) -> Unit)?
    ) {

        liveData.observe(viewLifecycleOwner) { response ->

            when (response) {
                is Response.Loading -> {
                    shimmerView.startShimmer()
                }

                is Response.Success -> {

                    if (recyclerView != null && adapterFactory != null) {

                        shimmerView.stopShimmer()
                        shimmerView.visibility = View.GONE
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