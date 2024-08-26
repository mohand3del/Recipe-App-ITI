package com.example.recipeappiti.details.view

import android.content.pm.ActivityInfo
import android.content.res.Configuration
import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.FrameLayout
import android.widget.ImageView
import android.widget.TextView
import androidx.activity.OnBackPressedCallback
import androidx.core.view.WindowInsetsCompat
import androidx.core.view.WindowInsetsControllerCompat
import androidx.core.view.isVisible
import androidx.fragment.app.Fragment
import androidx.fragment.app.viewModels
import androidx.lifecycle.Observer
import androidx.navigation.NavController
import androidx.navigation.fragment.findNavController
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import com.bumptech.glide.Glide
import com.example.recipeappiti.R
import com.example.recipeappiti.auth.model.data.LocalDataSourceImpl
import com.example.recipeappiti.auth.repository.UserRepositoryImpl
import com.example.recipeappiti.core.model.local.UserDatabase
import com.example.recipeappiti.details.view.adapters.IngredientsRecyclerViewAdapter
import com.example.recipeappiti.details.viewmodel.DetailsViewModel
import com.example.recipeappiti.details.viewmodel.DetailsViewModelFactory
import com.example.recipeappiti.home.data.remote.RemoteGsonDataImpl
import com.example.recipeappiti.home.model.Meal
import com.example.recipeappiti.home.repository.MealRepositoryImpl
import com.google.android.material.card.MaterialCardView
import com.pierfrancescosoffritti.androidyoutubeplayer.core.player.YouTubePlayer
import com.pierfrancescosoffritti.androidyoutubeplayer.core.player.listeners.AbstractYouTubePlayerListener
import com.pierfrancescosoffritti.androidyoutubeplayer.core.player.listeners.FullscreenListener
import com.pierfrancescosoffritti.androidyoutubeplayer.core.player.options.IFramePlayerOptions
import com.pierfrancescosoffritti.androidyoutubeplayer.core.player.views.YouTubePlayerView

class RecipeDetailFragment : Fragment() {
    private val detailsViewModel: DetailsViewModel by viewModels {
        val userRepository = UserRepositoryImpl(
            LocalDataSourceImpl(
                UserDatabase.getDatabaseInstance(requireContext()).userDao())
        )
        val mealRepository = MealRepositoryImpl(RemoteGsonDataImpl())
        DetailsViewModelFactory(mealRepository, userRepository)
    }

    // Youtube
    private lateinit var youtubePlayerView: YouTubePlayerView
    private lateinit var youtubePlayer: YouTubePlayer
    private lateinit var fullScreenContainer: FrameLayout
    private lateinit var iFramePlayerOptions: IFramePlayerOptions
    private var isFullscreen = false
    private val onBackPressedCallback = object : OnBackPressedCallback(true) {
        override fun handleOnBackPressed() {
            if (isFullscreen) {
                // if the player is in fullscreen, exit fullscreen
                youtubePlayer.toggleFullscreen()
            } else {
                //finish()
            }
        }
    }

