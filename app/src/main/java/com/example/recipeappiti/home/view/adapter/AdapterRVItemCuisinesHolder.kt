package com.example.recipeappiti.home.view.adapter

import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.TextView
import android.widget.Toast
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import com.bumptech.glide.Glide
import com.example.recipeappiti.R
import com.example.recipeappiti.home.model.Meal
import com.facebook.shimmer.ShimmerFrameLayout

class AdapterRVItemCuisinesHolder(private val data: Map<String, List<Meal>>) :
    RecyclerView.Adapter<AdapterRVItemCuisinesHolder.MealViewHolder>() {

    // Convert keys (cuisines) into a list to access by index
    private val cuisineKeys: List<String> = data.keys.toList()

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): MealViewHolder {
        val view = LayoutInflater.from(parent.context)
            .inflate(R.layout.item_rv_cuisines_holder, parent, false)
        return MealViewHolder(view)
    }

    override fun onBindViewHolder(holder: MealViewHolder, position: Int) {
        // Get the cuisine and its associated meals
        val cuisine = cuisineKeys[position]
        val meals = data[cuisine] ?: emptyList()

        // Bind data to the ViewHolder
        holder.bind(cuisine, meals)
    }

    override fun getItemCount(): Int = data.size

    class MealViewHolder(itemView: View) : RecyclerView.ViewHolder(itemView) {
        private val title: TextView = itemView.findViewById(R.id.item_cuisinesTitleHolder)
        private val recyclerView: RecyclerView = itemView.findViewById(R.id.item_recyclerviewCuisinesHolder)
        private val shimmer: ShimmerFrameLayout = itemView.findViewById(R.id.shimmerCuisinesHolder)

        init {
            // Set up the nested RecyclerView
            recyclerView.apply {
                layoutManager =
                    LinearLayoutManager(itemView.context, LinearLayoutManager.HORIZONTAL, false)
            }
        }

        fun bind(cuisine: String, meals: List<Meal>) {
            // Bind the cuisine name
            title.text = cuisine


            // Bind the meals to the nested RecyclerView adapter
            val adapter = AdapterRVItemMeal(meals)
            recyclerView.adapter = adapter

            // Stop shimmer effect when data is loaded
            shimmer.stopShimmer()
            shimmer.visibility = View.GONE
        }
    }
}
