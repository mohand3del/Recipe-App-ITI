package com.example.recipeappiti.profile.view.fragments

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.fragment.app.Fragment
import com.example.recipeappiti.R
import com.google.android.material.bottomnavigation.BottomNavigationView

class ProfileFragment : Fragment() {

    private lateinit var bottomNavigationView: BottomNavigationView

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        return inflater.inflate(R.layout.fragment_profile, container, false)
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        initViews()

    }

    private fun initViews() {

        bottomNavigationView = requireActivity().findViewById(R.id.bottom_navigation)
        bottomNavigationView.visibility = View.GONE

    }

    override fun onDestroyView() {
        super.onDestroyView()

        bottomNavigationView.visibility = View.VISIBLE

    }


}