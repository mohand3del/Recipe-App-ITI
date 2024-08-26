package com.example.recipeappiti.search.view.adapters

import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.TextView
import androidx.recyclerview.widget.RecyclerView
import com.example.recipeappiti.R
import com.example.recipeappiti.home.model.Category

class AdapterRVCategoryFilters(
    private val filters: List<Category>,
    private val doFilter: (String) -> Unit
) : RecyclerView.Adapter<AdapterRVCategoryFilters.MealViewHolder>() {

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): MealViewHolder {
        val view = LayoutInflater.from(parent.context)
            .inflate(R.layout.item_rv_filter, parent, false)
        return MealViewHolder(view)
    }

    override fun onBindViewHolder(holder: MealViewHolder, position: Int) {
        val filter = filters[position]
        holder.titleView.text = filter.strCategory

        holder.itemView.setOnClickListener {

            doFilter(filter.strCategory)

        }

    }

    override fun getItemCount(): Int = filters.size

    class MealViewHolder(itemView: View) : RecyclerView.ViewHolder(itemView) {
        val titleView: TextView = itemView.findViewById(R.id.item_filter_name)
    }
}
