package com.example.recipeappiti.favourite.view

import FavoriteViewModel
import FavoriteViewModelFactory
import FavouriteRecyclerAdapter
import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.GridView
import androidx.fragment.app.Fragment
import androidx.fragment.app.viewModels
import androidx.lifecycle.Observer
import androidx.recyclerview.widget.GridLayoutManager
import androidx.recyclerview.widget.RecyclerView
import com.example.recipeappiti.R
import com.example.recipeappiti.auth.model.data.LocalDataSourceImpl
import com.example.recipeappiti.auth.repository.UserRepositoryImpl
import com.example.recipeappiti.core.model.local.UserDatabase
import com.example.recipeappiti.details.viewmodel.DetailsViewModelFactory
import com.example.recipeappiti.home.data.remote.RemoteGsonDataImpl
import com.example.recipeappiti.home.repository.MealRepositoryImpl

class FavouriteFragment : Fragment() {


    private val favoriteViewModel: FavoriteViewModel by viewModels {
        val userRepository = UserRepositoryImpl(
            LocalDataSourceImpl(
                UserDatabase.getDatabaseInstance(requireContext()).userDao()
            )
        )
        val mealRepository = MealRepositoryImpl(RemoteGsonDataImpl())
        FavoriteViewModelFactory(userRepository, mealRepository)
    }
    private lateinit var favouriteRecycle: RecyclerView
    private val adapter = FavouriteRecyclerAdapter( { id -> favoriteViewModel.changeFavouriteState(id)}, { id -> checkFavouriteState(id) })


    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {

        return inflater.inflate(R.layout.fragment_favourite, container, false)


    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        initObservers()
        favouriteRecycle = view.findViewById(R.id.favouriteRecycle)

        val gridLayoutManager = GridLayoutManager(requireContext(), 2)
        favouriteRecycle.layoutManager = gridLayoutManager
        favouriteRecycle.adapter = adapter

    }

    private fun initObservers() {
        favoriteViewModel.recipes.observe(viewLifecycleOwner, Observer { recipes ->

            adapter.updateData(recipes)

        })

    }
    private  fun checkFavouriteState(recipeId: String) :Boolean{
        favoriteViewModel.checkFavouriteState(recipeId)
        var isFavouriteTwo = false


        favoriteViewModel.isFavourite.observe(viewLifecycleOwner, Observer { isFavourite ->
            isFavouriteTwo = isFavourite
        })
        return  isFavouriteTwo



    }
}
