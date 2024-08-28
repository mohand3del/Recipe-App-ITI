package com.example.recipeappiti.favourite.view

import com.example.recipeappiti.core.viewmodel.DataViewModel
import com.example.recipeappiti.core.viewmodel.DataViewModelFactory
import FavouriteRecyclerAdapter
import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.fragment.app.Fragment
import androidx.fragment.app.activityViewModels
import androidx.lifecycle.Observer
import androidx.lifecycle.viewModelScope
import androidx.recyclerview.widget.GridLayoutManager
import androidx.recyclerview.widget.RecyclerView
import com.example.recipeappiti.R
import com.example.recipeappiti.core.model.local.source.LocalDataSourceImpl
import com.example.recipeappiti.core.model.local.repository.UserRepositoryImpl
import com.example.recipeappiti.core.model.local.source.UserDatabase
import com.example.recipeappiti.core.model.remote.source.RemoteGsonDataImpl
import com.example.recipeappiti.core.model.remote.repository.MealRepositoryImpl
import kotlinx.coroutines.launch

class FavouriteFragment : Fragment() {
    private val dataViewModel: DataViewModel by activityViewModels {
        val userRepository = UserRepositoryImpl(
            LocalDataSourceImpl(
                UserDatabase.getDatabaseInstance(requireContext()).userDao()
            )
        )
        val mealRepository = MealRepositoryImpl(RemoteGsonDataImpl())
        DataViewModelFactory(userRepository, mealRepository)
    }
    private lateinit var favouriteRecycle: RecyclerView
    private lateinit var adapter: FavouriteRecyclerAdapter
    private var favouriteState = false


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
        adapter = FavouriteRecyclerAdapter { id, isChange, onComplete ->
            changeFavouriteState(id, isChange, onComplete)
        }
        favouriteRecycle.layoutManager = GridLayoutManager(requireContext(), 2)
        favouriteRecycle.adapter = adapter

    }

    private fun initObservers() {
        dataViewModel.recipes.observe(viewLifecycleOwner, Observer { recipes ->
            adapter.updateData(recipes)
        })

        dataViewModel.isFavourite.observe(viewLifecycleOwner, Observer { isFavourite ->
            favouriteState = isFavourite
        })
    }

    private fun changeFavouriteState(
        recipeId: String,
        isChange: Boolean,
        onComplete: (Boolean) -> Unit
    ) {
        dataViewModel.viewModelScope.launch {
            dataViewModel.changeFavouriteState(recipeId, isChange)
        }.invokeOnCompletion {
            onComplete(favouriteState)
        }
    }
}