    private var recipeId: String = "52772"
    private lateinit var navController: NavController
    private lateinit var ingredientsRecycler: RecyclerView
    private lateinit var ingredientsAdapter: IngredientsRecyclerViewAdapter
    private lateinit var ingredientsCard: MaterialCardView
    private lateinit var instructionsCard: MaterialCardView
    private lateinit var mealName: TextView
    private lateinit var mealCategory: TextView
    private lateinit var mealArea: TextView
    private lateinit var instructionsText: TextView
    private lateinit var backImage: ImageView
    private lateinit var mealImage: ImageView
    private lateinit var favouriteImage: ImageView
    private lateinit var ingredientsArrow: ImageView
    private lateinit var instructionsArrow: ImageView


    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        requireActivity().onBackPressedDispatcher.addCallback(this,onBackPressedCallback)
    }

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        // Inflate the layout for this fragment
        return inflater.inflate(R.layout.fragment_recipe_detail, container, false)
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        arguments?.let {
            recipeId = RecipeDetailFragmentArgs.fromBundle(it).recipeId
            detailsViewModel.getMealDetails(recipeId)
        }
        initViews()
        initObservers()
        initListeners()
    }

    private fun initViews() {
        // Youtube
        youtubePlayerView = requireView().findViewById(R.id.youtubePlayer)
        fullScreenContainer = requireView().findViewById(R.id.fullScreenContainer)
        iFramePlayerOptions = IFramePlayerOptions.Builder()
            .controls(1)
            .fullscreen(1) // enable full screen button
            .build()
        youtubePlayerView.enableAutomaticInitialization = false
        lifecycle.addObserver(youtubePlayerView)


        navController = findNavController()
        ingredientsRecycler = requireView().findViewById(R.id.ingredientsRecycler)
        ingredientsAdapter = IngredientsRecyclerViewAdapter()
        ingredientsCard = requireView().findViewById(R.id.ingredientsCard)
        instructionsCard = requireView().findViewById(R.id.instructionsCard)
        backImage = requireView().findViewById(R.id.btnBack)
        mealImage = requireView().findViewById(R.id.mealImage)
        favouriteImage = requireView().findViewById(R.id.btnFavourite)
        ingredientsArrow = requireView().findViewById(R.id.ingredientsArrow)
        instructionsArrow = requireView().findViewById(R.id.instructionsArrow)
        mealName = requireView().findViewById(R.id.mealName)
        mealCategory = requireView().findViewById(R.id.mealCategory)
        mealArea = requireView().findViewById(R.id.mealArea)
        instructionsText = requireView().findViewById(R.id.instructionsText)

    }

    private fun initListeners() {
        backImage.setOnClickListener { navController.popBackStack() }

        favouriteImage.setOnClickListener { detailsViewModel.changeFavouriteState(recipeId) }

        ingredientsCard.setOnClickListener {
            ingredientsRecycler.isVisible = !ingredientsRecycler.isVisible
            ingredientsArrow.rotation = if (ingredientsRecycler.isVisible) 180f else 0f
        }

        instructionsCard.setOnClickListener {
            instructionsText.isVisible = !instructionsText.isVisible
            instructionsArrow.rotation = if (instructionsText.isVisible) 180f else 0f
        }
    }

    private fun initObservers() {
        detailsViewModel.recipe.observe(viewLifecycleOwner, Observer { result ->
            bindData(result)
        })

        detailsViewModel.isFavourite.observe(viewLifecycleOwner, Observer { result ->
            favouriteImage.setImageResource(
                when (result) {
                    true -> R.drawable.loved_icon
                    false -> R.drawable.love_icon_light
                }
            )
        })
    }

    private fun bindData(recipe: Meal) {
        detailsViewModel.checkFavouriteState(recipe.idMeal)
        ingredientsAdapter.updateData(recipe.listIngredientsWithMeasures)
        ingredientsRecycler.adapter = ingredientsAdapter
        ingredientsRecycler.layoutManager = LinearLayoutManager(requireContext())
        mealName.text = recipe.strMeal
        mealCategory.text = recipe.strCategory
        mealArea.text = recipe.strArea
        Glide.with(requireContext()).load(recipe.strMealThumb).into(mealImage)
        instructionsText.text = recipe.strInstructions
        activateYoutubePlayer(recipe.strYoutube)
    }

    private fun activateYoutubePlayer(url: String) {
        val youtubePlayerListener = object : AbstractYouTubePlayerListener() {
            override fun onReady(youTubePlayer: YouTubePlayer) {
                val videoId = detailsViewModel.extractYouTubeVideoId(url)
                youtubePlayer = youTubePlayer
                youtubePlayer.cueVideo(videoId, 0f)
            }
        }

        youtubePlayerView.addFullscreenListener(object : FullscreenListener {
            override fun onEnterFullscreen(fullscreenView: View, exitFullscreen: () -> Unit) {
                isFullscreen = true
                fullScreenContainer.addView(fullscreenView)
                fullScreenContainer.visibility = View.VISIBLE
                WindowInsetsControllerCompat(
                    requireActivity().window, requireView().findViewById(R.id.rootView)
                ).apply {
                    hide(WindowInsetsCompat.Type.statusBars())
                    systemBarsBehavior = WindowInsetsControllerCompat.BEHAVIOR_SHOW_TRANSIENT_BARS_BY_SWIPE
                }

                if (requireActivity().requestedOrientation != ActivityInfo.SCREEN_ORIENTATION_LANDSCAPE) {
                    requireActivity().requestedOrientation = ActivityInfo.SCREEN_ORIENTATION_SENSOR_LANDSCAPE
                }
            }

            override fun onExitFullscreen() {
                isFullscreen = false
                fullScreenContainer.removeAllViews()
                fullScreenContainer.visibility = View.GONE
                WindowInsetsControllerCompat(
                    requireActivity().window, requireView().findViewById(R.id.rootView)
                ).apply {
                    show(WindowInsetsCompat.Type.statusBars())
                }
                if (requireActivity().requestedOrientation != ActivityInfo.SCREEN_ORIENTATION_SENSOR) {
                    requireActivity().requestedOrientation = ActivityInfo.SCREEN_ORIENTATION_SENSOR_PORTRAIT
                }
            }

        })
        youtubePlayerView.enableAutomaticInitialization = false
        youtubePlayerView.initialize(youtubePlayerListener,iFramePlayerOptions)
    }

    override fun onConfigurationChanged(newConfig: Configuration) {
        super.onConfigurationChanged(newConfig)
        if (newConfig.orientation == Configuration.ORIENTATION_LANDSCAPE){
            if (!isFullscreen){
                youtubePlayer.toggleFullscreen()
            }
        }else if (newConfig.orientation == Configuration.ORIENTATION_PORTRAIT){
            if (isFullscreen){
                youtubePlayer.toggleFullscreen()
            }
        }
    }

    override fun onDestroyView() {
        super.onDestroyView()
        youtubePlayerView.release()
    }
}