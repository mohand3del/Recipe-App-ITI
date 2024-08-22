package com.example.recipeappiti.home.view.adapter

import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.ImageView
import android.widget.TextView
import androidx.recyclerview.widget.RecyclerView
import com.bumptech.glide.Glide
import com.example.recipeappiti.R
import com.example.recipeappiti.home.model.Category

class AdapterRVCategories(private val categories: List<Category>) :
    RecyclerView.Adapter<AdapterRVCategories.CategoryViewHolder>() {

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): CategoryViewHolder {
        val view = LayoutInflater.from(parent.context)
            .inflate(R.layout.item_rv_home_categories, parent, false)
        return CategoryViewHolder(view)
    }

    override fun onBindViewHolder(holder: CategoryViewHolder, position: Int) {
        val category = categories[position]
        holder.bind(category)
    }

    override fun getItemCount(): Int = categories.size

    class CategoryViewHolder(itemView: View) : RecyclerView.ViewHolder(itemView) {
        private val imageView: ImageView = itemView.findViewById(R.id.item_category_image)
        private val textView: TextView = itemView.findViewById(R.id.item_category_title)

        fun bind(category: Category) {
            textView.text = category.strCategory

            // Load image using Glide
            Glide.with(itemView.context)
                .load(category.strCategoryThumb)
                .centerCrop()
                .into(imageView)
        }
    }
}
