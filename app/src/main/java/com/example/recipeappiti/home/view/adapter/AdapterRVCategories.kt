package com.example.recipeappiti.home.view.adapter

import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.ImageView
import android.widget.TextView
import androidx.recyclerview.widget.RecyclerView
import com.bumptech.glide.Glide
import com.example.recipeappiti.R
import com.example.recipeappiti.core.model.remote.Category

class AdapterRVCategories(
    private var categories: List<Category>,
    private val goToSearch: ((id: String) -> Unit)? = null
) : RecyclerView.Adapter<AdapterRVCategories.CategoryViewHolder>() {

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): CategoryViewHolder {
        val view = LayoutInflater.from(parent.context)
            .inflate(R.layout.item_rv_home_categories, parent, false)
        return CategoryViewHolder(view)
    }

    override fun onBindViewHolder(holder: CategoryViewHolder, position: Int) {
        val category = categories[position]

        with(holder) {
            textView.text = category.strCategory
            Glide.with(itemView.context)
                .load(category.strCategoryThumb)
                .centerCrop()
                .into(imageView)

            itemView.setOnClickListener { goToSearch?.let { it(category.strCategory) } }
        }
    }

    override fun getItemCount(): Int = categories.size

    fun submitList(newCategories: List<Category>) {
        categories = newCategories
        notifyDataSetChanged()
    }

    class CategoryViewHolder(itemView: View) : RecyclerView.ViewHolder(itemView) {
        val imageView: ImageView = itemView.findViewById(R.id.item_category_image)
        val textView: TextView = itemView.findViewById(R.id.item_category_title)
    }
}

