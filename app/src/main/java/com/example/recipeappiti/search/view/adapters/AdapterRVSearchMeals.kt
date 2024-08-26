package com.example.recipeappiti.search.view.adapters

import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.ImageView
import android.widget.TextView
import androidx.recyclerview.widget.RecyclerView
import com.bumptech.glide.Glide
import com.example.recipeappiti.R
import com.example.recipeappiti.home.model.Meal

class AdapterRVSearchMeals(private val meals: List<Meal>) :
    RecyclerView.Adapter<AdapterRVSearchMeals.MealViewHolder>() {

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): MealViewHolder {
        val view = LayoutInflater.from(parent.context)
            .inflate(R.layout.item_rv_second_view_of_meals, parent, false)
        return MealViewHolder(view)
    }

    override fun onBindViewHolder(holder: MealViewHolder, position: Int) {
        val meal = meals[position]
        holder.bind(meal)
    }

    override fun getItemCount(): Int = meals.size

    class MealViewHolder(itemView: View) : RecyclerView.ViewHolder(itemView) {
        private val imageView: ImageView = itemView.findViewById(R.id.item_image_search)
        private val titleView: TextView = itemView.findViewById(R.id.item_title_search)

        fun bind(meal: Meal) {
            titleView.text = formatDescription(meal.strMeal)

            Glide.with(itemView.context)
                .load(meal.strMealThumb)
                .centerCrop()
                .into(imageView)
        }

        private fun formatDescription(
            description: String,
            maxLength: Int = 50,
            suffix: String = " ...Show more"
        ): String {
            return if (description.length > maxLength) {
                "${description.substring(0, maxLength)}$suffix"
            } else {
                description
            }
        }
    }
}
